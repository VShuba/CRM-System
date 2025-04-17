package ua.shpp.dto;

import io.swagger.v3.oas.annotations.media.Schema;

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

        @Schema(description = "Room name", example = "Room 3")
        String roomName
) {
}
