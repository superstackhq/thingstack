package one.superstack.thingstack.service;

import one.superstack.thingstack.enums.TopicAccess;
import one.superstack.thingstack.exception.ClientException;
import one.superstack.thingstack.exception.NotFoundException;
import one.superstack.thingstack.model.MqttAcl;
import one.superstack.thingstack.repository.MqttAclRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.function.Supplier;

@Service
public class MqttAclService {

    private final MqttAclRepository mqttAclRepository;

    private final MongoTemplate mongoTemplate;

    @Autowired
    public MqttAclService(MqttAclRepository mqttAclRepository, MongoTemplate mongoTemplate) {
        this.mqttAclRepository = mqttAclRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public void add(String username, TopicAccess topicAccess, Set<String> topics) {
        switch (topicAccess) {
            case PUBLISH -> add(username, topics, Collections.emptySet(), Collections.emptySet());
            case SUBSCRIBE -> add(username, Collections.emptySet(), topics, Collections.emptySet());
            case PUBSUB -> add(username, Collections.emptySet(), Collections.emptySet(), topics);
            default -> throw new ClientException("Invalid topic access");
        }
    }

    public void delete(String  username, TopicAccess topicAccess, Set<String> topics) {
        switch (topicAccess) {
            case PUBLISH -> delete(username, topics, Collections.emptySet(), Collections.emptySet());
            case SUBSCRIBE -> delete(username, Collections.emptySet(), topics, Collections.emptySet());
            case PUBSUB -> delete(username, Collections.emptySet(), Collections.emptySet(), topics);
            default -> throw new ClientException("Invalid topic access");
        }
    }

    public void add(String username, Set<String> publish, Set<String> subscribe, Set<String> pubsub) {
        mongoTemplate.upsert(Query.query(Criteria.where("username").is(username)),
                new Update()
                        .setOnInsert("username", username)
                        .setOnInsert("createdOn", new Date())
                        .set("modifiedOn", new Date())
                        .addToSet("publish").each(publish.toArray())
                        .addToSet("subscribe").each(subscribe.toArray())
                        .addToSet("pubsub").each(pubsub.toArray()),
                MqttAcl.class);
    }

    public void delete(String username, Set<String> publish, Set<String> subscribe, Set<String> pubsub) {
        mongoTemplate.updateFirst(Query.query(Criteria.where("username").is(username)),
                new Update()
                        .set("modifiedOn", new Date())
                        .pullAll("publish", publish.toArray())
                        .pullAll("subscribe", subscribe.toArray())
                        .pullAll("pubsub", pubsub.toArray()),
                MqttAcl.class);
    }

    public MqttAcl get(String username) throws Throwable {
        return mqttAclRepository.findByUsername(username)
                .orElseThrow((Supplier<Throwable>) () -> new NotFoundException("Mqtt ACL not found"));
    }

    public MqttAcl deleteAll(String username) throws Throwable {
        MqttAcl mqttAcl = get(username);
        mqttAclRepository.delete(mqttAcl);
        return mqttAcl;
    }
}
