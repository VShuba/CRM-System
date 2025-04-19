package ua.shpp.dto.branch;

import java.time.DayOfWeek;

public record WorkingHourDTO(
        DayOfWeek dayOfWeek,
        String startTime,
        String endTime,
        boolean dayOff
) {
}
