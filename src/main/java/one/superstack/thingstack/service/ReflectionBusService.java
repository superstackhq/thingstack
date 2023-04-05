package one.superstack.thingstack.service;

import one.superstack.thingstack.embedded.Bus;
import one.superstack.thingstack.exception.ClientException;
import one.superstack.thingstack.model.Reflection;
import one.superstack.thingstack.publisher.MqttPublisherGateway;
import one.superstack.thingstack.request.BusCustomTopicMessageRequest;
import one.superstack.thingstack.response.BusCustomTopicMessageResponse;
import one.superstack.thingstack.util.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReflectionBusService {

    private final ReflectionService reflectionService;

    private final MqttPublisherGateway mqttPublisherGateway;

    @Autowired
    public ReflectionBusService(ReflectionService reflectionService, MqttPublisherGateway mqttPublisherGateway) {
        this.reflectionService = reflectionService;
        this.mqttPublisherGateway = mqttPublisherGateway;
    }

    public BusCustomTopicMessageResponse publishMessageToCustomPublishTopic(BusCustomTopicMessageRequest busCustomTopicMessageRequest, String reflectionId, String organizationId) throws Throwable {
        Reflection reflection = reflectionService.get(reflectionId, organizationId);
        String customTopic = busCustomTopicMessageRequest.getTopic();
        Bus reflectionBus = reflection.getBus();

        if (!reflectionBus.getCustomPublishTopics().contains(customTopic) && !reflectionBus.getCustomPubSubTopics().contains(customTopic)) {
            throw new ClientException("Custom topic not found");
        }

        mqttPublisherGateway.send(customTopic, Json.encode(busCustomTopicMessageRequest.getMessage()));
        return new BusCustomTopicMessageResponse(true);
    }
}
