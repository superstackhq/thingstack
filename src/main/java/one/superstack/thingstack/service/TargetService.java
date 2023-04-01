package one.superstack.thingstack.service;

import one.superstack.thingstack.enums.TargetType;
import one.superstack.thingstack.exception.ClientException;
import one.superstack.thingstack.pojo.Target;
import one.superstack.thingstack.pojo.TargetReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TargetService {

    private final ThingTypeService thingTypeService;

    private final ThingService thingService;

    @Autowired
    public TargetService(ThingTypeService thingTypeService, ThingService thingService) {
        this.thingTypeService = thingTypeService;
        this.thingService = thingService;
    }

    public Boolean exists(TargetType targetType, String targetId, String organizationId) {
        switch (targetType) {
            case THING -> {
                return thingService.exists(targetId, organizationId);
            }

            case THING_TYPE -> {
                return thingTypeService.exists(targetId, organizationId);
            }

            default -> throw new ClientException("Invalid target type");
        }
    }

    public Target get(TargetReference targetReference) throws Throwable {
        switch (targetReference.getType()) {
            case THING_TYPE -> {
                return new Target(TargetType.THING_TYPE, targetReference.getId(), thingTypeService.get(targetReference.getId()));
            }

            case THING -> {
                return new Target(TargetType.THING, targetReference.getId(), thingService.get(targetReference.getId()));
            }

            default -> throw new ClientException("Invalid target type");
        }
    }
}
