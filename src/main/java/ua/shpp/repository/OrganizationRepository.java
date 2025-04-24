package ua.shpp.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ua.shpp.entity.Organization;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    boolean existsByName(String name);

    Page<Organization> findAll(@NonNull Pageable pageable);
}
