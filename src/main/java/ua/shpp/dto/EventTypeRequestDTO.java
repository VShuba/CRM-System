package ua.shpp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema(description = "Request to event_type")
public record EventTypeRequestDTO(
        @Schema(description = "Event type name", example = "Group")
        @Size(min = 3, max = 50, message = "The event type name must be between 3 and 50 characters long")
        @NotBlank
        String name,

        @Schema(description = "Branch id this event type belongs to", example = "1")
        @NotNull
        Long branchId,

        @Schema(description = "List of one-time services Id", example = "[1,2]")
        List<Long> oneTimeVisits,

        @Schema(description = "List of subscriptions Id", example = "[1,2]")
        List<Long> subscriptions
) {
}
