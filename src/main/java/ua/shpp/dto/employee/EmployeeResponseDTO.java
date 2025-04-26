package ua.shpp.dto.employee;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record EmployeeResponseDTO(@JsonProperty("branch_id") Long branchId, @JsonProperty("employee_id") Long id,
                                  String name, String email, String phone,
                                  @JsonProperty("base64_avatar") String base64Avatar,
                                  @JsonProperty("service_ids") List<Long> serviceIds) {
}