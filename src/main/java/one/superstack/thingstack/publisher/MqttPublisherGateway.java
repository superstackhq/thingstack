package one.superstack.thingstack.publisher;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;

@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
public interface MqttPublisherGateway {

    void send(@Header(MqttHeaders.TOPIC) String topic, String data);
}
