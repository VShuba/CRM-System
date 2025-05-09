package ua.shpp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.shpp.entity.OrganizationAccessEntity;

import java.util.List;

public interface OrganizationAccessRepository extends JpaRepository<OrganizationAccessEntity, Long> {
    List<OrganizationAccessEntity> findAllByOrderByOrganization_NameAsc();
}
