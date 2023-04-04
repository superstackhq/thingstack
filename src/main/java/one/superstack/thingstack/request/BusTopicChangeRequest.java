package one.superstack.thingstack.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import one.superstack.thingstack.enums.TopicType;

import java.io.Serializable;

public class BusTopicChangeRequest implements Serializable {

    @NotNull
    private TopicType type;

    @NotBlank
    private String key;

    @NotBlank
    private String topic;

    public BusTopicChangeRequest() {

    }

    public BusTopicChangeRequest(TopicType type, String key, String topic) {
        this.type = type;
        this.key = key;
        this.topic = topic;
    }

    public TopicType getType() {
        return type;
    }

    public void setType(TopicType type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
