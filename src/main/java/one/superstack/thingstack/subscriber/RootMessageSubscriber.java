package one.superstack.thingstack.subscriber;

import one.superstack.thingstack.util.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

@Component
public class RootMessageSubscriber {

    private static final Logger LOGGER = LoggerFactory.getLogger(RootMessageSubscriber.class);

    public void handleMessage(Message<?> message) {
        MessageHeaders headers = message.getHeaders();

        if (headers.isEmpty()) {
            LOGGER.error("No headers present in message, skipping");
        }

        Object topic = headers.get(MqttHeaders.RECEIVED_TOPIC);

        if (null == topic) {
            LOGGER.error("No topic present in message, skipping");
        }

        byte[] payload = (byte[]) message.getPayload();

        System.out.println("Topic: " + topic);
        System.out.println("Payload: " + new String(payload));
    }
}
