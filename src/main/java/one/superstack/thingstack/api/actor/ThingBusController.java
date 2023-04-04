package one.superstack.thingstack.api.actor;

import jakarta.validation.Valid;
import one.superstack.thingstack.auth.actor.AuthenticatedController;
import one.superstack.thingstack.enums.Permission;
import one.superstack.thingstack.enums.TargetType;
import one.superstack.thingstack.request.*;
import one.superstack.thingstack.response.*;
import one.superstack.thingstack.service.AccessService;
import one.superstack.thingstack.service.ThingBusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1")
public class ThingBusController extends AuthenticatedController {

    private final ThingBusService thingBusService;

    private final AccessService accessService;

    @Autowired
    public ThingBusController(ThingBusService thingBusService, AccessService accessService) {
        this.thingBusService = thingBusService;
        this.accessService = accessService;
    }

    @PutMapping(value = "/things/{thingId}/bus/properties/{propertyKey}")
    public ThingBusSetPropertyResponse setProperty(@PathVariable String thingId, @PathVariable String propertyKey, @Valid @RequestBody ThingBusSetPropertyRequest thingBusSetPropertyRequest) {
        checkAccess(accessService, TargetType.THING, thingId, Permission.SET_PROPERTY);
        return thingBusService.setProperty(thingBusSetPropertyRequest, propertyKey, thingId, getOrganizationId());
    }

    @PostMapping(value = "/things/{thingId}/bus/properties/{propertyKey}/ack")
    public ThingBusPropertyChangeAckResponse ackPropertyChange(@PathVariable String thingId, @PathVariable String propertyKey, @Valid @RequestBody ThingBusPropertyChangeAckRequest thingBusPropertyChangeAckRequest) {
        checkAccess(accessService, TargetType.THING, thingId, Permission.ACK_PROPERTY_CHANGE);
        return thingBusService.ackPropertyChange(thingBusPropertyChangeAckRequest, propertyKey, thingId, getOrganizationId());
    }

    @PostMapping(value = "/things/{thingId}/bus/actions/{actionKey}/invoke")
    public ThingBusActionInvocationResponse invokeAction(@PathVariable String thingId, @PathVariable String actionKey, @Valid @RequestBody ThingBusActionInvocationRequest thingBusActionInvocationRequest) {
        checkAccess(accessService, TargetType.THING, thingId, Permission.INVOKE_ACTION);
        return thingBusService.invokeAction(thingBusActionInvocationRequest, actionKey, thingId, getOrganizationId());
    }

    @PostMapping(value = "/things/{thingId}/bus/actions/{actionKey}/results")
    public ThingBusActionResultResponse setActionResult(@PathVariable String thingId, @PathVariable String actionKey, @Valid @RequestBody ThingBusActionResultRequest thingBusActionResultRequest) {
        checkAccess(accessService, TargetType.THING, thingId, Permission.SET_ACTION_RESULT);
        return thingBusService.setActionResult(thingBusActionResultRequest, actionKey, thingId, getOrganizationId());
    }

    @PostMapping(value = "/things/{thingId}/bus/events/{eventKey}/emit")
    public ThingBusEmitEventResponse emitEvent(@PathVariable String thingId, @PathVariable String eventKey, @Valid @RequestBody ThingBusEmitEventRequest thingBusEmitEventRequest) {
        checkAccess(accessService, TargetType.THING, thingId, Permission.EMIT_EVENT);
        return thingBusService.emitEvent(thingBusEmitEventRequest, eventKey, thingId, getOrganizationId());
    }
}
