package ua.shpp.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = WorkingHourValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidWorkingHours {
    String message() default "Invalid working hours";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
