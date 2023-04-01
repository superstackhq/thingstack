package one.superstack.thingstack.controller;

import jakarta.validation.Valid;
import one.superstack.thingstack.auth.actor.AuthenticatedController;
import one.superstack.thingstack.enums.Permission;
import one.superstack.thingstack.enums.TargetType;
import one.superstack.thingstack.model.Thing;
import one.superstack.thingstack.request.ThingBusCustomTopicRequest;
import one.superstack.thingstack.request.ThingBusTopicChangeRequest;
import one.superstack.thingstack.request.ThingCreationRequest;
import one.superstack.thingstack.request.ThingUpdateRequest;
import one.superstack.thingstack.response.AccessKeyResponse;
import one.superstack.thingstack.response.SuccessResponse;
import one.superstack.thingstack.service.AccessService;
import one.superstack.thingstack.service.ThingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
public class ThingController extends AuthenticatedController {

    private final ThingService thingService;

    private final AccessService accessService;

    @Autowired
    public ThingController(ThingService thingService, AccessService accessService) {
        this.thingService = thingService;
        this.accessService = accessService;
    }

    @PostMapping(value = "/things")
    public Thing create(@Valid @RequestBody ThingCreationRequest thingCreationRequest) throws Throwable {
        checkAccess(accessService, TargetType.THING, null, Permission.CREATE);
        return thingService.create(thingCreationRequest, getActor());
    }

    @GetMapping(value = "/things")
    public List<Thing> list(@RequestParam String typeId, Pageable pageable) {
        return thingService.list(typeId, getOrganizationId(), pageable);
    }

    @GetMapping(value = "/things/{thingId}")
    public Thing get(@PathVariable String thingId) throws Throwable {
        return thingService.get(thingId, getOrganizationId());
    }

    @PutMapping(value = "/things/{thingId}")
    public Thing update(@PathVariable String thingId, @Valid @RequestBody ThingUpdateRequest thingUpdateRequest) throws Throwable {
        checkAccess(accessService, TargetType.THING, thingId, Permission.UPDATE);
        return thingService.update(thingId, thingUpdateRequest, getOrganizationId());
    }

    @DeleteMapping(value = "/things/{thingId}")
    public Thing delete(@PathVariable String thingId) throws Throwable {
        checkAccess(accessService, TargetType.THING, thingId, Permission.DELETE);
        return thingService.delete(thingId, getOrganizationId());
    }

    @GetMapping(value = "/things/{thingId}/access-key")
    public AccessKeyResponse getAccessKey(@PathVariable String thingId) throws Throwable {
        checkAccess(accessService, TargetType.THING, thingId, Permission.MANAGE_ACCESS_KEY);
        return thingService.getAccessKey(thingId, getOrganizationId());
    }

    @PutMapping(value = "/things/{thingId}/access-key")
    public AccessKeyResponse resetAccessKey(@PathVariable String thingId) throws Throwable {
        checkAccess(accessService, TargetType.THING, thingId, Permission.MANAGE_ACCESS_KEY);
        return thingService.resetAccessKey(thingId, getOrganizationId());
    }

    @PutMapping(value = "/things/{thingId}/bus/topics")
    public SuccessResponse changeAffordanceTopic(@PathVariable String thingId, @Valid @RequestBody ThingBusTopicChangeRequest thingBusTopicChangeRequest) {
        checkAccess(accessService, TargetType.THING, thingId, Permission.MANAGE_BUS);
        thingService.changeAffordanceTopic(thingId, thingBusTopicChangeRequest, getOrganizationId());
        return new SuccessResponse("Thing bus topics changed successfully");
    }

    @PostMapping(value = "/things/{thingId}/bus/topics/custom")
    public SuccessResponse addCustomTopicAccess(@PathVariable String thingId, @Valid @RequestBody ThingBusCustomTopicRequest thingBusCustomTopicRequest) {
        checkAccess(accessService, TargetType.THING, thingId, Permission.MANAGE_BUS);
        thingService.addCustomTopicAccess(thingId, thingBusCustomTopicRequest, getOrganizationId());
        return new SuccessResponse("Custom topic added to thing bus successfully");
    }

    @DeleteMapping(value = "/things/{thingId}/bus/topics/custom")
    public SuccessResponse deleteCustomTopicAccess(@PathVariable String thingId, @Valid @RequestBody ThingBusCustomTopicRequest thingBusCustomTopicRequest) {
        checkAccess(accessService, TargetType.THING, thingId, Permission.MANAGE_BUS);
        thingService.deleteCustomTopicAccess(thingId, thingBusCustomTopicRequest, getOrganizationId());
        return new SuccessResponse("Custom topic removed from thing bus successfully");
    }
}
