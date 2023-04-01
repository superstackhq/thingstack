package one.superstack.thingstack.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import one.superstack.thingstack.enums.ThingBusTopicType;

import java.io.Serializable;

public class ThingBusTopicChangeRequest implements Serializable {

    @NotNull
    private ThingBusTopicType type;

    @NotBlank
    private String key;

    @NotBlank
    private String topic;

    public ThingBusTopicChangeRequest() {

    }

    public ThingBusTopicChangeRequest(ThingBusTopicType type, String key, String topic) {
        this.type = type;
        this.key = key;
        this.topic = topic;
    }

    public ThingBusTopicType getType() {
        return type;
    }

    public void setType(ThingBusTopicType type) {
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
