package one.superstack.thingstack.request;

import one.superstack.thingstack.embedded.Bus;

import java.io.Serializable;
import java.util.List;

public class ThingUpdateRequest implements Serializable {

    private String name;

    private String description;

    public ThingUpdateRequest() {

    }

    public ThingUpdateRequest(String name, String description) {
        this.name = name;
        this.description = description;
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
}
