package ua.shpp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Branch of the organization")
public record BranchForModerationDTO(
        @NotNull
        @Schema(description = "Branch ID", example = "1")
        Long id,

        @Schema(description = "Branch name", example = "Central")
        String name,

        @Schema(description = "Branch address", example = "Volkova St., 5")
        String address
) {
}
