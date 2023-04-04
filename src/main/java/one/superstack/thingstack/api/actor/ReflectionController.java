package one.superstack.thingstack.api.actor;

import jakarta.validation.Valid;
import one.superstack.thingstack.auth.actor.AuthenticatedController;
import one.superstack.thingstack.enums.Permission;
import one.superstack.thingstack.enums.TargetType;
import one.superstack.thingstack.model.Reflection;
import one.superstack.thingstack.request.CustomBusTopicAccessRequest;
import one.superstack.thingstack.response.AccessKeyResponse;
import one.superstack.thingstack.response.SuccessResponse;
import one.superstack.thingstack.service.AccessService;
import one.superstack.thingstack.service.ReflectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1")
public class ReflectionController extends AuthenticatedController {

    private final ReflectionService reflectionService;

    private final AccessService accessService;

    @Autowired
    public ReflectionController(ReflectionService reflectionService, AccessService accessService) {
        this.reflectionService = reflectionService;
        this.accessService = accessService;
    }

    @GetMapping(value = "/things/{thingId}/reflection")
    public Reflection get(@PathVariable String thingId) throws Throwable {
        return reflectionService.get(thingId, getOrganizationId());
    }

    @GetMapping(value = "/things/{thingId}/reflection/access-key")
    public AccessKeyResponse getAccessKey(@PathVariable String thingId) throws Throwable {
        checkAccess(accessService, TargetType.THING, thingId, Permission.MANAGE_REFLECTION);
        return reflectionService.getAccessKey(thingId, getOrganizationId());
    }

    @PutMapping(value = "/things/{thingId}/reflection/access-key")
    public AccessKeyResponse resetAccessKey(@PathVariable String thingId) throws Throwable {
        checkAccess(accessService, TargetType.THING, thingId, Permission.MANAGE_REFLECTION);
        return reflectionService.resetAccessKey(thingId, getOrganizationId());
    }

    @PostMapping(value = "/things/{thingId}/reflection/bus/topics/custom")
    public SuccessResponse addCustomTopicAccess(@PathVariable String thingId, @Valid @RequestBody CustomBusTopicAccessRequest customBusTopicAccessRequest) {
        checkAccess(accessService, TargetType.THING, thingId, Permission.MANAGE_REFLECTION);
        reflectionService.addCustomTopicAccess(thingId, customBusTopicAccessRequest, getOrganizationId());
        return new SuccessResponse("Custom topic access added to reflection bus successfully");
    }

    @DeleteMapping(value = "/things/{thingId}/reflection/bus/topics/custom")
    public SuccessResponse deleteCustomTopicAccess(@PathVariable String thingId, @Valid @RequestBody CustomBusTopicAccessRequest customBusTopicAccessRequest) {
        checkAccess(accessService, TargetType.THING, thingId, Permission.MANAGE_REFLECTION);
        reflectionService.deleteCustomTopicAccess(thingId, customBusTopicAccessRequest, getOrganizationId());
        return new SuccessResponse("Custom topic access removed from reflection bus succesfully");
    }
}
