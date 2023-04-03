package one.superstack.thingstack.service;

import one.superstack.thingstack.embedded.HookReference;
import one.superstack.thingstack.enums.HookType;
import one.superstack.thingstack.exception.ClientException;
import one.superstack.thingstack.model.WebHook;
import one.superstack.thingstack.pojo.Hook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class HookService {

    private final WebhookService webhookService;

    @Autowired
    public HookService(WebhookService webhookService) {
        this.webhookService = webhookService;
    }

    public Boolean allExist(Set<HookReference> hookReferences, String organizationId) throws ExecutionException, InterruptedException {
        Set<String> webhookIds = new HashSet<>();

        for (HookReference hookReference : hookReferences) {
            switch (hookReference.getType()) {
                case WEB -> webhookIds.add(hookReference.getId());
                default -> throw new ClientException("Invalid hook type");
            }
        }

        CompletableFuture<Boolean> allWebHooksExistFuture = null;

        if (!webhookIds.isEmpty()) {
            allWebHooksExistFuture = webhookService.asyncAllExist(webhookIds, organizationId);
        }

        if (null != allWebHooksExistFuture) {
            if (!allWebHooksExistFuture.get()) {
                return false;
            }
        }

        return true;
    }

    public Boolean exists(HookReference hookReference, String organizationId) {
        switch (hookReference.getType()) {
            case WEB -> {
                return webhookService.exists(hookReference.getId(), organizationId);
            }

            default -> throw new ClientException("Invalid hook type");
        }
    }

    public List<Hook> fetch(Set<HookReference> hookReferences) throws ExecutionException, InterruptedException {
        List<String> webhookIds = new ArrayList<>();

        for (HookReference hookReference : hookReferences) {
            switch (hookReference.getType()) {
                case WEB -> webhookIds.add(hookReference.getId());
                default -> throw new ClientException("Invalid hook type");
            }
        }

        CompletableFuture<List<WebHook>> webhooksFuture = null;
        if (!webhookIds.isEmpty()) {
            webhooksFuture = webhookService.asyncGet(webhookIds);
        }

        List<Hook> hooks = new ArrayList<>();

        if (null != webhooksFuture) {
            for (WebHook webHook : webhooksFuture.get()) {
                hooks.add(new Hook(HookType.WEB, webHook.getId(), webHook));
            }
        }

        return hooks;
    }
}
