package ua.shpp.dto.branch;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Short response of Branch which includes id and name")
public record BranchShortResponseDTO(

        @Schema(description = "Id of branch", example = "1")
        Long id,

        @Schema(description = "Name of branch", example = "My Branch")
        String name

) {
}
