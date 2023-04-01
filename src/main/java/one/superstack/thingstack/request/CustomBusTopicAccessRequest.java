package one.superstack.thingstack.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import one.superstack.thingstack.enums.TopicAccess;

import java.io.Serializable;

public class CustomBusTopicAccessRequest implements Serializable {

    @NotBlank
    private String topic;

    @NotNull
    private TopicAccess access;

    public CustomBusTopicAccessRequest() {

    }

    public CustomBusTopicAccessRequest(String topic, TopicAccess access) {
        this.topic = topic;
        this.access = access;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public TopicAccess getAccess() {
        return access;
    }

    public void setAccess(TopicAccess access) {
        this.access = access;
    }
}
