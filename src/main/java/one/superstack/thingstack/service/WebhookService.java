package one.superstack.thingstack.service;

import one.superstack.thingstack.auth.actor.AuthenticatedActor;
import one.superstack.thingstack.enums.Permission;
import one.superstack.thingstack.enums.TargetType;
import one.superstack.thingstack.exception.ClientException;
import one.superstack.thingstack.exception.NotFoundException;
import one.superstack.thingstack.model.WebHook;
import one.superstack.thingstack.repository.WebHookRepository;
import one.superstack.thingstack.request.AccessRequest;
import one.superstack.thingstack.request.WebHookCreationRequest;
import one.superstack.thingstack.request.WebHookUpdateRequest;
import one.superstack.thingstack.response.SecretResponse;
import one.superstack.thingstack.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@Service
public class WebhookService {

    private final WebHookRepository webHookRepository;

    private final AccessService accessService;

    @Autowired
    public WebhookService(WebHookRepository webHookRepository, AccessService accessService) {
        this.webHookRepository = webHookRepository;
        this.accessService = accessService;
    }

    public WebHook create(WebHookCreationRequest webHookCreationRequest, AuthenticatedActor creator) {
        if (nameExists(webHookCreationRequest.getName(), creator.getOrganizationId())) {
            throw new ClientException("Webhook " + webHookCreationRequest.getName() + " already exists");
        }

        WebHook webHook = new WebHook(webHookCreationRequest.getName(),
                webHookCreationRequest.getDescription(),
                webHookCreationRequest.getEndpoint(),
                Random.generateRandomString(128),
                creator.getOrganizationId(),
                creator.getType(),
                creator.getId());

        webHook = webHookRepository.save(webHook);

        accessService.add(new AccessRequest(TargetType.WEB_HOOK, webHook.getId(), creator.getType(), creator.getId(), Set.of(Permission.ALL)), creator);
        return webHook;
    }

    public List<WebHook> list(String organizationId, Pageable pageable) {
        return webHookRepository.findByOrganizationId(organizationId, pageable);
    }

    public WebHook get(String webhookId, String organizationId) throws Throwable {
        return webHookRepository.findByIdAndOrganizationId(webhookId, organizationId)
                .orElseThrow((Supplier<Throwable>) () -> new NotFoundException("Webhook not found"));
    }

    public WebHook get(String webhookId) throws Throwable {
        return webHookRepository.findById(webhookId)
                .orElseThrow((Supplier<Throwable>) () -> new NotFoundException("Webhook not found"));
    }

    @Async
    public CompletableFuture<List<WebHook>> asyncGet(List<String> webhookIds) {
        return CompletableFuture.completedFuture(get(webhookIds));
    }

    public List<WebHook> get(List<String> webhookIds) {
        return webHookRepository.findByIdIn(webhookIds);
    }

    public WebHook update(String webhookId, WebHookUpdateRequest webHookUpdateRequest, String organizationId) throws Throwable {
        WebHook webHook = get(webhookId, organizationId);

        if (null != webHookUpdateRequest.getName() && !webHookUpdateRequest.getName().isEmpty()) {
            if (webHookRepository.existsByIdNotAndNameAndOrganizationId(webhookId, webHookUpdateRequest.getName(), organizationId)) {
                throw new ClientException("Webhook " + webHookUpdateRequest.getName() + " already exists");
            }

            webHook.setName(webHookUpdateRequest.getName());
        }

        if (null != webHookUpdateRequest.getEndpoint() && !webHookUpdateRequest.getEndpoint().isEmpty()) {
            webHook.setEndpoint(webHookUpdateRequest.getEndpoint());
        }

        webHook.setDescription(webHookUpdateRequest.getDescription());

        webHook.setModifiedOn(new Date());
        return webHookRepository.save(webHook);
    }

    public WebHook delete(String webhookId, String organizationId) throws Throwable {
        WebHook webHook = get(webhookId, organizationId);
        webHookRepository.delete(webHook);
        accessService.deleteAllForTarget(TargetType.WEB_HOOK, webhookId);
        return webHook;
    }

    public SecretResponse getSecret(String webhookId, String organizationId) throws Throwable {
        return new SecretResponse(get(webhookId, organizationId).getSecret());
    }

    public SecretResponse resetSecret(String webhookId, String organizationId) throws Throwable {
        WebHook webHook = get(webhookId, organizationId);
        webHook.setSecret(Random.generateRandomString(128));
        webHook.setModifiedOn(new Date());
        webHook = webHookRepository.save(webHook);
        return new SecretResponse(webHook.getSecret());
    }

    public Boolean exists(String webhookId, String organizationId) {
        return webHookRepository.existsByIdAndOrganizationId(webhookId, organizationId);
    }

    @Async
    public CompletableFuture<Boolean> asyncAllExist(Set<String> webhookIds, String organizationId) {
        return CompletableFuture.completedFuture(allExist(webhookIds, organizationId));
    }

    public Boolean allExist(Set<String> webhookIds, String organizationId) {
        return webHookRepository.countByIdInAndOrganizationId(webhookIds, organizationId) == webhookIds.size();
    }

    private Boolean nameExists(String name, String organizationId) {
        return webHookRepository.existsByNameAndOrganizationId(name, organizationId);
    }
}
