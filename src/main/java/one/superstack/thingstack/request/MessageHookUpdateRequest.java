package one.superstack.thingstack.request;

import java.io.Serializable;

public class MessageHookUpdateRequest implements Serializable {

    private String name;

    private String description;

    private String topic;

    public MessageHookUpdateRequest() {

    }

    public MessageHookUpdateRequest(String name, String description, String topic) {
        this.name = name;
        this.description = description;
        this.topic = topic;
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

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
