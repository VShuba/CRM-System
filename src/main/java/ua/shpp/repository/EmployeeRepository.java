package ua.shpp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.shpp.entity.EmployeeEntity;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

    EmployeeEntity findByEmailAndBranchId(String email, Long branchId);

    @Query(value = "SELECT branch_id FROM employee WHERE id = :employeeId", nativeQuery = true)
    Long getBranchIdById(Long employeeId);
}