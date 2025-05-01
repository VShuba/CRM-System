package ua.shpp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ua.shpp.entity.EmployeeEntity;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {
    @Modifying
    @Query(nativeQuery = true, value = """
            DELETE FROM employee_branch WHERE employee_id = :id AND branch_id = :branchId;
            DELETE FROM employee_service WHERE employee_id = :id
                                             AND service_id IN ( SELECT s.id FROM services s WHERE s.branch_id = :branchId );
            """)
    int unbindEmployeeFromBranch(Long id, Long branchId);
}