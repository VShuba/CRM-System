package ua.shpp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Branch of the organization")
public record BranchForModerationDTO(
        @NotNull
        @Schema(description = "Branch ID", example = "1")
        Long id,

        @Schema(description = "Branch name", example = "Central")
        @Size(min = 3, max = 50, message = "The branch name must be 3-50 characters long")
        String name,

        @Schema(description = "Branch address", example = "Volkova St., 5")
        @Size(min = 5, max = 50, message = "The branch address must be 5-50 characters long")
        String address
) {
}
