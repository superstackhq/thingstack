package one.superstack.thingstack.service;

import one.superstack.thingstack.enums.TargetType;
import one.superstack.thingstack.exception.ClientException;
import one.superstack.thingstack.pojo.Target;
import one.superstack.thingstack.pojo.TargetReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TargetService {

    private final ThingTypeService thingTypeService;

    private final ThingService thingService;

    private final RuleService ruleService;

    @Autowired
    public TargetService(ThingTypeService thingTypeService, ThingService thingService, RuleService ruleService) {
        this.thingTypeService = thingTypeService;
        this.thingService = thingService;
        this.ruleService = ruleService;
    }

    public Boolean exists(TargetType targetType, String targetId, String organizationId) {
        switch (targetType) {
            case THING -> {
                return thingService.exists(targetId, organizationId);
            }

            case THING_TYPE -> {
                return thingTypeService.exists(targetId, organizationId);
            }

            case RULE -> {
                return ruleService.exists(targetId, organizationId);
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

            case RULE -> {
                return new Target(TargetType.RULE, targetReference.getId(), ruleService.get(targetReference.getId()));
            }

            default -> throw new ClientException("Invalid target type");
        }
    }
}
