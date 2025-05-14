package ua.shpp.dto.employee;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

public record EmployeeServiceCreateDTO(
        @Schema(description = "Service name", example = "Haircut")
        @Size(min = 3, max = 50, message = "The service name must be 3-50 characters long")
        String name,

        @Schema(description = "HEX color code for the service", example = "#FF5733")
        @Size(min = 3, max = 10, message = "The HEX color code must be 3-10 characters long")
        String color) {
}