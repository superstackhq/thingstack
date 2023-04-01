package one.superstack.thingstack.repository;

import one.superstack.thingstack.model.Reflection;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReflectionRepository extends MongoRepository<Reflection, String> {

    Optional<Reflection> findByThingIdAndOrganizationId(String thingId, String organizationId);
}
