package one.superstack.thingstack.service;

import com.mongodb.client.result.UpdateResult;
import one.superstack.thingstack.auth.actor.AuthenticatedActor;
import one.superstack.thingstack.auth.thing.AuthenticatedThing;
import one.superstack.thingstack.embedded.Bus;
import one.superstack.thingstack.enums.Permission;
import one.superstack.thingstack.enums.TargetType;
import one.superstack.thingstack.enums.TopicAccess;
import one.superstack.thingstack.exception.ClientException;
import one.superstack.thingstack.exception.InvalidTokenException;
import one.superstack.thingstack.exception.NotFoundException;
import one.superstack.thingstack.model.Thing;
import one.superstack.thingstack.model.ThingType;
import one.superstack.thingstack.repository.ThingRepository;
import one.superstack.thingstack.request.*;
import one.superstack.thingstack.response.AccessKeyResponse;
import one.superstack.thingstack.util.Random;
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
public class ThingService {

    private final ThingRepository thingRepository;

    private final ThingTypeService thingTypeService;

    private final MqttUserService mqttUserService;

    private final MqttAclService mqttAclService;


    private final ReflectionService reflectionService;

    private final AccessService accessService;

    private final NamespaceService namespaceService;

    private final MongoTemplate mongoTemplate;

    @Autowired
    public ThingService(ThingRepository thingRepository, ThingTypeService thingTypeService, MqttUserService mqttUserService, MqttAclService mqttAclService, ReflectionService reflectionService, AccessService accessService, NamespaceService namespaceService, MongoTemplate mongoTemplate) {
        this.thingRepository = thingRepository;
        this.thingTypeService = thingTypeService;
        this.mqttUserService = mqttUserService;
        this.mqttAclService = mqttAclService;
        this.reflectionService = reflectionService;
        this.accessService = accessService;
        this.namespaceService = namespaceService;
        this.mongoTemplate = mongoTemplate;
    }

    public Thing create(ThingCreationRequest thingCreationRequest, AuthenticatedActor creator) throws Throwable {
        ThingType thingType = thingTypeService.get(thingCreationRequest.getTypeId(), creator.getOrganizationId());

        if (null != thingCreationRequest.getNamespace() && thingCreationRequest.getNamespace().isEmpty()) {
            thingCreationRequest.setNamespace(null);
        }

        String accessKey = Random.generateRandomString(128);

        Thing thing = new Thing(thingCreationRequest.getTypeId(),
                thingCreationRequest.getNamespace(),
                thingCreationRequest.getName(),
                thingCreationRequest.getDescription(),
                accessKey,
                null,
                creator.getOrganizationId(),
                creator.getType(),
                creator.getId());

        thing = thingRepository.save(thing);


        // Validate the bus and set it
        Bus validatedBus = thingCreationRequest.getBus().validateAgainstThingType(thingType, thing);
        thing.setBus(validatedBus);
        thing = thingRepository.save(thing);

        // Initialize mqtt layer for the thing
        initMqtt(thing);

        // Grant full access to the creator
        accessService.add(new AccessRequest(TargetType.THING, thing.getId(), creator.getType(), creator.getId(), Set.of(Permission.ALL)), creator);

        if (null != thing.getNamespace()) {
            namespaceService.register(thing.getNamespace(), thing.getTypeId(), thing.getOrganizationId());
        }

        // create the reflection
        reflectionService.init(thing);

        return thing;
    }

    public List<Thing> list(String thingTypeId, String organizationId, Pageable pageable) {
        return thingRepository.findByTypeIdAndOrganizationId(thingTypeId, organizationId, pageable);
    }

    public Thing get(String thingId, String organizationId) throws Throwable {
        return thingRepository.findByIdAndOrganizationId(thingId, organizationId)
                .orElseThrow((Supplier<Throwable>) () -> new NotFoundException("Thing not found"));
    }

    public Thing get(String thingId) throws Throwable {
        return thingRepository.findById(thingId)
                .orElseThrow((Supplier<Throwable>) () -> new NotFoundException("Thing not found"));
    }

    public Thing update(String thingId, ThingUpdateRequest thingUpdateRequest, String organizationId) throws Throwable {
        Thing thing = get(thingId, organizationId);

        if (null != thingUpdateRequest.getName() && !thingUpdateRequest.getName().isEmpty()) {
            thing.setName(thingUpdateRequest.getName());
        }

        thing.setDescription(thingUpdateRequest.getDescription());

        thing.setModifiedOn(new Date());
        return thingRepository.save(thing);
    }

    public Thing delete(String thingId, String organizationId) throws Throwable {
        Thing thing = get(thingId, organizationId);
        thingRepository.delete(thing);

        // Clean up mqtt stuff
        mqttUserService.delete(thing.getId());
        mqttAclService.deleteAll(thing.getId());

        // Clean up access stuff
        accessService.deleteAllForTarget(TargetType.THING, thing.getId());

        // Clean up reflection
        reflectionService.delete(thingId);

        return thing;
    }

