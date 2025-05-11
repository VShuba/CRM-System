package ua.shpp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ua.shpp.dto.OrganizationModerationDTO;
import ua.shpp.service.OrganizationModerationService;

import java.util.List;

@RestController
@RequestMapping("/admin/organizations")
@RequiredArgsConstructor
@Tag(name = "Organizations (moderation)", description = "Endpoints for pre-moderation of organizations (ADMIN access)")
public class OrganizationModerationController {
    private final OrganizationModerationService moderationService;

    @Operation(summary = "Get all organizations for moderation",
            description = "Returns a list of all organizations that require pre-moderation, " +
                    "including their access status")
    @ApiResponse(responseCode = "200", description = "Successfully received list",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = OrganizationModerationDTO.class)))
    @GetMapping
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public List<OrganizationModerationDTO> getAll() {
        return moderationService.getAllForModeration();
    }

    @Operation(summary = "Delete organization along with access",
            description = "Deletes both the organization and its associated access permissions by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "The organization has been deleted"),
            @ApiResponse(responseCode = "404", description = "Organization not found")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<Void> delete(@Parameter(description = "Organization ID") @PathVariable Long id) {
        moderationService.deleteOrganization(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Enable or disable access to an organization",
            description = "Toggles the access permission (enabled/disabled) for the specified organization")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Access changed"),
            @ApiResponse(responseCode = "404", description = "Organization not found")
    })
    @PatchMapping("/{id}/toggle-access")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<Void> toggleAccess(@Parameter(description = "Organization ID") @PathVariable Long id) {
        moderationService.toggleAccess(id);
        return ResponseEntity.ok().build();
    }
}
