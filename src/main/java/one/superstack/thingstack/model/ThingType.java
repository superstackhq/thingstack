package one.superstack.thingstack.model;

import one.superstack.thingstack.embedded.ActionAffordance;
import one.superstack.thingstack.embedded.EventAffordance;
import one.superstack.thingstack.embedded.PropertyAffordance;
import one.superstack.thingstack.enums.ActorType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

@Document(collection = "thing_types")
public class ThingType implements Serializable {

    @Id
    private String id;

    private String name;

    private String version;

    private String description;

    private Map<String, PropertyAffordance> properties;

    private Map<String, ActionAffordance> actions;

    private Map<String, EventAffordance> events;

    private ActorType creatorType;

    private String creatorId;

    private String organizationId;

    private Date createdOn;

    private Date modifiedOn;

    public ThingType() {

    }

    public ThingType(String name, String version, String description, Map<String, PropertyAffordance> properties, Map<String, ActionAffordance> actions, Map<String, EventAffordance> events, ActorType creatorType, String creatorId, String organizationId) {
        this.name = name;
        this.version = version;
        this.description = description;
        this.properties = properties;
        this.actions = actions;
        this.events = events;
        this.creatorType = creatorType;
        this.creatorId = creatorId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, PropertyAffordance> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, PropertyAffordance> properties) {
        this.properties = properties;
    }

    public Map<String, ActionAffordance> getActions() {
        return actions;
    }

    public void setActions(Map<String, ActionAffordance> actions) {
        this.actions = actions;
    }

    public Map<String, EventAffordance> getEvents() {
        return events;
    }

    public void setEvents(Map<String, EventAffordance> events) {
        this.events = events;
    }

    public ActorType getCreatorType() {
        return creatorType;
    }

    public void setCreatorType(ActorType creatorType) {
        this.creatorType = creatorType;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
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
