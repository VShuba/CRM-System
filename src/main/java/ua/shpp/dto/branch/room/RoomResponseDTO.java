package ua.shpp.dto.branch.room;

import io.swagger.v3.oas.annotations.media.Schema;

public record RoomResponseDTO(
        Long id,
        @Schema(description = "Name of room")
        String name
) {
}
