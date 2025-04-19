package ua.shpp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.shpp.dto.branch.BranchPatchRequestDTO;
import ua.shpp.dto.branch.BranchRequestDTO;
import ua.shpp.dto.branch.BranchResponseDTO;
import ua.shpp.dto.branch.WorkingHourDTO;
import ua.shpp.service.BranchService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/organizations/{orgId}/branches")
@RequiredArgsConstructor
@Tag(name = "Branches", description = "Operations related to organization branches")
public class BranchController {

    private final BranchService branchService;

    @Operation(summary = "Create a new branch", description =
            "Creates a new branch under a specific organization and returns its DTO")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Branch created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BranchResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
    @PostMapping
    public ResponseEntity<BranchResponseDTO> createBranch(
            @PathVariable Long orgId,
            @RequestBody @Valid BranchRequestDTO requestDTO) {
        BranchResponseDTO createdBranch = branchService.create(orgId, requestDTO);

        URI location = URI.create("/api/organizations/" + orgId + "/branches/" + createdBranch.id());

        return ResponseEntity.created(location).body(createdBranch);
    }

    @Operation(summary = "Get a branch by ID", description = "Returns a single branch by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Branch found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BranchResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Branch not found", content = @Content)
    })
    @GetMapping("/{branchId}")
    public ResponseEntity<BranchResponseDTO> getBranch(
            @PathVariable Long orgId,
            @PathVariable Long branchId) {
        return new ResponseEntity<>(branchService.get(orgId, branchId), HttpStatus.OK);
    }

    @Operation(summary = "Update branch name", description = "Updates the name of an existing branch")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Branch updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BranchResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Branch not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
    @PatchMapping("/{branchId}")
    public ResponseEntity<BranchResponseDTO> updateBranch(
            @PathVariable Long orgId,
            @PathVariable Long branchId,
            @RequestBody @Valid BranchPatchRequestDTO requestDTO) {
        return new ResponseEntity<>(branchService.updateName(orgId, branchId, requestDTO), HttpStatus.OK);
    }

    @PatchMapping("/{branchId}/working-hours")
    public ResponseEntity<BranchResponseDTO> updateWorkingHours(
            @PathVariable Long orgId,
            @PathVariable Long branchId,
            @RequestBody @Valid List<@Valid WorkingHourDTO> requestDTO) {
        return ResponseEntity.ok(branchService.updateWorkingHours(orgId, branchId, requestDTO));
    }

    @Operation(summary = "Delete a branch", description = "Deletes a branch by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Branch deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Branch not found", content = @Content)
    })
    @DeleteMapping("/{branchId}")
    public ResponseEntity<Void> deleteBranch(
            @PathVariable Long orgId,
            @PathVariable Long branchId) {
        branchService.delete(orgId, branchId);
        return ResponseEntity.noContent().build();
    }
}
