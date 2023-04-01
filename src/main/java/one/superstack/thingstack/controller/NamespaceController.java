package one.superstack.thingstack.controller;

import jakarta.validation.Valid;
import one.superstack.thingstack.auth.actor.AuthenticatedController;
import one.superstack.thingstack.model.Namespace;
import one.superstack.thingstack.request.NamespaceFetchRequest;
import one.superstack.thingstack.service.NamespaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
public class NamespaceController extends AuthenticatedController {

    private final NamespaceService namespaceService;

    @Autowired
    public NamespaceController(NamespaceService namespaceService) {
        this.namespaceService = namespaceService;
    }

    @PostMapping(value = "/things/types/{thingTypeId}/namespaces")
    public List<Namespace> fetch(@PathVariable String thingTypeId, @Valid @RequestBody NamespaceFetchRequest namespaceFetchRequest, Pageable pageable) {
        return namespaceService.list(namespaceFetchRequest, thingTypeId, getOrganizationId(), pageable);
    }
}
