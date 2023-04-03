package one.superstack.thingstack.service;

import one.superstack.thingstack.auth.actor.AuthenticatedActor;
import one.superstack.thingstack.embedded.Broker;
import one.superstack.thingstack.enums.Permission;
import one.superstack.thingstack.enums.TargetType;
import one.superstack.thingstack.exception.ClientException;
import one.superstack.thingstack.exception.NotFoundException;
import one.superstack.thingstack.model.MessageHook;
import one.superstack.thingstack.repository.MessageHookRepository;
import one.superstack.thingstack.request.AccessRequest;
import one.superstack.thingstack.request.MessageHookCreationRequest;
import one.superstack.thingstack.request.MessageHookUpdateRequest;
import one.superstack.thingstack.response.PasswordResponse;
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
public class MessageHookService {

    private final MessageHookRepository messageHookRepository;

    private final AccessService accessService;

    @Autowired
    public MessageHookService(MessageHookRepository messageHookRepository, AccessService accessService) {
        this.messageHookRepository = messageHookRepository;
        this.accessService = accessService;
    }

    public MessageHook create(MessageHookCreationRequest messageHookCreationRequest, AuthenticatedActor creator) {
        if (nameExists(messageHookCreationRequest.getName(), creator.getOrganizationId())) {
            throw new ClientException("Message hook " + messageHookCreationRequest.getName() + " already exists");
        }

        MessageHook messageHook = new MessageHook(messageHookCreationRequest.getName(),
                messageHookCreationRequest.getDescription(),
                messageHookCreationRequest.getBroker(),
                messageHookCreationRequest.getTopic(),
                creator.getOrganizationId(),
                creator.getType(),
                creator.getId());

        messageHook = messageHookRepository.save(messageHook);

        accessService.add(new AccessRequest(TargetType.MESSAGE_HOOK, messageHook.getId(), creator.getType(), creator.getId(), Set.of(Permission.ALL)), creator);
        return messageHook;
    }

    public List<MessageHook> list(String organizationId, Pageable pageable) {
        return messageHookRepository.findByOrganizationId(organizationId, pageable);
    }

    public MessageHook get(String messageHookId, String organizationId) throws Throwable {
        return messageHookRepository.findByIdAndOrganizationId(messageHookId, organizationId)
                .orElseThrow((Supplier<Throwable>) () -> new NotFoundException("Message hook not found"));
    }

    public MessageHook get(String messageHookId) throws Throwable {
        return messageHookRepository.findById(messageHookId)
                .orElseThrow((Supplier<Throwable>) () -> new NotFoundException("Message hook not found"));
    }

    @Async
    public CompletableFuture<List<MessageHook>> asyncGet(List<String> messageHookIds) {
        return CompletableFuture.completedFuture(get(messageHookIds));
    }

    public List<MessageHook> get(List<String> messageHookIds) {
        return messageHookRepository.findByIdIn(messageHookIds);
    }

    public MessageHook update(String messageHookId, MessageHookUpdateRequest messageHookUpdateRequest, String organizationId) throws Throwable {
        MessageHook messageHook = get(messageHookId, organizationId);

        if (null != messageHookUpdateRequest.getName() && !messageHookUpdateRequest.getName().isBlank()) {
            if (messageHookRepository.existsByIdNotAndNameAndOrganizationId(messageHookId, messageHookUpdateRequest.getName(), organizationId)) {
                throw new ClientException("Message hook " + messageHookUpdateRequest.getName() + " already exists");
            }

            messageHook.setName(messageHookUpdateRequest.getName());
        }

        messageHook.setDescription(messageHookUpdateRequest.getDescription());

        if (null != messageHookUpdateRequest.getTopic() && !messageHookUpdateRequest.getTopic().isBlank()) {
            messageHook.setTopic(messageHookUpdateRequest.getTopic());
        }

        messageHook.setModifiedOn(new Date());
        return messageHookRepository.save(messageHook);
    }

    public MessageHook delete(String messageHookId, String organizationId) throws Throwable {
        MessageHook messageHook = get(messageHookId, organizationId);
        messageHookRepository.delete(messageHook);
        accessService.deleteAllForTarget(TargetType.MESSAGE_HOOK, messageHookId);
        return messageHook;
    }

    public MessageHook updateBroker(String messageHookId, Broker broker, String organizationId) throws Throwable {
        MessageHook messageHook = get(messageHookId, organizationId);

        Broker currentBroker = messageHook.getBroker();
        if (null == currentBroker) {
            throw new ClientException("Message hook does not have a remote broker associated");
        }

        if (null != broker.getEndpoint()) {
            currentBroker.setEndpoint(broker.getEndpoint());
        }

        if (null != broker.getUsername()) {
            currentBroker.setUsername(broker.getUsername());
        }

        if (null != broker.getPassword()) {
            currentBroker.setPassword(broker.getPassword());
        }

        messageHook.setModifiedOn(new Date());
        return messageHookRepository.save(messageHook);
    }

    public PasswordResponse getBrokerPassword(String messageHookId, String organizationId) throws Throwable {
        Broker broker = get(messageHookId, organizationId).getBroker();

        if (null == broker) {
            throw new ClientException("Message hook does not have a remote broker associated");
        }

        return new PasswordResponse(broker.getPassword());
    }

    @Async
    public CompletableFuture<Boolean> asyncAllExist(Set<String> messageHookIds, String organizationId) {
        return CompletableFuture.completedFuture(allExist(messageHookIds, organizationId));
    }

    public Boolean allExist(Set<String> messageHookIds, String organizationId) {
        return messageHookRepository.countByIdInAndOrganizationId(messageHookIds, organizationId) == messageHookIds.size();
    }

    public Boolean exists(String messageHookId, String organizationId) {
        return messageHookRepository.existsByIdAndOrganizationId(messageHookId, organizationId);
    }

    private Boolean nameExists(String name, String organizationId) {
        return messageHookRepository.existsByNameAndOrganizationId(name, organizationId);
    }
}
