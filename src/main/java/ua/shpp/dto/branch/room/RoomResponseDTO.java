package ua.shpp.dto.branch.room;

import io.swagger.v3.oas.annotations.media.Schema;

public record RoomResponseDTO(
        @Schema(description = "Id of room", example = "1")
        Long id,
        @Schema(description = "Name of room", example = "Main room")
        String name
) {
}
