package one.superstack.thingstack.api.thing;

import jakarta.validation.Valid;
import one.superstack.thingstack.auth.thing.AuthenticatedThingController;
import one.superstack.thingstack.request.BusCustomTopicMessageRequest;
import one.superstack.thingstack.request.ThingBusActionResultRequest;
import one.superstack.thingstack.request.ThingBusEmitEventRequest;
import one.superstack.thingstack.request.ThingBusPropertyChangeAckRequest;
import one.superstack.thingstack.response.BusCustomTopicMessageResponse;
import one.superstack.thingstack.response.ThingBusActionResultResponse;
import one.superstack.thingstack.response.ThingBusEmitEventResponse;
import one.superstack.thingstack.response.ThingBusPropertyChangeAckResponse;
import one.superstack.thingstack.service.ThingBusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/things/api/v1")
public class ThingBusController extends AuthenticatedThingController {

    private final ThingBusService thingBusService;

    @Autowired
    public ThingBusController(ThingBusService thingBusService) {
        this.thingBusService = thingBusService;
    }

    @PostMapping(value = "/thing/bus/properties/{propertyKey}/ack")
    public ThingBusPropertyChangeAckResponse ackPropertyChange(@PathVariable String propertyKey, @Valid @RequestBody ThingBusPropertyChangeAckRequest thingBusPropertyChangeAckRequest) throws Throwable {
        return thingBusService.ackPropertyChange(thingBusPropertyChangeAckRequest, propertyKey, getThingId(), getOrganizationId());
    }

    @PostMapping(value = "/thing/bus/actions/{actionKey}/results")
    public ThingBusActionResultResponse setActionResult(@PathVariable String actionKey, @Valid @RequestBody ThingBusActionResultRequest thingBusActionResultRequest) throws Throwable {
        return thingBusService.setActionResult(thingBusActionResultRequest, actionKey, getThingId(), getOrganizationId());
    }

    @PostMapping(value = "/thing/bus/events/{eventKey}/emit")
    public ThingBusEmitEventResponse emitEvent(@PathVariable String eventKey, @Valid @RequestBody ThingBusEmitEventRequest thingBusEmitEventRequest) throws Throwable {
        return thingBusService.emitEvent(thingBusEmitEventRequest, eventKey, getThingId(), getOrganizationId());
    }

    @PostMapping(value = "/thing/bus/topics/custom/message")
    public BusCustomTopicMessageResponse sendMessageToCustomTopic(@Valid @RequestBody BusCustomTopicMessageRequest busCustomTopicMessageRequest) throws Throwable {
        return thingBusService.publishMessageToCustomPublishTopic(busCustomTopicMessageRequest, getThingId(), getOrganizationId());
    }
}
