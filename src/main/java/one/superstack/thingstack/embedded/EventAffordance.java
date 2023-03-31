package one.superstack.thingstack.embedded;

import java.io.Serializable;

public class EventAffordance implements Serializable {

    private String title;

    private String description;

    private DataSchema payload;

    public EventAffordance() {

    }

    public EventAffordance(String title, String description, DataSchema payload) {
        this.title = title;
        this.description = description;
        this.payload = payload;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DataSchema getPayload() {
        return payload;
    }

    public void setPayload(DataSchema payload) {
        this.payload = payload;
    }
}
