package ua.shpp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.shpp.entity.UserOrganization;
import ua.shpp.entity.UserOrganizationId;
import ua.shpp.model.OrgRole;

@Repository
public interface UserOrganizationRepository extends JpaRepository<UserOrganization, UserOrganizationId> {

    @Query(value = """
            SELECT organization_role
            FROM user_organization
            WHERE user_id = :userId AND organization_id = :organizationId
            """, nativeQuery = true)
    OrgRole getUserRoleInOrganization(Long userId, Long organizationId);
}