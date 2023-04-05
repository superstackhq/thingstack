package one.superstack.thingstack.api.reflection;

import jakarta.validation.Valid;
import one.superstack.thingstack.auth.reflection.AuthenticatedReflectionController;
import one.superstack.thingstack.request.ThingBusActionInvocationRequest;
import one.superstack.thingstack.request.ThingBusSetPropertyRequest;
import one.superstack.thingstack.response.ThingBusActionInvocationResponse;
import one.superstack.thingstack.response.ThingBusSetPropertyResponse;
import one.superstack.thingstack.service.ThingBusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/reflections/api/v1")
public class ThingBusController extends AuthenticatedReflectionController {

    private final ThingBusService thingBusService;

    @Autowired
    public ThingBusController(ThingBusService thingBusService) {
        this.thingBusService = thingBusService;
    }

    @PostMapping(value = "/thing/bus/actions/{actionKey}/invoke")
    public ThingBusActionInvocationResponse invokeAction(@PathVariable String actionKey, @Valid @RequestBody ThingBusActionInvocationRequest thingBusActionInvocationRequest) throws Throwable {
        return thingBusService.invokeAction(thingBusActionInvocationRequest, actionKey, getThingId(), getOrganizationId());
    }

    @PutMapping(value = "/thing/bus/properties/{propertyKey}")
    public ThingBusSetPropertyResponse setProperty(@PathVariable String propertyKey, @Valid @RequestBody ThingBusSetPropertyRequest thingBusSetPropertyRequest) throws Throwable {
        return thingBusService.setProperty(thingBusSetPropertyRequest, propertyKey, getThingId(), getOrganizationId());
    }
}
