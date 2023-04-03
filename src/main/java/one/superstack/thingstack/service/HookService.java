package one.superstack.thingstack.service;

import one.superstack.thingstack.embedded.HookReference;
import one.superstack.thingstack.pojo.Hook;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class HookService {

    public Boolean allExist(Set<HookReference> hookReferences, String organizationId) {
        // TODO
        return true;
    }

    public Boolean exists(HookReference hookReference, String organizationId) {
        // TODO
        return true;
    }

    public List<Hook> fetch(Set<HookReference> hookReferences) {
        // TODO
        return null;
    }
}
