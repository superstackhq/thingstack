package one.superstack.thingstack.embedded;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Bus implements Serializable {

    private Map<String, String> propertyTopics;

    private Map<String, String> propertyAckTopics;

    private Map<String, String> actionInvocationTopics;

    private Map<String, String> actionResultTopics;

    private Map<String, String> eventTopics;

    private Set<String> customPublishTopics;

    private Set<String> customSubscribeTopics;

    private Set<String> customPubSubTopics;

    public Bus() {

    }

    public Bus(Map<String, String> propertyTopics, Map<String, String> propertyAckTopics, Map<String, String> actionInvocationTopics, Map<String, String> actionResultTopics, Map<String, String> eventTopics, Set<String> customPublishTopics, Set<String> customSubscribeTopics, Set<String> customPubSubTopics) {
        this.propertyTopics = propertyTopics;
        this.propertyAckTopics = propertyAckTopics;
        this.actionInvocationTopics = actionInvocationTopics;
        this.actionResultTopics = actionResultTopics;
        this.eventTopics = eventTopics;
        this.customPublishTopics = customPublishTopics;
        this.customSubscribeTopics = customSubscribeTopics;
        this.customPubSubTopics = customPubSubTopics;
    }

    public Map<String, String> getPropertyTopics() {
        return propertyTopics;
    }

    public void setPropertyTopics(Map<String, String> propertyTopics) {
        this.propertyTopics = propertyTopics;
    }

    public Map<String, String> getPropertyAckTopics() {
        return propertyAckTopics;
    }

    public void setPropertyAckTopics(Map<String, String> propertyAckTopics) {
        this.propertyAckTopics = propertyAckTopics;
    }

    public Map<String, String> getActionInvocationTopics() {
        return actionInvocationTopics;
    }

    public void setActionInvocationTopics(Map<String, String> actionInvocationTopics) {
        this.actionInvocationTopics = actionInvocationTopics;
    }

    public Map<String, String> getActionResultTopics() {
        return actionResultTopics;
    }

    public void setActionResultTopics(Map<String, String> actionResultTopics) {
        this.actionResultTopics = actionResultTopics;
    }

    public Map<String, String> getEventTopics() {
        return eventTopics;
    }

    public void setEventTopics(Map<String, String> eventTopics) {
        this.eventTopics = eventTopics;
    }

    public Set<String> getCustomPublishTopics() {
        return customPublishTopics;
    }

    public void setCustomPublishTopics(Set<String> customPublishTopics) {
        this.customPublishTopics = customPublishTopics;
    }

    public Set<String> getCustomSubscribeTopics() {
        return customSubscribeTopics;
    }

    public void setCustomSubscribeTopics(Set<String> customSubscribeTopics) {
        this.customSubscribeTopics = customSubscribeTopics;
    }

    public Set<String> getCustomPubSubTopics() {
        return customPubSubTopics;
    }

    public void setCustomPubSubTopics(Set<String> customPubSubTopics) {
        this.customPubSubTopics = customPubSubTopics;
    }
}
