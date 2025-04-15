package ua.shpp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request to branch")
public record BranchRequestDTO(

        @Schema(description = "Id of organization", example = "1")
        Long organizationId,

        @Schema(description = "Name of the branch", example = "Central branch")
        @Size(min = 6, max = 30, message = "The length of the branch should be between 6-30 characters")
        @NotBlank
        String name
) {
}
