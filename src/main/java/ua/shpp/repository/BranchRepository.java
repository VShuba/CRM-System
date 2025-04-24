package ua.shpp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.shpp.entity.BranchEntity;

@Repository
public interface BranchRepository extends JpaRepository<BranchEntity, Long> {
    boolean existsByName(String name);

    boolean existsByNameAndOrganizationId(String name, Long organizationId);
}
