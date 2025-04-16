package ua.shpp.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record RoomResponseDTO(
        Long id,
        @Schema(description = "Name of room")
        String name
) {
}
