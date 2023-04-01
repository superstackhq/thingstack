package one.superstack.thingstack.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import one.superstack.thingstack.enums.TopicAccess;

import java.io.Serializable;

public class ThingBusCustomTopicRequest implements Serializable {

    @NotBlank
    private String topic;

    @NotNull
    private TopicAccess topicAccess;

    public ThingBusCustomTopicRequest() {

    }

    public ThingBusCustomTopicRequest(String topic, TopicAccess topicAccess) {
        this.topic = topic;
        this.topicAccess = topicAccess;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public TopicAccess getTopicAccess() {
        return topicAccess;
    }

    public void setTopicAccess(TopicAccess topicAccess) {
        this.topicAccess = topicAccess;
    }
}
