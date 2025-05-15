package ua.shpp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record OneTimeDealRequestDto(
        @Schema(description = "Client id", example = "1")
        @NotNull
        Long clientId,
        @Schema(description = "Offer id", example = "1")
        Long oneTimeId,
        @Schema(description = "One-time visit used", example = "false")
        Boolean visitUsed) {

}