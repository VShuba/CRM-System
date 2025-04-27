package ua.shpp.dto;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response valid offer")
public record ValidVisitsDealDto(
        @Schema(description = "Subscription or one-time id", example = "1")
        Long id,
        @Schema(description = "Valid", example = "true")
        Boolean valid,
        @Schema(description = "Visits >= 0", example = "5")
        Integer visits) {
}
