package one.superstack.thingstack.request;

import jakarta.validation.constraints.NotBlank;
import one.superstack.thingstack.embedded.Bus;

import java.io.Serializable;
import java.util.List;

public class ThingCreationRequest implements Serializable {

    @NotBlank
    private String typeId;

    private List<String> namespace;

    @NotBlank
    private String name;

    private String description;

    private Bus bus;

    public ThingCreationRequest() {

    }

    public ThingCreationRequest(String typeId, List<String> namespace, String name, String description, Bus bus) {
        this.typeId = typeId;
        this.namespace = namespace;
        this.name = name;
        this.description = description;
        this.bus = bus;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public List<String> getNamespace() {
        return namespace;
    }

    public void setNamespace(List<String> namespace) {
        this.namespace = namespace;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }
}
