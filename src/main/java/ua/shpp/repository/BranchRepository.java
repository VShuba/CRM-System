package ua.shpp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.shpp.entity.BranchEntity;

@Repository
public interface BranchRepository extends JpaRepository<BranchEntity, Long> {
    boolean existsByName(String name);

    boolean existsByNameAndOrganizationId(String name, Long organizationId);

    Page<BranchEntity> findAllByOrganizationId(Long orgId, Pageable pageable);

    @Query("SELECT b.organization.id FROM BranchEntity b WHERE b.id = :branchId")
    Long findOrganizationIdByBranchId(@Param("branchId") Long branchId);
}
