package one.superstack.thingstack.auth.reflection;

import java.io.Serializable;

public class AuthenticatedReflection implements Serializable {

    private String id;

    private String thingId;

    private String organizationId;

    public AuthenticatedReflection() {

    }

    public AuthenticatedReflection(String id, String thingId, String organizationId) {
        this.id = id;
        this.thingId = thingId;
        this.organizationId = organizationId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThingId() {
        return thingId;
    }

    public void setThingId(String thingId) {
        this.thingId = thingId;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }
}
