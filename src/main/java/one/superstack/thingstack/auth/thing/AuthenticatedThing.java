package one.superstack.thingstack.auth.thing;

import java.io.Serializable;

public class AuthenticatedThing implements Serializable {

    private String id;

    private String organizationId;

    public AuthenticatedThing() {

    }

    public AuthenticatedThing(String id, String organizationId) {
        this.id = id;
        this.organizationId = organizationId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }
}
