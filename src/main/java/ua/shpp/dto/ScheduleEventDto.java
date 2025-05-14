package ua.shpp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalTime;

@Schema(description = "Request & response to schedule event")
public record ScheduleEventDto(
        Long id,
        @Schema(description = "Service ID", example = "1")
        Long serviceId,
        @Schema(example = "12-04-2025", type = "string", format = "date")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        LocalDate eventDate,
        @Schema(type = "string", example = "13:00:00", format = "time")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
        LocalTime startTime,
        @Schema(type = "string", example = "14:30:00", format = "time")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
        LocalTime endTime,
        Boolean repeatEvent,
        @Schema(type = "integer", example = "4", description = "Number of people")
        byte numberOfPeople,
        @Schema(description = "Trainer ID", example = "1")
        Long trainerId,
        @Schema(description = "Room ID", example = "1")
        Long roomId,
        @Schema(description = "Event type ID", example = "1")
        Long eventTypeId) {
}
