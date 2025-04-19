package ua.shpp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request to one-time offer")
public record OneTimeOfferDTO(
        @Schema(description = "Service id", example = "1")
        Long id,
        @Schema(description = "Service id", example = "1")
        Long activityId,
        @Schema(description = "Event type id", example = "1")
        Long eventTypeId,
        @Schema(description = "Duration in minutes", example = "60")
        @Size(min = 1, max = 500, message = "Duration must be between 1 and 500 minutes")
        @NotBlank(message = "Duration cannot be blank")
        Long durationInMinutes,
        @Schema(description = "Price", example = "100")
        @Size(min = 1, max = 1000000, message = "Price must be between 1 and 1_000_000.")
        @NotBlank(message = "Price cannot be blank")
        Long price
) {
}
