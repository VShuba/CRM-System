package ua.shpp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "Request for creating or updating a service")
public record ServiceRequestDTO(

        @Schema(description = "Service name", example = "Haircut")
        @NotBlank(message = "Service name must not be blank")
        String name,

        @Schema(description = "HEX color code for the service", example = "#FF5733")
        @NotBlank(message = "Color must not be blank")
        String color,

        @Schema(description = "ID of the branch this service belongs to", example = "1")
        @NotNull(message = "Branch ID is required")
        Long branchId,

        // new List комнат
        @Schema(description = "IDs of rooms used for this service", example = "[1, 2, 3]")
        List<Long> roomIds,

        // new List сотрудников
        @Schema(description = "IDs of employees who provide this service", example = "[4, 5]")
        List<Long> employeeIds
) {
}
