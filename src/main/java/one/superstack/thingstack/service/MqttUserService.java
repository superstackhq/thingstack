package one.superstack.thingstack.service;

import one.superstack.thingstack.exception.NotFoundException;
import one.superstack.thingstack.model.MqttUser;
import one.superstack.thingstack.repository.MqttUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.function.Supplier;

@Service
public class MqttUserService {

    private final MqttUserRepository mqttUserRepository;

    private final MongoTemplate mongoTemplate;

    @Autowired
    public MqttUserService(MqttUserRepository mqttUserRepository, MongoTemplate mongoTemplate) {
        this.mqttUserRepository = mqttUserRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public void add(String username, String password) {
        mongoTemplate.upsert(Query.query(Criteria.where("username").is(username)),
                new Update().setOnInsert("username", username)
                        .setOnInsert("password", password)
                        .setOnInsert("createdOn", new Date())
                        .set("modifiedOn", new Date()),
                MqttUser.class);
    }

    public MqttUser get(String username) throws Throwable {
        return mqttUserRepository.findByUsername(username)
                .orElseThrow((Supplier<Throwable>) () -> new NotFoundException("Mqtt user not found"));
    }

    public MqttUser changePassword(String username, String password) throws Throwable {
        MqttUser mqttUser = get(username);
        mqttUser.setPassword(password);
        mqttUser.setModifiedOn(new Date());
        return mqttUserRepository.save(mqttUser);
    }

    public MqttUser delete(String username) throws Throwable {
        MqttUser mqttUser = get(username);
        mqttUserRepository.delete(mqttUser);
        return mqttUser;
    }
}
