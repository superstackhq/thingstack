package one.superstack.thingstack.repository;

import one.superstack.thingstack.model.WebHook;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface WebHookRepository extends MongoRepository<WebHook, String> {

    Optional<WebHook> findByIdAndOrganizationId(String id, String organizationId);

    List<WebHook> findByOrganizationId(String organizationId, Pageable pageable);

    List<WebHook> findByIdIn(List<String> ids);

    Boolean existsByNameAndOrganizationId(String name, String organizationId);

    Boolean existsByIdNotAndNameAndOrganizationId(String id, String name, String organizationId);

    Boolean existsByIdAndOrganizationId(String id, String organizationId);

    Integer countByIdInAndOrganizationId(Set<String> ids, String organizationId);
}
