package ua.shpp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import ua.shpp.model.ClientEventStatus;

public record EventClientDto(
        @Schema(description = "Customer ID", example = "1")
        @NotNull
        Long clientId,

        @Schema(description = "The identifier of the event in which the client is participating (event schedule)",
                example = "1")
        @NotNull
        Long scheduleId,

        @Schema(description = "Client participation status in the event", example = "ASSIGNED")
        ClientEventStatus clientEventStatus,

        @Schema(description = "Purchased single visit ID (if any)", example = "1")
        Long oneTimeInfoId,

        @Schema(description = "Purchased subscription ID (if applicable)", example = "1")
        Long subscriptionInfoId
) {
}
