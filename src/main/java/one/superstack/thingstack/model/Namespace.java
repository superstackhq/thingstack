package one.superstack.thingstack.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Document(collection = "namespaces")
public class Namespace implements Serializable {

    @Id
    private String id;

    private String thingTypeId;

    private List<String> parent;

    private String name;

    private String organizationId;

    private Date createdOn;

    private Date modifiedOn;

    public Namespace() {

    }

    public Namespace(String thingTypeId, List<String> parent, String name, String organizationId) {
        this.thingTypeId = thingTypeId;
        this.parent = parent;
        this.name = name;
        this.organizationId = organizationId;
        this.createdOn = new Date();
        this.modifiedOn = new Date();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThingTypeId() {
        return thingTypeId;
    }

    public void setThingTypeId(String thingTypeId) {
        this.thingTypeId = thingTypeId;
    }

    public List<String> getParent() {
        return parent;
    }

    public void setParent(List<String> parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }
}
