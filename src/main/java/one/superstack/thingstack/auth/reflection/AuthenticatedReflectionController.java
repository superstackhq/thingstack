package one.superstack.thingstack.auth.reflection;

public class AuthenticatedReflectionController {

    public AuthenticatedReflection getReflection() {
        return ThreadLocalWrapper.getReflection();
    }

    public String getReflectionId() {
        return getReflection().getId();
    }

    public String getThingId() {
        return getReflection().getThingId();
    }

    public String getOrganizationId() {
        return getReflection().getOrganizationId();
    }
}
