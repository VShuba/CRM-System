package ua.shpp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
@Schema(description = "Response to visit a scheduled deal")
public record VisitDealDTO(
        @Schema(description = "Deal id", example = "1")
        Long dealId,
        @Schema(description = "Schedule id", example = "1")
        Long scheduleId) {
}
