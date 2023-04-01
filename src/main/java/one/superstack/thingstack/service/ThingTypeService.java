package one.superstack.thingstack.service;

import one.superstack.thingstack.auth.actor.AuthenticatedActor;
import one.superstack.thingstack.enums.Permission;
import one.superstack.thingstack.enums.TargetType;
import one.superstack.thingstack.exception.ClientException;
import one.superstack.thingstack.exception.NotFoundException;
import one.superstack.thingstack.model.ThingType;
import one.superstack.thingstack.repository.ThingTypeRepository;
import one.superstack.thingstack.request.AccessRequest;
import one.superstack.thingstack.request.ThingTypeCreationRequest;
import one.superstack.thingstack.request.ThingTypeUpdateRequest;
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
public class ThingTypeService {

    private final ThingTypeRepository thingTypeRepository;

    private final AccessService accessService;

    @Autowired
    public ThingTypeService(ThingTypeRepository thingTypeRepository, AccessService accessService) {
        this.thingTypeRepository = thingTypeRepository;
        this.accessService = accessService;
    }

    public ThingType create(ThingTypeCreationRequest thingTypeCreationRequest, AuthenticatedActor creator) {
        if (nameExists(thingTypeCreationRequest.getName(), thingTypeCreationRequest.getVersion())) {
            throw new ClientException("Thing type " + thingTypeCreationRequest.getName() + " with version " + thingTypeCreationRequest.getVersion() + " already exists");
        }

        ThingType thingType = new ThingType(thingTypeCreationRequest.getName(),
                thingTypeCreationRequest.getVersion(),
                thingTypeCreationRequest.getDescription(),
                thingTypeCreationRequest.getProperties(),
                thingTypeCreationRequest.getActions(),
                thingTypeCreationRequest.getEvents(),
                creator.getType(),
                creator.getId(),
                creator.getOrganizationId());
        thingType = thingTypeRepository.save(thingType);

        accessService.add(new AccessRequest(TargetType.THING_TYPE, thingType.getId(), creator.getType(), creator.getId(), Set.of(Permission.ALL)), creator);
        return thingType;
    }

    public List<ThingType> list(String organizationId, Pageable pageable) {
        return thingTypeRepository.findByOrganizationId(organizationId, pageable);
    }

    public ThingType get(String thingTypeId, String organizationId) throws Throwable {
        return thingTypeRepository.findByIdAndOrganizationId(thingTypeId, organizationId)
                .orElseThrow((Supplier<Throwable>) () -> new NotFoundException("Thing type not found"));
    }

    public ThingType get(String thingTypeId) throws Throwable {
        return thingTypeRepository.findById(thingTypeId)
                .orElseThrow((Supplier<Throwable>) () -> new NotFoundException("Thing type not found"));
    }

    public ThingType update(String thingTypeId, ThingTypeUpdateRequest thingTypeUpdateRequest, String organizationId) throws Throwable {
        ThingType thingType = get(thingTypeId, organizationId);
        thingType.setDescription(thingTypeUpdateRequest.getDescription());
        thingType.setModifiedOn(new Date());
        return thingTypeRepository.save(thingType);
    }

    public ThingType delete(String thingTypeId, String organizationId) throws Throwable {
        ThingType thingType = get(thingTypeId, organizationId);
        thingTypeRepository.delete(thingType);
        accessService.deleteAllForTarget(TargetType.THING_TYPE, thingTypeId);
        return thingType;
    }

    public Boolean exists(String thingTypeId, String organizationId) {
        return thingTypeRepository.existsByIdAndOrganizationId(thingTypeId, organizationId);
    }

    private Boolean nameExists(String name, String version) {
        return thingTypeRepository.existsByNameAndVersion(name, version);
    }
}
