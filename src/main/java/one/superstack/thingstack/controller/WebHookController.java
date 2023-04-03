package one.superstack.thingstack.controller;

import jakarta.validation.Valid;
import one.superstack.thingstack.auth.actor.AuthenticatedController;
import one.superstack.thingstack.enums.Permission;
import one.superstack.thingstack.enums.TargetType;
import one.superstack.thingstack.model.WebHook;
import one.superstack.thingstack.request.WebHookCreationRequest;
import one.superstack.thingstack.request.WebHookUpdateRequest;
import one.superstack.thingstack.response.SecretResponse;
import one.superstack.thingstack.service.AccessService;
import one.superstack.thingstack.service.WebhookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/hooks")
public class WebHookController extends AuthenticatedController {

    private final WebhookService webhookService;

    private final AccessService accessService;

    @Autowired
    public WebHookController(WebhookService webhookService, AccessService accessService) {
        this.webhookService = webhookService;
        this.accessService = accessService;
    }

    @PostMapping(value = "/web")
    public WebHook create(@Valid @RequestBody WebHookCreationRequest webHookCreationRequest) {
        checkAccess(accessService, TargetType.WEB_HOOK, null, Permission.CREATE);
        return webhookService.create(webHookCreationRequest, getActor());
    }

    @GetMapping(value = "/web")
    public List<WebHook> list(Pageable pageable) {
        return webhookService.list(getOrganizationId(), pageable);
    }

    @GetMapping(value = "/web/{webhookId}")
    public WebHook get(@PathVariable String webhookId) throws Throwable {
        return webhookService.get(webhookId, getOrganizationId());
    }

    @PutMapping(value = "/web/{webhookId}")
    public WebHook update(@PathVariable String webhookId, @Valid @RequestBody WebHookUpdateRequest webHookUpdateRequest) throws Throwable {
        checkAccess(accessService, TargetType.WEB_HOOK, webhookId, Permission.UPDATE);
        return webhookService.update(webhookId, webHookUpdateRequest, getOrganizationId());
    }

    @DeleteMapping(value = "/web/{webhookId}")
    public WebHook delete(@PathVariable String webhookId) throws Throwable {
        checkAccess(accessService, TargetType.WEB_HOOK, webhookId, Permission.DELETE);
        return webhookService.delete(webhookId, getOrganizationId());
    }

    @GetMapping(value = "/web/{webhookId}/secret")
    public SecretResponse getSecret(@PathVariable String webhookId) throws Throwable {
        checkAccess(accessService, TargetType.WEB_HOOK, webhookId, Permission.MANAGE_SECRET);
        return webhookService.getSecret(webhookId, getOrganizationId());
    }

    @PutMapping(value = "/web/{webhookId}/secret")
    public SecretResponse resetSecret(@PathVariable String webhookId) throws Throwable {
        checkAccess(accessService, TargetType.WEB_HOOK, webhookId, Permission.MANAGE_SECRET);
        return webhookService.resetSecret(webhookId, getOrganizationId());
    }
}
