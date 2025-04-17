package ua.shpp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record EventTypeResponseDTO(
        @Schema(description = "Id of event type", example = "1")
        Long id,

        @Schema(description = "Event type name", example = "Групові")
        @Size(min = 3, max = 50, message = "Довжина назви типу події має становити від 3 до 50 символів")
        @NotBlank
        String name,

        @Schema(description = "Список разових послуг")
        List<OneTimeOfferDTO> oneTimeVisits,

        @Schema(description = "Список абонементів")
        List<SubscriptionOfferDTO> subscriptions
) {
}
