package ua.shpp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ua.shpp.entity.EmployeeEntity;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {
    @Modifying
    @Query(nativeQuery = true, value = """
            DELETE FROM employee_service WHERE employee_id = :id;
            DELETE FROM employee WHERE id = :id
            """)
    int deleteEmployeeAndRelatedRecords(Long id);
}