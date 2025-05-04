package ua.shpp.dto.employee;

import com.fasterxml.jackson.annotation.JsonProperty;

public record EmployeeResponseDTO(@JsonProperty("employee_id") Long id,
                                  String name, String email, String phone,
                                  @JsonProperty("branch_id") Long branchId,
                                  @JsonProperty("base64_avatar") String base64Avatar) {
}