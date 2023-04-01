package one.superstack.thingstack.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import one.superstack.thingstack.enums.ThingBusTopicType;

import java.io.Serializable;

public class ThingBusTopicChangeRequest implements Serializable {

    @NotNull
    private ThingBusTopicType topicType;

    @NotBlank
    private String key;

    @NotBlank
    private String topic;

    public ThingBusTopicChangeRequest() {

    }

    public ThingBusTopicChangeRequest(ThingBusTopicType topicType, String key, String topic) {
        this.topicType = topicType;
        this.key = key;
        this.topic = topic;
    }

    public ThingBusTopicType getTopicType() {
        return topicType;
    }

    public void setTopicType(ThingBusTopicType topicType) {
        this.topicType = topicType;
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
