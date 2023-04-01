package one.superstack.thingstack.util;

import one.superstack.thingstack.enums.ThingBusTopicType;
import one.superstack.thingstack.enums.TopicAccess;
import one.superstack.thingstack.exception.ClientException;
import one.superstack.thingstack.model.Thing;
import one.superstack.thingstack.model.ThingType;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators;

import java.util.List;
import java.util.Objects;

public class TopicUtil {

    public static final String TENANT_TOPIC_COMPONENT = "tenants";

    public static Boolean validateOrganization(String topic, String organizationId) {
        String[] components = topic.split("/");

        if (components.length < 2) {
            return false;
        }

        if (!TENANT_TOPIC_COMPONENT.equals(components[0])) {
            return false;
        }

        return Objects.equals(components[1], organizationId);
    }

    public static TopicAccess getThingTopicAccessForTopicType(ThingBusTopicType topicType) {
        switch (topicType) {
            case PROPERTY, ACTION_INVOCATION -> {
                return TopicAccess.SUBSCRIBE;
            }

            case PROPERTY_ACK, ACTION_RESULT, EVENT -> {
                return TopicAccess.PUBLISH;
            }

            default -> throw new ClientException("Invalid thing bus topic type");
        }
    }

    public static TopicAccess getReflectionTopicAccessForTopicType(ThingBusTopicType topicType) {
        switch (topicType) {
            case PROPERTY, ACTION_INVOCATION -> {
                return TopicAccess.PUBLISH;
            }

            case PROPERTY_ACK, ACTION_RESULT, EVENT -> {
                return TopicAccess.SUBSCRIBE;
            }

            default -> throw new ClientException("Invalid thing bus topic type");
        }
    }

    public static String getDefaultPropertyTopic(ThingType thingType, Thing thing, String propertyKey) {
        String namespaceString = getNamespaceString(thing.getNamespace());

        return String.format("%s/%s/things/%s/%s/%s%s/properties/%s",
                TENANT_TOPIC_COMPONENT,
                thing.getOrganizationId(),
                thingType.getName(),
                thingType.getVersion(),
                namespaceString,
                thing.getId(),
                propertyKey);
    }

    public static String getDefaultPropertyAckTopic(ThingType thingType, Thing thing, String propertyKey) {
        String namespaceString = getNamespaceString(thing.getNamespace());

        return String.format("%s/%s/things/%s/%s/%s%s/properties/%s/acks",
                TENANT_TOPIC_COMPONENT,
                thing.getOrganizationId(),
                thingType.getName(),
                thingType.getVersion(),
                namespaceString,
                thing.getId(),
                propertyKey);
    }

    public static String getDefaultActionInvocationTopic(ThingType thingType, Thing thing, String actionKey) {
        String namespaceString = getNamespaceString(thing.getNamespace());

        return String.format("%s/%s/things/%s/%s/%s%s/actions/%s/invocations",
                TENANT_TOPIC_COMPONENT,
                thing.getOrganizationId(),
                thingType.getName(),
                thingType.getVersion(),
                namespaceString,
                thing.getId(),
                actionKey);
    }

    public static String getDefaultActionResultTopic(ThingType thingType, Thing thing, String actionKey) {
        String namespaceString = getNamespaceString(thing.getNamespace());

        return String.format("%s/%s/things/%s/%s/%s%s/actions/%s/results",
                TENANT_TOPIC_COMPONENT,
                thing.getOrganizationId(),
                thingType.getName(),
                thingType.getVersion(),
                namespaceString,
                thing.getId(),
                actionKey);

    }

    public static String getDefaultEventTopic(ThingType thingType, Thing thing, String eventKey) {
        String namespaceString = getNamespaceString(thing.getNamespace());

        return String.format("%s/%s/things/%s/%s/%s%s/events/%s",
                TENANT_TOPIC_COMPONENT,
                thing.getOrganizationId(),
                thingType.getName(),
                thingType.getVersion(),
                namespaceString,
                thing.getId(),
                eventKey);
    }

    public static String getNamespaceString(List<String> namespace) {
        String namespaceString = "";

        if (null != namespace && !namespace.isEmpty()) {
            namespaceString = String.join("/", namespace);
        }

        return namespaceString;
    }
}
