package ua.shpp.dto.employee;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.Set;

@Builder
public record EmployeeUpdateRequestDTO(String name, String email, String phone,
                                       @JsonProperty("existing_services_ids") Set<Long> existingServicesIds,
                                       @JsonProperty("new_services") Set<EmployeeServiceCreateDTO> newServicesDTO) {
}