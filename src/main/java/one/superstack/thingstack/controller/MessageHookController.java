package one.superstack.thingstack.controller;

import jakarta.validation.Valid;
import one.superstack.thingstack.auth.actor.AuthenticatedController;
import one.superstack.thingstack.embedded.Broker;
import one.superstack.thingstack.enums.Permission;
import one.superstack.thingstack.enums.TargetType;
import one.superstack.thingstack.model.MessageHook;
import one.superstack.thingstack.request.MessageHookCreationRequest;
import one.superstack.thingstack.request.MessageHookUpdateRequest;
import one.superstack.thingstack.response.PasswordResponse;
import one.superstack.thingstack.service.AccessService;
import one.superstack.thingstack.service.MessageHookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/hooks")
public class MessageHookController extends AuthenticatedController {

    private final MessageHookService messageHookService;

    private final AccessService accessService;

    @Autowired
    public MessageHookController(MessageHookService messageHookService, AccessService accessService) {
        this.messageHookService = messageHookService;
        this.accessService = accessService;
    }

    @PostMapping(value = "/message")
    public MessageHook create(@Valid @RequestBody MessageHookCreationRequest messageHookCreationRequest) {
        checkAccess(accessService, TargetType.MESSAGE_HOOK, null, Permission.CREATE);
        return messageHookService.create(messageHookCreationRequest, getActor());
    }

    @GetMapping(value = "/message")
    public List<MessageHook> list(Pageable pageable) {
        return messageHookService.list(getOrganizationId(), pageable);
    }

    @GetMapping(value = "/message/{messageHookId}")
    public MessageHook get(@PathVariable String messageHookId) throws Throwable {
        return messageHookService.get(messageHookId, getOrganizationId());
    }

    @PutMapping(value = "/message/{messageHookId}")
    public MessageHook update(@PathVariable String messageHookId, @Valid @RequestBody MessageHookUpdateRequest messageHookUpdateRequest) throws Throwable {
        checkAccess(accessService, TargetType.MESSAGE_HOOK, messageHookId, Permission.UPDATE);
        return messageHookService.update(messageHookId, messageHookUpdateRequest, getOrganizationId());
    }

    @DeleteMapping(value = "/message/{messageHookId}")
    public MessageHook delete(@PathVariable String messageHookId) throws Throwable {
        checkAccess(accessService, TargetType.MESSAGE_HOOK, messageHookId, Permission.DELETE);
        return messageHookService.delete(messageHookId, getOrganizationId());
    }

    @PutMapping(value = "/message/{messageHookId}/broker")
    public MessageHook updateBroker(@PathVariable String messageHookId, @Valid @RequestBody Broker broker) throws Throwable {
        checkAccess(accessService, TargetType.MESSAGE_HOOK, messageHookId, Permission.MANAGE_HOOK);
        return messageHookService.updateBroker(messageHookId, broker, getOrganizationId());
    }

    @GetMapping(value = "/message/{messageHookId}/broker/password")
    public PasswordResponse getBrokerPassword(@PathVariable String messageHookId) throws Throwable {
        checkAccess(accessService, TargetType.MESSAGE_HOOK, messageHookId, Permission.MANAGE_HOOK);
        return messageHookService.getBrokerPassword(messageHookId, getOrganizationId());
    }
}
