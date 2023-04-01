package one.superstack.thingstack.embedded;

import one.superstack.thingstack.enums.ThingBusTopicType;
import one.superstack.thingstack.enums.TopicAccess;
import one.superstack.thingstack.exception.ClientException;
import one.superstack.thingstack.model.Thing;
import one.superstack.thingstack.model.ThingType;
import one.superstack.thingstack.util.TopicUtil;

import java.io.Serializable;
import java.util.*;

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

    public Bus validateAgainstThingType(ThingType thingType, Thing thing) {
        Bus validatedBus = new Bus();

        // Check if custom publish topics belong to the organization
        if (null != customPublishTopics && !customPublishTopics.isEmpty()) {
            for (String customPublishTopic : customPublishTopics) {
                if (!TopicUtil.validateOrganization(customPublishTopic, thing.getOrganizationId())) {
                    throw new ClientException("Custom publish topic " + customPublishTopic + " does not belong to this organization");
                }
            }

            validatedBus.setCustomPublishTopics(customPublishTopics);
        } else {
            validatedBus.setCustomPubSubTopics(Collections.emptySet());
        }

        // Check if custom subscribe topics belong to the organization
        if (null != customSubscribeTopics && !customSubscribeTopics.isEmpty()) {
            for (String customSubscribeTopic : customSubscribeTopics) {
                if (!TopicUtil.validateOrganization(customSubscribeTopic, thing.getOrganizationId())) {
                    throw new ClientException("Custom subscribe topic " + customSubscribeTopic + " does not belong to this organization");
                }
            }

            validatedBus.setCustomSubscribeTopics(customSubscribeTopics);
        } else {
            validatedBus.setCustomSubscribeTopics(Collections.emptySet());
        }

        // Check if custom pubsub topics belong to the organization
        if (null != customPubSubTopics && !customPubSubTopics.isEmpty()) {
            for (String customPubSubTopic : customPubSubTopics) {
                if (!TopicUtil.validateOrganization(customPubSubTopic, thing.getOrganizationId())) {
                    throw new ClientException("Custom pubsub topic " + customPubSubTopic + " does not belong to this organization");
                }
            }

            validatedBus.setCustomPubSubTopics(customPubSubTopics);
        } else {
            validatedBus.setCustomPubSubTopics(Collections.emptySet());
        }

        // validate the property topics and set the empty ones with defaults
        if (null != thingType.getProperties() && !thingType.getProperties().isEmpty()) {
            Map<String, String> validatedPropertyTopics = new HashMap<>();
            Map<String, String> validatedPropertyAckTopics = new HashMap<>();

            for (Map.Entry<String, PropertyAffordance> property : thingType.getProperties().entrySet()) {
                String propertyTopic = propertyTopics.getOrDefault(property.getKey(), null);

                if (null == propertyTopic) {
                    propertyTopic = TopicUtil.getDefaultPropertyTopic(thingType, thing, property.getKey());
                } else {
                    if (!TopicUtil.validateOrganization(propertyTopic, thing.getOrganizationId())) {
                        throw new ClientException("Property topic " + propertyTopic + " does not belong to this organization");
                    }
                }

                String propertyAckTopic = propertyAckTopics.getOrDefault(property.getKey(), null);

                if (null == propertyAckTopic) {
                    propertyAckTopic = TopicUtil.getDefaultPropertyAckTopic(thingType, thing, property.getKey());
                } else {
                    if (!TopicUtil.validateOrganization(propertyAckTopic, thing.getOrganizationId())) {
                        throw new ClientException("Property ack topic " + propertyAckTopic + " does not belong to this organization");
                    }
                }

                validatedPropertyTopics.put(property.getKey(), propertyTopic);
                validatedPropertyAckTopics.put(property.getKey(), propertyAckTopic);
            }

            validatedBus.setPropertyTopics(validatedPropertyTopics);
            validatedBus.setPropertyAckTopics(validatedPropertyAckTopics);
        } else {
            validatedBus.setPropertyTopics(Collections.emptyMap());
            validatedBus.setPropertyAckTopics(Collections.emptyMap());
        }

        if (null != thingType.getActions() && !thingType.getActions().isEmpty()) {
            Map<String, String> validatedActionInvocationTopics = new HashMap<>();
            Map<String, String> validatedActionResultTopics = new HashMap<>();

            for (Map.Entry<String, ActionAffordance> action : thingType.getActions().entrySet()) {
                String actionInvocationTopic = actionInvocationTopics.getOrDefault(action.getKey(), null);

                if (null == actionInvocationTopic) {
                    actionInvocationTopic = TopicUtil.getDefaultActionInvocationTopic(thingType, thing, action.getKey());
                } else {
                    if (!TopicUtil.validateOrganization(actionInvocationTopic, thing.getOrganizationId())) {
                        throw new ClientException("Action invocation topic " + actionInvocationTopic + " does not belong to this organization");
                    }
                }

                String actionResultTopic = actionResultTopics.getOrDefault(action.getKey(), null);

                if (null == actionResultTopic) {
                    actionResultTopic = TopicUtil.getDefaultActionResultTopic(thingType, thing, action.getKey());
                } else {
                    if (!TopicUtil.validateOrganization(actionResultTopic, thing.getOrganizationId())) {
                        throw new ClientException("Action result topic " + actionResultTopic + " does not belong to this organization");
                    }
                }

                validatedActionInvocationTopics.put(action.getKey(), actionInvocationTopic);
                validatedActionResultTopics.put(action.getKey(), actionResultTopic);
            }

            validatedBus.setActionInvocationTopics(validatedActionInvocationTopics);
            validatedBus.setActionResultTopics(validatedActionResultTopics);
        } else {
            validatedBus.setActionInvocationTopics(Collections.emptyMap());
            validatedBus.setActionResultTopics(Collections.emptyMap());
        }

        if (null != thingType.getEvents() && !thingType.getEvents().isEmpty()) {
            Map<String, String> validatedEventTopics = new HashMap<>();

            for (Map.Entry<String, EventAffordance> event : thingType.getEvents().entrySet()) {
                String eventTopic = eventTopics.getOrDefault(event.getKey(), null);

                if (null == eventTopic) {
                    eventTopic = TopicUtil.getDefaultEventTopic(thingType, thing, event.getKey());
                } else {
                    if (!TopicUtil.validateOrganization(eventTopic, thing.getOrganizationId())) {
                        throw new ClientException("Event topic " + eventTopic + " does not belong to this organization");
                    }
                }

                validatedEventTopics.put(event.getKey(), eventTopic);
            }

            validatedBus.setEventTopics(validatedEventTopics);
        } else {
            validatedBus.setEventTopics(Collections.emptyMap());
        }

        return validatedBus;
    }

    public String getTopic(ThingBusTopicType topicType, String key) {
        switch (topicType) {

            case PROPERTY -> {
                return propertyTopics.get(key);
            }

            case PROPERTY_ACK -> {
                return propertyAckTopics.get(key);
            }

            case ACTION_INVOCATION -> {
                return actionInvocationTopics.get(key);
            }

            case ACTION_RESULT -> {
                return actionResultTopics.get(key);
            }

            case EVENT -> {
                return eventTopics.get(key);
            }

            default -> throw new ClientException("Invalid topic type");
        }
    }

    public static String getFieldKey(TopicAccess topicAccess) {
        switch (topicAccess) {

            case PUBLISH -> {
                return "bus.publishTopics";
            }

            case SUBSCRIBE -> {
                return "bus.subscribeTopics";
            }

            case PUBSUB -> {
                return "bus.customPubSubTopics";
            }

            default -> throw new ClientException("Invalid topic access type");
        }
    }

    public static String getFieldKey(ThingBusTopicType topicType, String affordanceKey) {
        String prefix;

        switch (topicType) {

            case PROPERTY -> {
                prefix = "bus.propertyTopics";
            }

            case PROPERTY_ACK -> {
                prefix = "bus.propertyAckTopics";
            }

            case ACTION_INVOCATION -> {
                prefix = "bus.actionInvocationTopics";
            }

            case ACTION_RESULT -> {
                prefix = "bus.actionResultTopics";
            }

            case EVENT -> {
                prefix = "bus.eventTopics";
            }

            default -> throw new ClientException("Invalid thing topic type");
        }

        return String.format("%s.%s", prefix, affordanceKey);
    }

    public Set<String> getAllTopicsWithPublishAccess() {
        Set<String> topics = new HashSet<>();

        if (null != customPublishTopics) {
            topics.addAll(customPublishTopics);
        }

        if (null != propertyAckTopics) {
            topics.addAll(propertyAckTopics.values());
        }

        if (null != eventTopics) {
            topics.addAll(eventTopics.values());
        }

        if (null != actionResultTopics) {
            topics.addAll(actionResultTopics.values());
        }

        return topics;
    }

    public Set<String> getAllTopicsWithSubscribeAccess() {
        Set<String> topics = new HashSet<>();

        if (null != customSubscribeTopics) {
            topics.addAll(customSubscribeTopics);
        }

        if (null != propertyTopics) {
            topics.addAll(propertyTopics.values());
        }

        if (null != actionInvocationTopics) {
            topics.addAll(actionInvocationTopics.values());
        }

        return topics;
    }

    public Set<String> getAllTopicsWithPubSubAccess() {
        return customPubSubTopics;
    }
}
