package one.superstack.thingstack.request;

import one.superstack.thingstack.embedded.Bus;

import java.io.Serializable;
import java.util.List;

public class ThingUpdateRequest implements Serializable {

    private String description;

    public ThingUpdateRequest() {

    }

    public ThingUpdateRequest(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
