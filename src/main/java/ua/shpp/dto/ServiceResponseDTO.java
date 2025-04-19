package ua.shpp.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Set;

@Schema(description = "Response with service information")
public record ServiceResponseDTO(

        @Schema(description = "Service ID", example = "10")
        Long id,

        @Schema(description = "Service name", example = "Pool-dance")
        String name,

        @Schema(description = "Service color in HEX", example = "#AABBCC")
        String color,

        @Schema(description = "Branch name", example = "Central office")
        String branchName,

        // List комнат
        @Schema(description = "Names of rooms", example = "[\"Room 1\", \"Room 2\"]")
        Set<String> roomNames,

        // List сотрудников
        @Schema(description = "Employee names", example = "[\"Alice Johnson\", \"Bob Smith\"]")
        Set<String> employeeNames
) {
}
