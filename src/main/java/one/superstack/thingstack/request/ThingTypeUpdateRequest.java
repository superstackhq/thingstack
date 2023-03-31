package one.superstack.thingstack.request;

import java.io.Serializable;

public class ThingTypeUpdateRequest implements Serializable {

    private String description;

    public ThingTypeUpdateRequest() {

    }

    public ThingTypeUpdateRequest(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
