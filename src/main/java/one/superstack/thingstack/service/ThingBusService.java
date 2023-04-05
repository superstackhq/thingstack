package one.superstack.thingstack.service;

import one.superstack.thingstack.embedded.ActionAffordance;
import one.superstack.thingstack.embedded.Bus;
import one.superstack.thingstack.embedded.EventAffordance;
import one.superstack.thingstack.embedded.PropertyAffordance;
import one.superstack.thingstack.exception.ClientException;
import one.superstack.thingstack.model.Thing;
import one.superstack.thingstack.model.ThingType;
import one.superstack.thingstack.publisher.MqttPublisherGateway;
import one.superstack.thingstack.request.*;
import one.superstack.thingstack.response.*;
import one.superstack.thingstack.util.DataTypeValidator;
import one.superstack.thingstack.util.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ThingBusService {

    private final ThingService thingService;

    private final ThingTypeService thingTypeService;

    private final MqttPublisherGateway mqttPublisherGateway;

    @Autowired
    public ThingBusService(ThingService thingService, ThingTypeService thingTypeService, MqttPublisherGateway mqttPublisherGateway) {
        this.thingService = thingService;
        this.thingTypeService = thingTypeService;
        this.mqttPublisherGateway = mqttPublisherGateway;
    }

    public ThingBusSetPropertyResponse setProperty(ThingBusSetPropertyRequest thingBusSetPropertyRequest, String propertyKey, String thingId, String organizationId) throws Throwable {
        Thing thing = thingService.get(thingId, organizationId);
        ThingType thingType = thingTypeService.get(thing.getTypeId());

        if (null == thing.getBus().getPropertyTopics() || null == thingType.getProperties()) {
            throw new ClientException("Properties are not defined for this thing");
        }

        String propertyTopic = thing.getBus().getPropertyTopics().get(propertyKey);

        if (null == propertyTopic) {
            throw new ClientException("Property " + propertyKey + " not found");
        }

        PropertyAffordance propertyAffordance = thingType.getProperties().get(propertyKey);

        if (null == propertyAffordance) {
            throw new ClientException("Property " + propertyKey + " not found");
        }

        if (!DataTypeValidator.validate(propertyAffordance.getDataType(), thingBusSetPropertyRequest.getValue())) {
            throw new ClientException("Invalid data type for the property value");
        }

        String requestId = UUID.randomUUID().toString();
        mqttPublisherGateway.send(propertyTopic, requestId, Json.encode(thingBusSetPropertyRequest.getValue()));
        return new ThingBusSetPropertyResponse(requestId);
    }

    public ThingBusPropertyChangeAckResponse ackPropertyChange(ThingBusPropertyChangeAckRequest thingBusPropertyChangeAckRequest, String propertyKey, String thingId, String organizationId) throws Throwable {
        Thing thing = thingService.get(thingId, organizationId);
        ThingType thingType = thingTypeService.get(thing.getTypeId());

        if (null == thing.getBus().getPropertyAckTopics() || null == thingType.getProperties()) {
            throw new ClientException("Properties are not defined for this thing");
        }

        String propertyAckTopic = thing.getBus().getPropertyAckTopics().get(propertyKey);

        if (null == propertyAckTopic) {
            throw new ClientException("Property " + propertyKey + " not found");
        }

        PropertyAffordance propertyAffordance = thingType.getProperties().get(propertyKey);

        if (null == propertyAffordance) {
            throw new ClientException("Property " + propertyKey + " not found");
        }

        if (!DataTypeValidator.validate(propertyAffordance.getDataType(), thingBusPropertyChangeAckRequest.getValue())) {
            throw new ClientException("Invalid data type for the property value");
        }

        mqttPublisherGateway.send(propertyAckTopic, thingBusPropertyChangeAckRequest.getRequestId(), Json.encode(thingBusPropertyChangeAckRequest.getValue()));
        return new ThingBusPropertyChangeAckResponse(thingBusPropertyChangeAckRequest.getRequestId());
    }

    public ThingBusActionInvocationResponse invokeAction(ThingBusActionInvocationRequest thingBusActionInvocationRequest, String actionKey, String thingId, String organizationId) throws Throwable {
        Thing thing = thingService.get(thingId, organizationId);
        ThingType thingType = thingTypeService.get(thing.getTypeId());

        if (null == thing.getBus().getActionInvocationTopics() || null == thingType.getActions()) {
            throw new ClientException("Actions are not defined for this thing");
        }

        String actionInvocationTopic = thing.getBus().getActionInvocationTopics().get(actionKey);

        if (null == actionInvocationTopic) {
            throw new ClientException("Action " + actionKey + " not found");
        }

        ActionAffordance actionAffordance = thingType.getActions().get(actionKey);

        if (null == actionAffordance) {
            throw new ClientException("Action " + actionKey + " not found");
        }

        if (!DataTypeValidator.validate(actionAffordance.getInput().getDataType(), thingBusActionInvocationRequest.getInput())) {
            throw new ClientException("Invalid data type for action input");
        }

        String requestId = UUID.randomUUID().toString();
        mqttPublisherGateway.send(actionInvocationTopic, requestId, Json.encode(thingBusActionInvocationRequest.getInput()));
        return new ThingBusActionInvocationResponse(requestId);
    }

    public ThingBusActionResultResponse setActionResult(ThingBusActionResultRequest thingBusActionResultRequest, String actionKey, String thingId, String organizationId) throws Throwable {
        Thing thing = thingService.get(thingId, organizationId);
        ThingType thingType = thingTypeService.get(thing.getTypeId());

        if (null == thing.getBus().getActionInvocationTopics() || null == thingType.getActions()) {
            throw new ClientException("Actions are not defined for this thing");
        }

        String actionResultTopic = thing.getBus().getActionResultTopics().get(actionKey);

        if (null == actionResultTopic) {
            throw new ClientException("Action " + actionKey + " not found");
        }

        ActionAffordance actionAffordance = thingType.getActions().get(actionKey);

        if (null == actionAffordance) {
            throw new ClientException("Action " + actionKey + " not found");
        }

        if (!DataTypeValidator.validate(actionAffordance.getOutput().getDataType(), thingBusActionResultRequest.getOutput())) {
            throw new ClientException("Invalid data type for action output");
        }

        mqttPublisherGateway.send(actionResultTopic, thingBusActionResultRequest.getRequestId(), Json.encode(thingBusActionResultRequest.getOutput()));
        return new ThingBusActionResultResponse(thingBusActionResultRequest.getRequestId());
    }

    public ThingBusEmitEventResponse emitEvent(ThingBusEmitEventRequest thingBusEmitEventRequest, String eventKey, String thingId, String organizationId) throws Throwable {
        Thing thing = thingService.get(thingId, organizationId);
        ThingType thingType = thingTypeService.get(thing.getTypeId());

        if (null == thing.getBus().getEventTopics() || null == thingType.getEvents()) {
            throw new ClientException("Events are not defined for this thing");
        }

        String eventTopic = thing.getBus().getEventTopics().get(eventKey);

        if (null == eventTopic) {
            throw new ClientException("Event " + eventKey + " not found");
        }

        EventAffordance eventAffordance = thingType.getEvents().get(eventKey);

        if (null == eventAffordance) {
            throw new ClientException("Event " + eventKey + " not found");
        }

        if (!DataTypeValidator.validate(eventAffordance.getPayload().getDataType(), thingBusEmitEventRequest.getPayload())) {
            throw new ClientException("Invalid data type for event payload");
        }

        String eventId = UUID.randomUUID().toString();
        mqttPublisherGateway.send(eventTopic, eventId, Json.encode(thingBusEmitEventRequest.getPayload()));
        return new ThingBusEmitEventResponse(eventId);
    }

    public BusCustomTopicMessageResponse publishMessageToCustomTopic(BusCustomTopicMessageRequest busCustomTopicMessageRequest, String thingId, String organizationId) throws Throwable {
        Thing thing = thingService.get(thingId, organizationId);

        String customTopic = busCustomTopicMessageRequest.getTopic();
        Bus thingBus = thing.getBus();

        if (!thingBus.getCustomPublishTopics().contains(customTopic) &&
                !thingBus.getCustomSubscribeTopics().contains(customTopic) &&
                !thingBus.getCustomPubSubTopics().contains(customTopic)) {
            throw new ClientException("Custom topic not found");
        }

        mqttPublisherGateway.send(customTopic, Json.encode(busCustomTopicMessageRequest.getMessage()));
        return new BusCustomTopicMessageResponse(true);
    }

    public BusCustomTopicMessageResponse publishMessageToCustomPublishTopic(BusCustomTopicMessageRequest busCustomTopicMessageRequest, String thingId, String organizationId) throws Throwable {
        Thing thing = thingService.get(thingId, organizationId);

        String customTopic = busCustomTopicMessageRequest.getTopic();
        Bus thingBus = thing.getBus();

        if (!thingBus.getCustomPublishTopics().contains(customTopic) && !thingBus.getCustomPubSubTopics().contains(customTopic)) {
            throw new ClientException("Custom topic not found");
        }

        mqttPublisherGateway.send(customTopic, Json.encode(busCustomTopicMessageRequest.getMessage()));
        return new BusCustomTopicMessageResponse(true);
    }
}
