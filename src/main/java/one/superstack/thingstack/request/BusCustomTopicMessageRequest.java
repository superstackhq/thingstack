package one.superstack.thingstack.request;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public class BusCustomTopicMessageRequest implements Serializable {

    @NotBlank
    private String topic;

    private Object message;

    public BusCustomTopicMessageRequest() {

    }

    public BusCustomTopicMessageRequest(String topic, Object message) {
        this.topic = topic;
        this.message = message;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }
}
