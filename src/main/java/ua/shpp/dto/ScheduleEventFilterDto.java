package ua.shpp.dto;


import io.swagger.v3.oas.annotations.media.Schema;

public record ScheduleEventFilterDto(
        @Schema(description = "Room id", example = "1")
        Long roomId,
        @Schema(description = "Employee id", example = "1")
        Long employeeId,
        @Schema(description = "Event type id", example = "1")
        Long eventTypeId,
        @Schema(description = "Service id", example = "1")
        Long serviceId) {
}
