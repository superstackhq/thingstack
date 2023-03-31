package one.superstack.thingstack.repository;

import one.superstack.thingstack.model.Thing;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThingRepository extends MongoRepository<Thing, String> {

}
