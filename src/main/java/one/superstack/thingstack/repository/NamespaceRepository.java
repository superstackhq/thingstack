package one.superstack.thingstack.repository;

import one.superstack.thingstack.model.Namespace;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NamespaceRepository extends MongoRepository<Namespace, String> {

    List<Namespace> findByParentAndThingTypeIdAndOrganizationId(List<String>  parent, String thingTypeId, String organizationId, Pageable pageable);

    Boolean existsByThingTypeIdAndParentAndNameAndOrganizationId(String thingTypeId, List<String>  parent, String name, String organizationId);
}
