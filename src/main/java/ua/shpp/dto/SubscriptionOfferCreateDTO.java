package ua.shpp.dto;


import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Request to create subscription offer")
public record SubscriptionOfferCreateDTO(
        @Schema(description = "Offer name", example = "Complex 1")
        String name,
        @Schema(description = "Event type id", example = "1")
        Long eventTypeId,
        @Schema(description = "List of services id", example = "[1,2]")
        List<Long> activitiesId,
        @Schema(description = "Number of visits", example = "5")
        Integer visits,
        @Schema(description = "Validity period in days", example = "30")
        Integer termOfValidityInDays,
        @Schema(description = "Price", example = "1000")
        Long price) {
}

