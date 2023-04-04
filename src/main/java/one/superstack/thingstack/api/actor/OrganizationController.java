package one.superstack.thingstack.api.actor;

import one.superstack.thingstack.auth.actor.AuthenticatedController;
import one.superstack.thingstack.model.Organization;
import one.superstack.thingstack.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1")
public class OrganizationController extends AuthenticatedController {

    private final OrganizationService organizationService;

    @Autowired
    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @GetMapping(value = "/organization")
    public Organization get() throws Throwable {
        return organizationService.get(getOrganizationId());
    }
}
