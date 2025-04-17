package ua.shpp.dto;

public record ServiceRequestDTO(
        String name,
        String color,
        Long branchId,
        Long roomId
) {
}
