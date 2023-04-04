package one.superstack.thingstack.api.actor;

import jakarta.validation.Valid;
import one.superstack.thingstack.auth.actor.AuthenticatedController;
import one.superstack.thingstack.enums.Permission;
import one.superstack.thingstack.enums.TargetType;
import one.superstack.thingstack.model.ThingType;
import one.superstack.thingstack.request.ThingTypeCreationRequest;
import one.superstack.thingstack.request.ThingTypeUpdateRequest;
import one.superstack.thingstack.service.AccessService;
import one.superstack.thingstack.service.ThingTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
public class ThingTypeController extends AuthenticatedController {

    private final ThingTypeService thingTypeService;

    private final AccessService accessService;

    @Autowired
    public ThingTypeController(ThingTypeService thingTypeService, AccessService accessService) {
        this.thingTypeService = thingTypeService;
        this.accessService = accessService;
    }


    @PostMapping(value = "/things/types")
    public ThingType create(@Valid @RequestBody ThingTypeCreationRequest thingTypeCreationRequest) {
        checkAccess(accessService, TargetType.THING_TYPE, null, Permission.CREATE);
        return thingTypeService.create(thingTypeCreationRequest, getActor());
    }

    @GetMapping(value = "/things/types")
    public List<ThingType> list(Pageable pageable) {
        return thingTypeService.list(getOrganizationId(), pageable);
    }

    @GetMapping(value = "/things/types/{typeId}")
    public ThingType get(@PathVariable String typeId) throws Throwable {
        return thingTypeService.get(typeId, getOrganizationId());
    }

    @PutMapping(value = "/things/types/{typeId}")
    public ThingType update(@PathVariable String typeId, @Valid @RequestBody ThingTypeUpdateRequest thingTypeUpdateRequest) throws Throwable {
        checkAccess(accessService, TargetType.THING_TYPE, typeId, Permission.UPDATE);
        return thingTypeService.update(typeId, thingTypeUpdateRequest, getOrganizationId());
    }

    @DeleteMapping(value = "/things/types/{typeId}")
    public ThingType delete(@PathVariable String typeId) throws Throwable {
        checkAccess(accessService, TargetType.THING_TYPE, typeId, Permission.DELETE);
        return thingTypeService.delete(typeId, getOrganizationId());
    }
}
