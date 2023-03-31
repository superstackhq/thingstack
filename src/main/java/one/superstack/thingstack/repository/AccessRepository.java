package one.superstack.thingstack.repository;

import one.superstack.thingstack.enums.ActorType;
import one.superstack.thingstack.enums.Permission;
import one.superstack.thingstack.enums.TargetType;
import one.superstack.thingstack.model.Access;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface AccessRepository extends MongoRepository<Access, String> {

    Optional<Access> findByTargetTypeAndTargetIdAndActorTypeAndActorIdAndOrganizationId(TargetType targetType, String targetId, ActorType actorType, String actorId, String organizationId);

    List<Access> findByTargetTypeAndTargetIdAndActorTypeAndOrganizationId(TargetType targetType, String targetId, ActorType actorType, String organizationId, Pageable pageable);

    Boolean existsByTargetTypeAndTargetIdAndActorTypeAndActorIdAndPermissionsIn(TargetType targetType, String targetId, ActorType actorType, String actorId, Set<Permission> permissions);

    Boolean existsByTargetTypeAndTargetIdAndActorTypeAndActorIdInAndPermissionsIn(TargetType targetType, String targetId, ActorType actorType, Set<String> actorIds, Set<Permission> permissions);
}
