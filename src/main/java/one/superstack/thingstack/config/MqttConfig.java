package one.superstack.thingstack.config;

import one.superstack.thingstack.subscriber.RootMessageSubscriber;
import one.superstack.thingstack.util.TopicUtil;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.mqtt.inbound.Mqttv5PahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.Mqttv5PahoMessageHandler;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Configuration
public class MqttConfig {

    private final RootMessageSubscriber rootMessageSubscriber;

    private final String mqttUsername;

    private final String mqttPassword;

    private final String mqttEndpoint;

    @Autowired
    public MqttConfig(RootMessageSubscriber rootMessageSubscriber,
                      @Value("${mqtt.broker.username}") String mqttUsername,
                      @Value("${mqtt.broker.password}") String mqttPassword,
                      @Value("${mqtt.broker.endpoint}") String mqttEndpoint) {
        this.rootMessageSubscriber = rootMessageSubscriber;
        this.mqttUsername = mqttUsername;
        this.mqttPassword = mqttPassword;
        this.mqttEndpoint = mqttEndpoint;
    }

    @Bean
    public IntegrationFlow mqttInbound() {

        return IntegrationFlow.from(
                        new Mqttv5PahoMessageDrivenChannelAdapter(getConnectionOptions(),
                                UUID.randomUUID().toString(),
                                TopicUtil.ROOT_TOPIC + "/#"))
                .handle(rootMessageSubscriber::handleMessage)
                .get();
    }

    @Bean
    public IntegrationFlow mqttOutbound() {
        Mqttv5PahoMessageHandler handler = new Mqttv5PahoMessageHandler(getConnectionOptions(), UUID.randomUUID().toString());
        handler.setAsync(true);
        handler.setAsyncEvents(true);
        handler.setDefaultQos(2);
        return f -> f.channel("mqttOutboundChannel").handle(handler);
    }

    private MqttConnectionOptions getConnectionOptions() {
        MqttConnectionOptions mqttConnectionOptions = new MqttConnectionOptions();
        mqttConnectionOptions.setUserName(mqttUsername);
        mqttConnectionOptions.setPassword(mqttPassword.getBytes(StandardCharsets.UTF_8));
        mqttConnectionOptions.setAutomaticReconnect(true);
        mqttConnectionOptions.setServerURIs(new String[]{mqttEndpoint});
        return mqttConnectionOptions;
    }
}
