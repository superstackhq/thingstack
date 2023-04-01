package one.superstack.thingstack.service;

import one.superstack.thingstack.model.Namespace;
import one.superstack.thingstack.repository.NamespaceRepository;
import one.superstack.thingstack.request.NamespaceFetchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NamespaceService {

    private final NamespaceRepository namespaceRepository;

    @Autowired
    public NamespaceService(NamespaceRepository namespaceRepository) {
        this.namespaceRepository = namespaceRepository;
    }

    public void register(List<String> namespace, String thingTypeId, String organizationId) {
        if (null == namespace || namespace.isEmpty()) {
            return;
        }

        List<String> parent = namespace.subList(0, namespace.size() - 1);

        if (parent.isEmpty()) {
            parent = null;
        }

        String name = namespace.get(namespace.size() - 1);

        if (exists(parent, name, thingTypeId, organizationId)) {
            return;
        }

        Namespace n = new Namespace(thingTypeId, parent, name, organizationId);
        n = namespaceRepository.save(n);

        if (null != n.getParent()) {
            register(parent, thingTypeId, organizationId);
        }
    }

    public List<Namespace> list(NamespaceFetchRequest namespaceFetchRequest, String thingTypeId, String organizationId, Pageable pageable) {
        return namespaceRepository.findByParentAndThingTypeIdAndOrganizationId(namespaceFetchRequest.getParent(), thingTypeId, organizationId, pageable);
    }

    private Boolean exists(List<String> parent, String name, String thingTypeId, String organizationId) {
        return namespaceRepository.existsByThingTypeIdAndParentAndNameAndOrganizationId(thingTypeId,
                parent, name, organizationId);
    }
}
