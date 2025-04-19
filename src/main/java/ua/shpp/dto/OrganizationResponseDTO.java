package ua.shpp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import ua.shpp.dto.branch.BranchShortResponseDTO;

import java.util.List;

@Schema(description = "Response organization")
public record OrganizationResponseDTO(
        Long id,
        @Schema(description = "Name of organization", example = "My Company")
        String name,

        @Schema(description = "List of branches")
        List<BranchShortResponseDTO> branches
) {
}
