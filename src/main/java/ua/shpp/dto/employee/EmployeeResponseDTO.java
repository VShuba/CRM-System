package ua.shpp.dto.employee;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record EmployeeResponseDTO(@JsonProperty("employee_id") Long id,
                                  String name, String email, String phone,
                                  @JsonProperty("branch_id") Long branchId,
                                  List<EmployeeServicesResponseDTO> services,
                                  @JsonProperty("base64_avatar") String base64Avatar) {
}