package ua.shpp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record OrganizationRequestDTO(
        @Size(min = 6, max = 30, message = "The length of the organization name should be between 6-30 characters")
        @NotNull
        @NotBlank
        String name
) {
}
