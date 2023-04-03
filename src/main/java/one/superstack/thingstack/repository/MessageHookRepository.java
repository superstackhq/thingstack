package one.superstack.thingstack.repository;

import one.superstack.thingstack.model.MessageHook;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface MessageHookRepository extends MongoRepository<MessageHook, String> {

    Optional<MessageHook> findByIdAndOrganizationId(String id, String organizationId);

    List<MessageHook> findByOrganizationId(String organizationId, Pageable pageable);

    List<MessageHook> findByIdIn(List<String> ids);

    Boolean existsByNameAndOrganizationId(String name, String organizationId);

    Boolean existsByIdNotAndNameAndOrganizationId(String id, String name, String organizationId);

    Integer countByIdInAndOrganizationId(Set<String> ids, String organizationId);

    Boolean existsByIdAndOrganizationId(String id, String organizationId);
}
