package ua.shpp.dto.employee;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record EmployeeResponseDTO(@JsonProperty("employee_id") Long id,
                                  String name, String email, String phone,
                                  @JsonProperty("branch_ids") List<Long> branchIds,
                                  @JsonProperty("service_ids") List<Long> serviceIds,
                                  @JsonProperty("base64_avatar") String base64Avatar) {
}