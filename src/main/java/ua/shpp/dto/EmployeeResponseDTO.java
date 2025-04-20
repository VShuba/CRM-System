package ua.shpp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record EmployeeResponseDTO(@JsonProperty("branch_id") Long branchId,
                                  @JsonProperty("employee_id") Long id,
                                  @JsonProperty("first_name") String firstName,
                                  @JsonProperty("last_name") String lastName, String email, String phone,
                                  @JsonProperty("base64_avatar") String base64Avatar,
                                  @JsonProperty("services_ids") List<Long> servicesIds) {
}