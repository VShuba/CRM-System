package ua.shpp.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record OneTimeInfoRequestDto(
        @Schema(description = "Deal info id", example = "1")
        Long id,
        @Schema(description = "Client id", example = "1")
        Long clientId,
        @Schema(description = "Offer id", example = "1")
        Long oneTimeId,
        @Schema(description = "One-time visit used", example = "false")
        Boolean visitUsed) {

}