package ua.shpp.dto;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request to subscription offer")
public record SubscriptionOfferDTO(
        @Schema(description = "Offer id", example = "1")
        Long id,
        @Schema(description = "Offer name", example = "Dance")
        String name,
//                                   List<> activity,
        @Schema(description = "Number of visits", example = "5")
        Integer visits,
        @Schema(description = "Validity period in days", example = "30")
        Integer termOfValidityInDays,
        @Schema(description = "Price", example = "1000")
        Long price) {
}

