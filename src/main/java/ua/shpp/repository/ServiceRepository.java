package ua.shpp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.shpp.entity.ServiceEntity;

import java.util.List;

public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {
    @Query("SELECT s.name FROM ServiceEntity s")
    Page<String> findAllServiceNames(Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT service_name FROM services WHERE service_name IN (:names) AND branch_id = :branchId")
    List<String> findAllServiceNamesByNamesAndBranch(List<String> names, Long branchId);
}