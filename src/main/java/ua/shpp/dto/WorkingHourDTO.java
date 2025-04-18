package ua.shpp.dto;

import java.time.DayOfWeek;

public record WorkingHourDTO(
        DayOfWeek dayOfWeek,
        String startTime,
        String endTime,
        boolean dayOff
) {
}
