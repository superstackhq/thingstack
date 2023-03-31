package one.superstack.thingstack.auth.actor;

import one.superstack.thingstack.enums.ActorType;
import one.superstack.thingstack.enums.Permission;
import one.superstack.thingstack.enums.TargetType;
import one.superstack.thingstack.exception.NotAllowedException;
import one.superstack.thingstack.service.AccessService;

public class AuthenticatedController extends RequiresAuthentication {

    public AuthenticatedActor getActor() {
        return ThreadLocalWrapper.getActor();
    }

    public String getActorId() {
        return getActor().getId();
    }

    public ActorType getActorType() {
        return getActor().getType();
    }

    public String getOrganizationId() {
        return getActor().getOrganizationId();
    }

    public Boolean hasFullAccess() {
        return getActor().getHasFullAccess();
    }

    public void checkFullAccess() {
        if (!hasFullAccess()) {
            throw new NotAllowedException();
        }
    }

    public void checkAccess(AccessService accessService, TargetType targetType, String targetId, Permission permission) {
        if (!accessService.hasPermission(targetType, targetId, getActorType(), getActorId(), permission, hasFullAccess())) {
            throw new NotAllowedException();
        }
    }
}