package one.superstack.thingstack.repository;

import one.superstack.thingstack.model.Thing;
import one.superstack.thingstack.model.ThingType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ThingTypeRepository extends MongoRepository<ThingType, String> {

    List<ThingType> findByOrganizationId(String organizationId, Pageable pageable);

    Optional<ThingType> findByIdAndOrganizationId(String id, String organizationId);

    Boolean existsByNameAndVersion(String name, String version);
}
