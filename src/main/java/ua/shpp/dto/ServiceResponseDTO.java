package ua.shpp.dto;

public record ServiceResponseDTO(
        Long id,
        String name,
        String color,
        String branchName,
        String roomName
) {
}