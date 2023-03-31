package one.superstack.thingstack.repository;

import one.superstack.thingstack.model.MqttAcl;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MqttAclRepository extends MongoRepository<MqttAcl, String> {

    Optional<MqttAcl> findByUsername(String username);
}
