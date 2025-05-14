package ua.shpp.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema(description = "Request & response to subscription offer")
public record SubscriptionOfferDTO(

        @Schema(description = " Offer id", example = "1")
        Long id,
        @Schema(description = "Offer name", example = "Complex 1")
        @Size(min = 3, max = 50, message = "The offer name must be 3-50 characters long")
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

