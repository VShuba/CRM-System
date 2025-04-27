package ua.shpp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record SubscriptionInfoResponseDto(
        @Schema(description = "Deal info id", example = "1")
        Long id,
        @Schema(description = "Client id", example = "1")
        Long clientId,
        @Schema(description = "Offer id", example = "1")
        Long subscriptionId,
        @Schema(description = "Subscription visits used", example = "1")
        byte visitsUsed,
        @Schema(description = "Expiration date",example = "12-04-2025", type = "string", format = "date")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        LocalDate expirationDate,
        @Schema(description = "Check id", example = "1")
        Long checkId) {
}