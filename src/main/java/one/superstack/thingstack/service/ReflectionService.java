package one.superstack.thingstack.service;

import one.superstack.thingstack.auth.reflection.AuthenticatedReflection;
import one.superstack.thingstack.embedded.Bus;
import one.superstack.thingstack.enums.TopicAccess;
import one.superstack.thingstack.exception.ClientException;
import one.superstack.thingstack.exception.InvalidTokenException;
import one.superstack.thingstack.exception.NotFoundException;
import one.superstack.thingstack.model.Reflection;
import one.superstack.thingstack.model.Thing;
import one.superstack.thingstack.repository.ReflectionRepository;
import one.superstack.thingstack.request.CustomBusTopicAccessRequest;
import one.superstack.thingstack.request.BusTopicChangeRequest;
import one.superstack.thingstack.response.AccessKeyResponse;
import one.superstack.thingstack.util.Random;
import one.superstack.thingstack.util.TopicUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.function.Supplier;

@Service
public class ReflectionService {

    private final ReflectionRepository reflectionRepository;

    private final MqttUserService mqttUserService;

    private final MqttAclService mqttAclService;

    private final MongoTemplate mongoTemplate;

    @Autowired
    public ReflectionService(ReflectionRepository reflectionRepository, MqttUserService mqttUserService, MqttAclService mqttAclService, MongoTemplate mongoTemplate) {
        this.reflectionRepository = reflectionRepository;
        this.mqttUserService = mqttUserService;
        this.mqttAclService = mqttAclService;
        this.mongoTemplate = mongoTemplate;
    }

    public void init(Thing thing) {

        Reflection reflection = new Reflection(thing.getId(),
                Random.generateRandomString(128),
                thing.getBus(),
                thing.getOrganizationId());

        reflection = reflectionRepository.save(reflection);
        initMqtt(reflection);
    }

    public Reflection get(String thingId, String organizationId) throws Throwable {
        return reflectionRepository.findByThingIdAndOrganizationId(thingId, organizationId)
                .orElseThrow((Supplier<Throwable>) () -> new NotFoundException("Thing reflection not found"));
    }

    public Reflection get(String thingId) throws Throwable {
        return reflectionRepository.findById(thingId)
                .orElseThrow((Supplier<Throwable>) () -> new NotFoundException("Thing reflection not found"));
    }

    public AccessKeyResponse getAccessKey(String thingId, String organizationId) throws Throwable {
        return new AccessKeyResponse(get(thingId, organizationId).getAccessKey());
    }

    public AccessKeyResponse resetAccessKey(String thingId, String organizationId) throws Throwable {
        Reflection reflection = get(thingId, organizationId);

        reflection.setAccessKey(Random.generateRandomString(128));

        reflection.setModifiedOn(new Date());
        reflection = reflectionRepository.save(reflection);

        mqttUserService.changePassword(reflection.getId(), reflection.getAccessKey());
        return new AccessKeyResponse(reflection.getAccessKey());
    }

    public Reflection delete(String thingId) throws Throwable {
        Reflection reflection = get(thingId);
        reflectionRepository.delete(reflection);

        // Clean up mqtt stuff
        mqttUserService.delete(reflection.getId());
        mqttAclService.deleteAll(reflection.getId());

        return reflection;
    }

    public void changeAffordanceTopic(String thingId, BusTopicChangeRequest busTopicChangeRequest) {
        String busFieldKey = Bus.getFieldKey(busTopicChangeRequest.getType(), busTopicChangeRequest.getKey());

        Reflection reflection = mongoTemplate.findAndModify(Query.query(Criteria
                        .where("thingId").is(thingId)
                        .and(busFieldKey).exists(true)
                ),
                new Update()
                        .set("modifiedOn", new Date())
                        .set(busFieldKey, busTopicChangeRequest.getTopic()),
                Reflection.class);

        if (null == reflection) {
            throw new NotFoundException("Reflection bus topic not found");
        }

        TopicAccess topicAccess = TopicUtil.getReflectionTopicAccessForTopicType(busTopicChangeRequest.getType());

        // Remove existing topic
        mqttAclService.delete(thingId, topicAccess,
                Set.of(reflection.getBus().getTopic(busTopicChangeRequest.getType(), busTopicChangeRequest.getKey())));

        // Add existing topic
        mqttAclService.add(thingId, topicAccess, Set.of(busTopicChangeRequest.getTopic()));
    }

    public void addCustomTopicAccess(String thingId, CustomBusTopicAccessRequest customBusTopicAccessRequest, String organizationId) {
        if (!TopicUtil.validateOrganization(customBusTopicAccessRequest.getTopic(), organizationId)) {
            throw new ClientException("Topic does not belong to the same tenant");
        }

        String busFieldKey = Bus.getFieldKey(customBusTopicAccessRequest.getAccess());

        Reflection reflection = mongoTemplate.findAndModify(Query.query(Criteria
                        .where("thingId").is(thingId)
                        .and("organizationId").is(organizationId)
                ),
                new Update()
                        .set("modifiedOn", new Date())
                        .addToSet(busFieldKey, customBusTopicAccessRequest.getTopic()),
                Reflection.class);

        if (null == reflection) {
            throw new NotFoundException("Reflection not found");
        }

        mqttAclService.add(reflection.getId(), customBusTopicAccessRequest.getAccess(), Set.of(customBusTopicAccessRequest.getTopic()));
    }

    public void deleteCustomTopicAccess(String thingId, CustomBusTopicAccessRequest customBusTopicAccessRequest, String organizationId) {
        String busFieldKey = Bus.getFieldKey(customBusTopicAccessRequest.getAccess());

        Reflection reflection = mongoTemplate.findAndModify(Query.query(Criteria
                        .where("thingId").is(thingId)
                        .and("organizationId").is(organizationId)
                ),
                new Update()
                        .set("modifiedOn", new Date())
                        .pull(busFieldKey, customBusTopicAccessRequest.getTopic()),
                Reflection.class);

        if (null == reflection) {
            throw new NotFoundException("Reflection not found");
        }

        mqttAclService.delete(reflection.getId(), customBusTopicAccessRequest.getAccess(), Set.of(customBusTopicAccessRequest.getTopic()));
    }

    public void initMqtt(Reflection reflection) {
        mqttUserService.add(reflection.getId(), reflection.getAccessKey());

        // Since it is a reflection - the publish and subscribe topics must be flipped

        mqttAclService.add(reflection.getId(),
                reflection.getBus().getAllTopicsWithSubscribeAccessFromThing(),
                reflection.getBus().getAllTopicsWithPublishAccessFromThing(),
                Collections.emptySet());
    }

    public AuthenticatedReflection getByAccessKey(String accessKey) throws Throwable {
        Reflection reflection = reflectionRepository.findByAccessKey(accessKey).orElseThrow((Supplier<Throwable>) InvalidTokenException::new);
        return new AuthenticatedReflection(reflection.getId(), reflection.getThingId(), reflection.getOrganizationId());
    }
}
