package one.superstack.thingstack.service;

import com.mongodb.client.result.UpdateResult;
import one.superstack.thingstack.auth.actor.AuthenticatedActor;
import one.superstack.thingstack.embedded.HookReference;
import one.superstack.thingstack.enums.Permission;
import one.superstack.thingstack.enums.TargetType;
import one.superstack.thingstack.exception.ClientException;
import one.superstack.thingstack.exception.NotFoundException;
import one.superstack.thingstack.model.Rule;
import one.superstack.thingstack.pojo.Hook;
import one.superstack.thingstack.repository.RuleRepository;
import one.superstack.thingstack.request.AccessRequest;
import one.superstack.thingstack.request.RuleCreationRequest;
import one.superstack.thingstack.request.RuleUpdateRequest;
import one.superstack.thingstack.util.TopicUtil;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

@Service
public class RuleService {

    private final RuleRepository ruleRepository;

    private final HookService hookService;

    private final AccessService accessService;

    private final MongoTemplate mongoTemplate;

    @Autowired
    public RuleService(RuleRepository ruleRepository, HookService hookService, AccessService accessService, MongoTemplate mongoTemplate) {
        this.ruleRepository = ruleRepository;
        this.hookService = hookService;
        this.accessService = accessService;
        this.mongoTemplate = mongoTemplate;
    }

    public Rule create(RuleCreationRequest ruleCreationRequest, AuthenticatedActor creator) {
        if (nameExists(ruleCreationRequest.getName(), creator.getOrganizationId())) {
            throw new ClientException("Rule " + ruleCreationRequest.getName() + " already exists");
        }

        if (null != ruleCreationRequest.getHooks() && ruleCreationRequest.getHooks().isEmpty()) {
            ruleCreationRequest.setHooks(null);
        }

        if (null != ruleCreationRequest.getHooks()) {
            if (!hookService.allExist(ruleCreationRequest.getHooks(), creator.getOrganizationId())) {
                throw new ClientException("Hooks not found");
            }
        }

        Rule rule = new Rule(ruleCreationRequest.getName(),
                ruleCreationRequest.getDescription(),
                ruleCreationRequest.getTopic(),
                ruleCreationRequest.getExpression(),
                ruleCreationRequest.getHooks(),
                creator.getOrganizationId(),
                creator.getType(),
                creator.getId());

        rule = ruleRepository.save(rule);

        accessService.add(new AccessRequest(TargetType.RULE, rule.getId(), creator.getType(), creator.getId(), Set.of(Permission.ALL)), creator);
        return rule;
    }

    public List<Rule> list(String organizationId, Pageable pageable) {
        return ruleRepository.findByOrganizationId(organizationId, pageable);
    }

    public Rule get(String ruleId, String organizationId) throws Throwable {
        return ruleRepository.findByIdAndOrganizationId(ruleId, organizationId)
                .orElseThrow((Supplier<Throwable>) () -> new NotFoundException("Rule not found"));
    }

    public Rule get(String ruleId) throws Throwable {
        return ruleRepository.findById(ruleId)
                .orElseThrow((Supplier<Throwable>) () -> new NotFoundException("Rule not found"));
    }

    public Rule update(String ruleId, RuleUpdateRequest ruleUpdateRequest, String organizationId) throws Throwable {
        Rule rule = get(ruleId, organizationId);

        if (null != ruleUpdateRequest.getName()) {
            if (ruleRepository.existsByIdNotAndNameAndOrganizationId(ruleId, ruleUpdateRequest.getName(), organizationId)) {
                throw new ClientException("Rule " + ruleUpdateRequest.getName() + " already exists");
            }

            rule.setName(ruleUpdateRequest.getName());
        }

        rule.setDescription(ruleUpdateRequest.getDescription());

        if (null != ruleUpdateRequest.getExpression()) {
            rule.setExpression(ruleUpdateRequest.getExpression());
        }

        if (null != ruleUpdateRequest.getTopic()) {
            if (!TopicUtil.validateOrganization(ruleUpdateRequest.getTopic(), organizationId)) {
                throw new ClientException("Topic does not belong to this tenant");
            }

            rule.setTopic(ruleUpdateRequest.getTopic());
        }

        rule.setModifiedOn(new Date());
        return ruleRepository.save(rule);
    }

    public Rule delete(String ruleId, String organizationId) throws Throwable {
        Rule rule = get(ruleId, organizationId);
        ruleRepository.delete(rule);
        accessService.deleteAllForTarget(TargetType.RULE, rule.getId());
        return rule;
    }

    public void addHook(String ruleId, HookReference hookReference, String organizationId) {
        if (!hookService.exists(hookReference, organizationId)) {
            throw new NotFoundException("Hook not found");
        }

        UpdateResult updateResult = mongoTemplate.updateFirst(Query.query(Criteria
                        .where("_id").is(new ObjectId(ruleId))
                        .and("organizationId").is(organizationId)
                ),
                new Update()
                        .set("modifiedOn", new Date())
                        .addToSet("hooks", hookReference),
                Rule.class);

        if (updateResult.getMatchedCount() == 0) {
            throw new NotFoundException("Rule not found");
        }
    }

    public void deleteHook(String ruleId, HookReference hookReference, String organizationId) {
        UpdateResult updateResult = mongoTemplate.updateFirst(Query.query(Criteria
                        .where("_id").is(new ObjectId(ruleId))
                        .and("organizationId").is(organizationId)
                ),
                new Update()
                        .set("modifiedOn", new Date())
                        .pull("hooks", hookReference),
                Rule.class);

        if (updateResult.getMatchedCount() == 0) {
            throw new NotFoundException("Rule not found");
        }
    }

    public List<Hook> listHooks(String ruleId, String organizationId) throws Throwable {
        return hookService.fetch(get(ruleId, organizationId).getHooks());
    }

    public Boolean exists(String ruleId, String organizationId) {
        return ruleRepository.existsByIdAndOrganizationId(ruleId, organizationId);
    }

    private Boolean nameExists(String name, String organizationId) {
        return ruleRepository.existsByNameAndOrganizationId(name, organizationId);
    }
}
