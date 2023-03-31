package one.superstack.thingstack.service;

import com.mongodb.client.result.UpdateResult;
import one.superstack.thingstack.auth.actor.AuthenticatedActor;
import one.superstack.thingstack.enums.ActorType;
import one.superstack.thingstack.enums.Permission;
import one.superstack.thingstack.enums.TargetType;
import one.superstack.thingstack.exception.ClientException;
import one.superstack.thingstack.exception.NotFoundException;
import one.superstack.thingstack.model.Access;
import one.superstack.thingstack.model.GroupMember;
import one.superstack.thingstack.repository.AccessRepository;
import one.superstack.thingstack.request.AccessRequest;
import one.superstack.thingstack.request.DeleteAllAccessRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class AccessService {
    private final AccessRepository accessRepository;

    private final GroupMemberService groupMemberService;

    private final MongoTemplate mongoTemplate;

    @Autowired
    public AccessService(AccessRepository accessRepository, GroupMemberService groupMemberService, MongoTemplate mongoTemplate) {
        this.accessRepository = accessRepository;
        this.groupMemberService = groupMemberService;
        this.mongoTemplate = mongoTemplate;
    }

    public void add(AccessRequest accessRequest, AuthenticatedActor creator) {
        mongoTemplate.upsert(Query.query(Criteria
                        .where("actorType").is(accessRequest.getActorType())
                        .and("actorId").is(accessRequest.getActorId())
                        .and("targetType").is(accessRequest.getTargetType())
                        .and("targetId").is(accessRequest.getTargetId())
                        .and("organizationId").is(creator.getOrganizationId())),
                new Update()
                        .setOnInsert("actorType", accessRequest.getActorType())
                        .setOnInsert("actorId", accessRequest.getActorId())
                        .setOnInsert("targetType", accessRequest.getTargetType())
                        .setOnInsert("targetId", accessRequest.getTargetId())
                        .setOnInsert("creatorId", creator.getId())
                        .setOnInsert("organizationId", creator.getOrganizationId())
                        .setOnInsert("createdOn", new Date())
                        .set("modifiedOn", new Date())
                        .addToSet("permissions").each(accessRequest.getPermissions().toArray()),
                Access.class);
    }

    public Access get(TargetType targetType, String targetId, ActorType actorType, String actorId, String organizationId) throws Throwable {
        return accessRepository.findByTargetTypeAndTargetIdAndActorTypeAndActorIdAndOrganizationId(targetType, targetId, actorType, actorId, organizationId)
                .orElseThrow((Supplier<Throwable>) () -> new NotFoundException("Access not found"));
    }

    public void delete(AccessRequest accessRequest, String organizationId) {
        UpdateResult updateResult = mongoTemplate.updateFirst(Query.query(Criteria
                        .where("actorType").is(accessRequest.getActorType())
                        .and("actorId").is(accessRequest.getActorId())
                        .and("targetType").is(accessRequest.getTargetType())
                        .and("targetId").is(accessRequest.getTargetId())
                        .and("organizationId").is(organizationId)),
                new Update()
                        .set("modifiedOn", new Date())
                        .pullAll("permissions", accessRequest.getPermissions().toArray()),
                Access.class);

        if (updateResult.getMatchedCount() == 0) {
            throw new NotFoundException("Access not found");
        }
    }

    public Access deleteAll(DeleteAllAccessRequest deleteAllAccessRequest, String organizationId) throws Throwable {
        Access access = get(deleteAllAccessRequest.getTargetType(), deleteAllAccessRequest.getTargetId(), deleteAllAccessRequest.getActorType(), deleteAllAccessRequest.getActorId(), organizationId);
        accessRepository.delete(access);
        return access;
    }

    public List<Access> list(TargetType targetType, String targetId, ActorType actorType, String organizationId, Pageable pageable) {
        return accessRepository.findByTargetTypeAndTargetIdAndActorTypeAndOrganizationId(targetType, targetId, actorType, organizationId, pageable);
    }

    @Async
    public void deleteAllForTarget(TargetType targetType, String targetId) {
        mongoTemplate.remove(Query.query(Criteria.where("targetType").is(targetType).and("targetId").is(targetId)), Access.class);
    }

    @Async
    public void deleteAllForActor(ActorType actorType, String actorId) {
        mongoTemplate.remove(Query.query(Criteria.where("actorType").is(actorType).and("actorId").is(actorId)), Access.class);
    }
    public Boolean hasPermission(TargetType targetType, String targetId, ActorType actorType, String actorId, Permission permission, Boolean hasFullAccess) {
        if (hasFullAccess) {
            return true;
        }

        switch (actorType) {
            case USER -> {
                return checkPermissionForUser(targetType, targetId, actorId, permission);
            }

            case API_KEY -> {
                return checkPermissionForApiKey(targetType, targetId, actorId, permission);
            }

            default -> throw new ClientException("Invalid actor type");
        }
    }
    private Boolean checkPermissionForUser(TargetType targetType, String targetId, String userId, Permission permission) {
        Set<Permission> permissions = Set.of(Permission.ALL, permission);

        if (accessRepository.existsByTargetTypeAndTargetIdAndActorTypeAndActorIdAndPermissionsIn(targetType, targetId, ActorType.USER, userId, permissions)) {
            return true;
        }

        Set<String> groupIds = groupMemberService.listGroups(userId, Pageable.unpaged()).stream().map(GroupMember::getGroupId).collect(Collectors.toSet());
        if (groupIds.isEmpty()) {
            return false;
        }

        return accessRepository.existsByTargetTypeAndTargetIdAndActorTypeAndActorIdInAndPermissionsIn(targetType, targetId, ActorType.GROUP, groupIds, permissions);
    }

    private Boolean checkPermissionForApiKey(TargetType targetType, String targetId, String apiKeyId, Permission permission) {
        Set<Permission> permissions = Set.of(Permission.ALL, permission);
        return accessRepository.existsByTargetTypeAndTargetIdAndActorTypeAndActorIdAndPermissionsIn(targetType, targetId, ActorType.API_KEY, apiKeyId, permissions);
    }
}
