package one.superstack.thingstack.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import one.superstack.thingstack.embedded.Bus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Document(collection = "reflections")
public class Reflection implements Serializable {

    @Id
    private String id;

    private String thingId;

    @JsonIgnore
    private String accessKey;

    private Bus bus;

    private String organizationId;

    private Date createdOn;

    private Date modifiedOn;

    public Reflection() {

    }

    public Reflection(String thingId, String accessKey, Bus bus, String organizationId) {
        this.thingId = thingId;
        this.accessKey = accessKey;
        this.bus = bus;
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

    public String getThingId() {
        return thingId;
    }

    public void setThingId(String thingId) {
        this.thingId = thingId;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
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
