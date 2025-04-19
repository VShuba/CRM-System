package ua.shpp.dto.branch;

import io.swagger.v3.oas.annotations.media.Schema;
import ua.shpp.constraints.ValidWorkingHours;

import java.time.DayOfWeek;

@ValidWorkingHours
public record WorkingHourDTO(

        @Schema(description = "Day of week")
        DayOfWeek dayOfWeek,

        @Schema(description = "Start time", example = "12:00")
        String startTime,

        @Schema(description = "End time", example = "18:00")
        String endTime,

        @Schema(description = "Day off", example = "true")
        boolean dayOff
) {
}
