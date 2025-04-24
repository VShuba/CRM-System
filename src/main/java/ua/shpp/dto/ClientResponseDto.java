package ua.shpp.dto;

import java.time.LocalDate;

public record ClientResponseDto(
        Long id,
        String name,
        String phone,
        LocalDate birthday,
        String comment) {
}