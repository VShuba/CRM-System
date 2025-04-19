package ua.shpp.constraints;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ua.shpp.dto.branch.WorkingHourDTO;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class WorkingHourValidator implements ConstraintValidator<ValidWorkingHours, WorkingHourDTO> {

    @Override
    public boolean isValid(WorkingHourDTO dto, ConstraintValidatorContext context) {
        if (dto.dayOff()) {
            return true;
        }

        if (dto.startTime() == null || dto.endTime() == null) {
            return false;
        }

        try {
            LocalTime start = LocalTime.parse(dto.startTime());
            LocalTime end = LocalTime.parse(dto.endTime());
            return start.isBefore(end);
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}

