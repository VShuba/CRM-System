package ua.shpp.dto;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

import java.time.LocalDate;

public record ClientResponseDto(
        @CsvBindByName(column = "id", required = true)
        @CsvBindByPosition(position = 0)
        Long id,
        @CsvBindByName(column = "Name", required = true)
        @CsvBindByPosition(position = 1)
        String name,
        @CsvBindByName(column = "Phone", required = true)
        @CsvBindByPosition(position = 2)
        String phone,
        @CsvBindByName(column = "Birthday", required = true)
        @CsvBindByPosition(position = 3)
        LocalDate birthday,
        @CsvBindByName(column = "Comment", required = true)
        @CsvBindByPosition(position = 4)
        String comment) {
}