package ua.shpp.dto;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

import java.time.LocalDate;

public record ClientResponseDto(
        @CsvBindByName(column = "id", required = true)
        @CsvBindByPosition(position = 0)
        Long id,
        @CsvBindByName(column = "Ім’я", required = true)
        @CsvBindByPosition(position = 1)
        String name,
        @CsvBindByName(column = "Телефон", required = true)
        @CsvBindByPosition(position = 2)
        String phone,
        @CsvBindByName(column = "День народження", required = true)
        @CsvBindByPosition(position = 3)
        LocalDate birthday,
        @CsvBindByName(column = "Коментар", required = true)
        @CsvBindByPosition(position = 4)
        String comment) {
}