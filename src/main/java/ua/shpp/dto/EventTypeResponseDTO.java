package ua.shpp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record EventTypeResponseDTO(
        @Schema(description = "Id of event type", example = "1")
        Long id,

        @Schema(description = "Event type name", example = "Group")
        @Size(min = 3, max = 50, message = "The event type name must be between 3 and 50 characters long")
        @NotBlank
        String name,

        @Schema(description = "Branch name", example = "Office 1")
        String branchName,

        @Schema(description = "List of one-time services id")
        List<Long> oneTimeVisits,

        @Schema(description = "List of subscriptions id")
        List<Long> subscriptions
) {
}
