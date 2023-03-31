package one.superstack.thingstack.repository;

import one.superstack.thingstack.model.MqttUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MqttUserRepository extends MongoRepository<MqttUser, String> {

    Optional<MqttUser> findByUsername(String username);
}
