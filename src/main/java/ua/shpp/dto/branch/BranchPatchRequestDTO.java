package ua.shpp.dto.branch;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BranchPatchRequestDTO(
        @Schema(description = "New name of the branch", example = "Updated branch name")
        @NotBlank
        @Size(min = 6, max = 30, message = "The length of the branch name must be between 6-30 characters")
        String name
) {
}
