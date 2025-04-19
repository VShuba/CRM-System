package ua.shpp.dto.branch;

import io.swagger.v3.oas.annotations.media.Schema;
import ua.shpp.dto.branch.room.RoomResponseDTO;

import java.util.List;

@Schema(description = "Response branch")
public record BranchResponseDTO(

        @Schema(description = "Id of branch", example = "1")
        Long id,

        @Schema(description = "Id of organization", example = "1")
        Long organizationId,

        @Schema(description = "Name of branch")
        String name,

        @Schema(description = "List of rooms")
        List<RoomResponseDTO> rooms,

        List<WorkingHourDTO> workingHours
) {
}
