package one.superstack.thingstack.service;

import one.superstack.thingstack.publisher.MqttPublisherGateway;
import one.superstack.thingstack.request.*;
import one.superstack.thingstack.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public ThingBusSetPropertyResponse setProperty(ThingBusSetPropertyRequest thingBusSetPropertyRequest, String propertyKey, String thingId, String organizationId) {
        // TODO
        return null;
    }

    public ThingBusPropertyChangeAckResponse ackPropertyChange(ThingBusPropertyChangeAckRequest thingBusPropertyChangeAckRequest, String propertyKey, String thingId, String organizationId) {
        // TODO
        return null;
    }

    public ThingBusActionInvocationResponse invokeAction(ThingBusActionInvocationRequest thingBusActionInvocationRequest, String actionKey, String thingId, String organizationId) {
        // TODO
        return null;
    }

    public ThingBusActionResultResponse setActionResult(ThingBusActionResultRequest thingBusActionResultRequest, String actionKey, String thingId, String organizationId) {
        // TODO
        return null;
    }

    public ThingBusEmitEventResponse emitEvent(ThingBusEmitEventRequest thingBusEmitEventRequest, String eventKey, String thingId, String organizationId) {
        // TODO
        return null;
    }
}
