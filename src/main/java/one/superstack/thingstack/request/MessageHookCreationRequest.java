package one.superstack.thingstack.request;

import jakarta.validation.constraints.NotBlank;
import one.superstack.thingstack.embedded.Broker;

import java.io.Serializable;

public class MessageHookCreationRequest implements Serializable {

    @NotBlank
    private String name;

    private String description;

    private Broker broker;

    @NotBlank
    private String topic;

    public MessageHookCreationRequest() {

    }

    public MessageHookCreationRequest(String name, String description, Broker broker, String topic) {
        this.name = name;
        this.description = description;
        this.broker = broker;
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

    public Broker getBroker() {
        return broker;
    }

    public void setBroker(Broker broker) {
        this.broker = broker;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
