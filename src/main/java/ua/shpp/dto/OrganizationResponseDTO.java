package ua.shpp.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response organization")
public record OrganizationResponseDTO(
        Long id,
        @Schema(description = "Name of organization", example = "My Company")
        String name) {
}
