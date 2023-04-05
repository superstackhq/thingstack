package one.superstack.thingstack.api.reflection;

import jakarta.validation.Valid;
import one.superstack.thingstack.auth.reflection.AuthenticatedReflectionController;
import one.superstack.thingstack.request.BusCustomTopicMessageRequest;
import one.superstack.thingstack.response.BusCustomTopicMessageResponse;
import one.superstack.thingstack.service.ReflectionBusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/reflections/api/v1")
public class ReflectionBusController extends AuthenticatedReflectionController {

    private final ReflectionBusService reflectionBusService;

    @Autowired
    public ReflectionBusController(ReflectionBusService reflectionBusService) {
        this.reflectionBusService = reflectionBusService;
    }

    @PostMapping(value = "/thing/reflection/bus/topics/custom/message")
    public BusCustomTopicMessageResponse publishMessageToCustomTopic(@Valid @RequestBody BusCustomTopicMessageRequest busCustomTopicMessageRequest) throws Throwable {
        return reflectionBusService.publishMessageToCustomPublishTopic(busCustomTopicMessageRequest, getReflectionId(), getOrganizationId());
    }
}
