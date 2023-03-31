package one.superstack.thingstack.service;

import one.superstack.thingstack.enums.TargetType;
import one.superstack.thingstack.pojo.Target;
import one.superstack.thingstack.pojo.TargetReference;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class TargetService {

    public Boolean exists(TargetType targetType, String targetId, String organizationId) {
        // TODO
        return true;
    }

    public Target get(TargetReference targetReference) {
        // TODO
        return null;
    }

    public List<Target> fetch(List<TargetReference> targetReferences) {
        // TODO
        return null;
    }
}
