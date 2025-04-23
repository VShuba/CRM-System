package ua.shpp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

public record ClientRequestDto(
        @NotBlank
        @Length(message = "Name size should be from 8 to 32 characters", min = 8, max = 32)
        String name,

        @NotBlank(message = "Phone number should not be blank")
        @Pattern(message = "Wrong phone format", regexp = "^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$")
        String phone,

        @Past(message = "Birthday cannot be in future")
        LocalDate birthday,

        String comment) {
}