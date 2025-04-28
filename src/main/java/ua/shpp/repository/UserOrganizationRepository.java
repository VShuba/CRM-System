package ua.shpp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.shpp.entity.UserOrganization;
import ua.shpp.entity.UserOrganizationId;
import ua.shpp.model.OrgRole;

@Repository
public interface UserOrganizationRepository extends JpaRepository<UserOrganization, UserOrganizationId> {
    boolean existsByUserIdAndOrganizationIdAndRole(Long userId, Long organizationId, OrgRole role);
}