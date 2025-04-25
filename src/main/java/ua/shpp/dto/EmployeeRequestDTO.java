package ua.shpp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public record EmployeeRequestDTO(@JsonProperty("branch_id") Long branchId, String name, String email, String phone,
                                 @JsonProperty("existing_services_ids") Set<Long> existingServicesIds,
                                 @JsonProperty("new_services") Set<EmployeeServiceCreateDTO> newServicesDTO) {
}