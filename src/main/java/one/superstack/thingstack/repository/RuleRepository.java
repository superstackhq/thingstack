package one.superstack.thingstack.repository;

import one.superstack.thingstack.model.Rule;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RuleRepository extends MongoRepository<Rule, String> {

    List<Rule> findByOrganizationId(String organizationId, Pageable pageable);

    Optional<Rule> findByIdAndOrganizationId(String id, String organizationId);

    Boolean existsByNameAndOrganizationId(String name, String organizationId);

    Boolean existsByIdNotAndNameAndOrganizationId(String id, String name, String organizationId);

    Boolean existsByIdAndOrganizationId(String id, String organizationId);
}
