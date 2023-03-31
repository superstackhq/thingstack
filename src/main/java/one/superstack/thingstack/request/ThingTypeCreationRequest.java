package one.superstack.thingstack.request;

import jakarta.validation.constraints.NotBlank;
import one.superstack.thingstack.embedded.ActionAffordance;
import one.superstack.thingstack.embedded.EventAffordance;
import one.superstack.thingstack.embedded.PropertyAffordance;

import java.io.Serializable;
import java.util.Map;

public class ThingTypeCreationRequest implements Serializable {

    @NotBlank
    private String name;

    @NotBlank
    private String version;

    private String description;

    private Map<String, PropertyAffordance> properties;

    private Map<String, ActionAffordance> actions;

    private Map<String, EventAffordance> events;

    public ThingTypeCreationRequest() {

    }

    public ThingTypeCreationRequest(String name, String version, String description, Map<String, PropertyAffordance> properties, Map<String, ActionAffordance> actions, Map<String, EventAffordance> events) {
        this.name = name;
        this.version = version;
        this.description = description;
        this.properties = properties;
        this.actions = actions;
        this.events = events;
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
}
