package one.superstack.thingstack.repository;

import one.superstack.thingstack.model.Thing;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ThingRepository extends MongoRepository<Thing, String> {

    Optional<Thing> findByIdAndOrganizationId(String id, String organizationId);

    List<Thing> findByTypeIdAndOrganizationId(String typeId, String organizationId, Pageable pageable);

    Boolean existsByIdAndOrganizationId(String id, String organizationId);

    Optional<Thing> findByAccessKey(String accessKey);
}
