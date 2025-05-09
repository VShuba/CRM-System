package ua.shpp.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Organization information for moderation")
public record OrganizationModerationDTO(
        @Schema(description = "Organization ID", example = "1")
        Long id,

        @Schema(description = "Name of the organization", example = "Freak Dance")
        String name,

        @Schema(description = "Is access allowed", example = "true")
        boolean accessAllowed,

        @Schema(description = "List of organization branches")
        List<BranchForModerationDTO> branches
) {
}
