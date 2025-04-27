package ua.shpp.dto.employee;

import com.fasterxml.jackson.annotation.JsonProperty;

public record EmployeeDeleteRequestDTO(@JsonProperty("employeeId") Long employeeId,
                                       @JsonProperty("branch_id") Long branchId
) {
}