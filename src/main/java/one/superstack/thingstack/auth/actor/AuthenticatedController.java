package one.superstack.thingstack.auth.actor;

import one.superstack.thingstack.enums.ActorType;
import one.superstack.thingstack.exception.NotAllowedException;

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
}