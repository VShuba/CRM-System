package ua.shpp.dto.employee;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.Set;

@Builder
public record EmployeeRequestDTO(
        @Size(min = 3, max = 50, message = "The employee name must be 3-50 characters long")
        @Schema(description = "Employee name", example = "Peter")
        String name,

        @Email
        @Schema(description = "Email of the employee", example = "peter@example.com")
        String email,

        @Pattern(message = "Wrong phone format",
                regexp = "^\\+?3?8?[-\\s\\(]?(0\\d{2})[-\\s\\)]?\\s?\\d{3}[-\\s]?\\d{2}[-\\s]?\\d{2}$")
        @Schema(description = "Employee phone number", example = "+380991112233")
        String phone,

        @Schema(description = "ID of existing services", example = "[1]")
        @JsonProperty("existing_services_ids")
        Set<Long> existingServicesIds,

        @Schema(description = "New services for creation")
        @JsonProperty("new_services")
        Set<EmployeeServiceCreateDTO> newServicesDTO) {
}