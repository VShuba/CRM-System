package ua.shpp.dto.branch.room;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record RoomRequestDTO(
        @Schema(description = "Name of room", example = "Main room")
        @Length(min = 3, max = 30, message = "The length of the room name should be between 3-30 characters")
        @NotBlank
        String name
) {
}