    public AccessKeyResponse getAccessKey(String thingId, String organizationId) throws Throwable {
        return new AccessKeyResponse(get(thingId, organizationId).getAccessKey());
    }

    public AccessKeyResponse resetAccessKey(String thingId, String organizationId) throws Throwable {
        Thing thing = get(thingId, organizationId);

        thing.setAccessKey(Random.generateRandomString(128));

        thing.setModifiedOn(new Date());
        thing = thingRepository.save(thing);

        mqttUserService.changePassword(thing.getId(), thing.getAccessKey());

        return new AccessKeyResponse(thing.getAccessKey());
    }

    public void changeAffordanceTopic(String thingId, BusTopicChangeRequest busTopicChangeRequest, String organizationId) {
        if (!TopicUtil.validateOrganization(busTopicChangeRequest.getTopic(), organizationId)) {
            throw new ClientException("Topic does not belong to the same tenant");
        }

        String busFieldKey = Bus.getFieldKey(busTopicChangeRequest.getType(), busTopicChangeRequest.getKey());

        Thing thing = mongoTemplate.findAndModify(Query.query(Criteria
                        .where("id").is(new ObjectId(thingId))
                        .and("organizationId").is(organizationId)
                        .and(busFieldKey).exists(true)
                ),
                new Update()
                        .set("modifiedOn", new Date())
                        .set(busFieldKey, busTopicChangeRequest.getTopic()),
                Thing.class);

        if (null == thing) {
            throw new NotFoundException("Thing bus topic not found");
        }

        TopicAccess topicAccess = TopicUtil.getThingTopicAccessForTopicType(busTopicChangeRequest.getType());

        // Remove existing topic
        mqttAclService.delete(thingId, topicAccess,
                Set.of(thing.getBus().getTopic(busTopicChangeRequest.getType(), busTopicChangeRequest.getKey())));

        // Add existing topic
        mqttAclService.add(thingId, topicAccess, Set.of(busTopicChangeRequest.getTopic()));

        // Change the reflection topics too
        reflectionService.changeAffordanceTopic(thingId, busTopicChangeRequest);
    }

    public void addCustomTopicAccess(String thingId, CustomBusTopicAccessRequest customBusTopicAccessRequest, String organizationId) {
        if (!TopicUtil.validateOrganization(customBusTopicAccessRequest.getTopic(), organizationId)) {
            throw new ClientException("Topic does not belong to the same tenant");
        }

        String busFieldKey = Bus.getFieldKey(customBusTopicAccessRequest.getAccess());

        UpdateResult updateResult = mongoTemplate.updateFirst(Query.query(Criteria
                        .where("id").is(new ObjectId(thingId))
                        .and("organizationId").is(organizationId)
                ),
                new Update()
                        .set("modifiedOn", new Date())
                        .addToSet(busFieldKey, customBusTopicAccessRequest.getTopic()),
                Thing.class);

        if (updateResult.getMatchedCount() == 0) {
            throw new NotFoundException("Thing not found");
        }

        mqttAclService.add(thingId, customBusTopicAccessRequest.getAccess(), Set.of(customBusTopicAccessRequest.getTopic()));
    }

    public void deleteCustomTopicAccess(String thingId, CustomBusTopicAccessRequest customBusTopicAccessRequest, String organizationId) {
        String busFieldKey = Bus.getFieldKey(customBusTopicAccessRequest.getAccess());

        UpdateResult updateResult = mongoTemplate.updateFirst(Query.query(Criteria
                        .where("id").is(new ObjectId(thingId))
                        .and("organizationId").is(organizationId)
                ),
                new Update()
                        .set("modifiedOn", new Date())
                        .pull(busFieldKey, customBusTopicAccessRequest.getTopic()),
                Thing.class);

        if (updateResult.getMatchedCount() == 0) {
            throw new NotFoundException("Thing not found");
        }

        mqttAclService.delete(thingId, customBusTopicAccessRequest.getAccess(), Set.of(customBusTopicAccessRequest.getTopic()));
    }

    public Boolean exists(String thingId, String organizationId) {
        return thingRepository.existsByIdAndOrganizationId(thingId, organizationId);
    }

    public AuthenticatedThing getByAccessKey(String accessKey) throws Throwable {
        Thing thing = thingRepository.findByAccessKey(accessKey).orElseThrow((Supplier<Throwable>) InvalidTokenException::new);
        return new AuthenticatedThing(thing.getId(), thing.getOrganizationId());
    }

    private void initMqtt(Thing thing) {
        mqttUserService.add(thing.getId(), thing.getAccessKey());

        mqttAclService.add(thing.getId(),
                thing.getBus().getAllTopicsWithPublishAccessFromThing(),
                thing.getBus().getAllTopicsWithSubscribeAccessFromThing(),
                thing.getBus().getAllTopicsWithPubSubAccessFromThing());
    }
}
