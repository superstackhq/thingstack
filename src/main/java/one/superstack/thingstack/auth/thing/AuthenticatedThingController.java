package one.superstack.thingstack.auth.thing;

public class AuthenticatedThingController {

    public AuthenticatedThing getThing() {
        return ThreadLocalWrapper.getThing();
    }

    public String getThingId() {
        return getThing().getId();
    }

    public String getOrganizationId() {
        return getThing().getOrganizationId();
    }
}
