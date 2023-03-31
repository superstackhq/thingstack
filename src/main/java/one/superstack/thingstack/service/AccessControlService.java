package one.superstack.thingstack.service;

import one.superstack.thingstack.auth.actor.AuthenticatedActor;
import one.superstack.thingstack.enums.ActorType;
import one.superstack.thingstack.enums.TargetType;
import one.superstack.thingstack.exception.NotFoundException;
import one.superstack.thingstack.model.Access;
import one.superstack.thingstack.pojo.Actor;
import one.superstack.thingstack.pojo.ActorReference;
import one.superstack.thingstack.request.AccessRequest;
import one.superstack.thingstack.request.DeleteAllAccessRequest;
import one.superstack.thingstack.response.AccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class AccessControlService {

    private final AccessService accessService;

    private final TargetService targetService;

    private final ActorService actorService;

    @Autowired
    public AccessControlService(AccessService accessService, TargetService targetService, ActorService actorService) {
        this.accessService = accessService;
        this.targetService = targetService;
        this.actorService = actorService;
    }

    public void add(AccessRequest accessRequest, AuthenticatedActor creator) {
        if (null != accessRequest.getTargetId() && accessRequest.getTargetId().isBlank()) {
            accessRequest.setTargetId(null);
        }

        if (null != accessRequest.getTargetId()) {
            if (!targetService.exists(accessRequest.getTargetType(), accessRequest.getTargetId(), creator.getOrganizationId())) {
                throw new NotFoundException("Target not found");
            }
        }

        if (!actorService.exists(accessRequest.getActorType(), accessRequest.getActorId(), creator.getOrganizationId())) {
            throw new NotFoundException("Actor not found");
        }

        accessService.add(accessRequest, creator);
    }

    public void delete(AccessRequest accessRequest, String organizationId) {
        if (null != accessRequest.getTargetId() && accessRequest.getTargetId().isBlank()) {
            accessRequest.setTargetId(null);
        }

        accessService.delete(accessRequest, organizationId);
    }

    public Access deleteAll(DeleteAllAccessRequest deleteAllAccessRequest, String organizationId) throws Throwable {
        if (null != deleteAllAccessRequest.getTargetId() && deleteAllAccessRequest.getTargetId().isBlank()) {
            deleteAllAccessRequest.setTargetId(null);
        }

        return accessService.deleteAll(deleteAllAccessRequest, organizationId);
    }

    public List<AccessResponse> list(TargetType targetType, String targetId, ActorType actorType, String organizationId, Pageable pageable) throws ExecutionException, InterruptedException {
        if (null != targetId && targetId.isBlank()) {
            targetId = null;
        }

        List<Access> accesses = accessService.list(targetType, targetId, actorType, organizationId, pageable);

        List<ActorReference> actorReferences = accesses.stream().map(access -> new ActorReference(access.getActorType(), access.getActorId())).collect(Collectors.toList());
        Map<ActorReference, Actor> actorMap = actorService.fetch(actorReferences).stream().collect(Collectors.toMap(actor -> new ActorReference(actor.getType(), actor.getReferenceId()), actor -> actor, (a, b) -> b));

        List<AccessResponse> accessActorResponses = new ArrayList<>();

        for (Access access : accesses) {
            accessActorResponses.add(new AccessResponse(access, actorMap.get(new ActorReference(access.getActorType(), access.getActorId()))));
        }

        return accessActorResponses;
    }

    public List<AccessResponse> get(TargetType targetType, String targetId, ActorType actorType, String actorId, String organizationId) throws Throwable {
        if (null != targetId && targetId.isBlank()) {
            targetId = null;
        }

        Access access = accessService.get(targetType, targetId, actorType, actorId, organizationId);
        Actor actor = actorService.get(new ActorReference(access.getActorType(), access.getActorId()));

        return List.of(new AccessResponse(access, actor));
    }
}
