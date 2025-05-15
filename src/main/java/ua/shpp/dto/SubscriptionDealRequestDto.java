package ua.shpp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record SubscriptionDealRequestDto(
        @Schema(description = "Client id", example = "1")
        @NotNull
        Long clientId,
        @Schema(description = "Offer id", example = "1")
        @NotNull
        Long subscriptionId
//        @Schema(description = "Subscription visits used", example = "1")
//        Integer visits,
//        @Schema(description = "Expiration date", example = "12-04-2025", type = "string", format = "date")
//        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
//        LocalDate expirationDate
) {
}