package one.superstack.thingstack.controller;

import jakarta.validation.Valid;
import one.superstack.thingstack.auth.actor.AuthenticatedController;
import one.superstack.thingstack.embedded.HookReference;
import one.superstack.thingstack.enums.Permission;
import one.superstack.thingstack.enums.TargetType;
import one.superstack.thingstack.model.Rule;
import one.superstack.thingstack.pojo.Hook;
import one.superstack.thingstack.request.RuleCreationRequest;
import one.superstack.thingstack.request.RuleUpdateRequest;
import one.superstack.thingstack.response.SuccessResponse;
import one.superstack.thingstack.service.AccessService;
import one.superstack.thingstack.service.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(value = "/api/v1")
public class RuleController extends AuthenticatedController {

    private final RuleService ruleService;

    private final AccessService accessService;

    @Autowired
    public RuleController(RuleService ruleService, AccessService accessService) {
        this.ruleService = ruleService;
        this.accessService = accessService;
    }

    @PostMapping(value = "/rules")
    public Rule create(@Valid @RequestBody RuleCreationRequest ruleCreationRequest) throws ExecutionException, InterruptedException {
        checkAccess(accessService, TargetType.RULE, null, Permission.CREATE);
        return ruleService.create(ruleCreationRequest, getActor());
    }

    @GetMapping(value = "/rules")
    public List<Rule> list(Pageable pageable) {
        return ruleService.list(getOrganizationId(), pageable);
    }

    @GetMapping(value = "/rules/{ruleId}")
    public Rule get(@PathVariable String ruleId) throws Throwable {
        return ruleService.get(ruleId, getOrganizationId());
    }

    @PutMapping(value = "/rules/{ruleId}")
    public Rule update(@PathVariable String ruleId, @Valid @RequestBody RuleUpdateRequest ruleUpdateRequest) throws Throwable {
        checkAccess(accessService, TargetType.RULE, ruleId, Permission.UPDATE);
        return ruleService.update(ruleId, ruleUpdateRequest, getOrganizationId());
    }

    @DeleteMapping(value = "/rules/{ruleId}")
    public Rule delete(@PathVariable String ruleId) throws Throwable {
        checkAccess(accessService, TargetType.RULE, ruleId, Permission.DELETE);
        return ruleService.delete(ruleId, getOrganizationId());
    }

    @PostMapping(value = "/rules/{ruleId}/hooks")
    public SuccessResponse addHook(@PathVariable String ruleId, @Valid @RequestBody HookReference hookReference) {
        checkAccess(accessService, TargetType.RULE, ruleId, Permission.MANAGE_HOOK);
        ruleService.addHook(ruleId, hookReference, getOrganizationId());
        return new SuccessResponse("Hook added to rule successfully");
    }

    @DeleteMapping(value = "/rules/{ruleId}/hooks")
    public SuccessResponse deleteHook(@PathVariable String ruleId, @Valid @RequestBody HookReference hookReference) {
        checkAccess(accessService, TargetType.RULE, ruleId, Permission.MANAGE_HOOK);
        ruleService.deleteHook(ruleId, hookReference, getOrganizationId());
        return new SuccessResponse("Hook removed from rule successfully");
    }

    @GetMapping(value = "/rules/{ruleId}/hooks")
    public List<Hook> listHooks(@PathVariable String ruleId) throws Throwable {
        return ruleService.listHooks(ruleId, getOrganizationId());
    }
}
