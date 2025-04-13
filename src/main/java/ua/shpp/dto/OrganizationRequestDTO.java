package ua.shpp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request organization")
public record OrganizationRequestDTO(
        @Schema(description = "Name of the organization", example = "My Company")
        @Size(min = 6, max = 30, message = "The length of the organization name should be between 6-30 characters")
        @NotBlank
        String name
) {
}
