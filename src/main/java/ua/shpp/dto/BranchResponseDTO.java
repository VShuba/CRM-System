package ua.shpp.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response branch")
public record BranchResponseDTO(
        @Schema(description = "Id of branch", example = "1")
        Integer id,
        @Schema(description = "Id of organization", example = "1")
        Integer organizationId,
        @Schema(description = "Name of branch")
        String name
) {
}
