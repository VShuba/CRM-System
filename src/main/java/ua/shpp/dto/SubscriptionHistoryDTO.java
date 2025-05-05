package ua.shpp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;

@Schema(description = "History of valid and invalid subscriptions")
public record SubscriptionHistoryDTO(
        @Schema(description = "Unique identifier of the subscription", example = "1")
        Long id,

        @Schema(description = "Identifier of the client associated with this subscription", example = "1")
        @NonNull
        Long clientId,

        @Schema(description = "Name of the subscription", example = "Mini")
        @NonNull
        String name,

        @Schema(description = "Type of the event associated with the subscription", example = "Group")
        @NonNull
        String eventType,

        @Schema(description = "Total number of visits included in the subscription", example = "5")
        @NonNull
        Integer totalVisits,

        @Schema(description = "Number of visits remaining on the subscription", example = "2")
        @NonNull
        Integer visitsLeft,

        @Schema(description = "Validity status of the subscription (true if valid, false if invalid)", example = "true")
        @NonNull
        Boolean isValid
) {
}
