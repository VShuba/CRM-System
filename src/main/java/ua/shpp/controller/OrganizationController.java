package ua.shpp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ua.shpp.dto.OrganizationRequestDTO;
import ua.shpp.dto.OrganizationResponseDTO;
import ua.shpp.service.OrganizationService;

@RestController
@RequestMapping("/api/organizations")
@RequiredArgsConstructor
@Tag(name = "Organizations", description = "Organization management API")
public class OrganizationController {

    private final OrganizationService organizationService;

    @Operation(summary = "Create new organization", description = "Returns the created organization as a DTO")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The organization was created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrganizationResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "409", description = "An organization with this name already exists",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<OrganizationResponseDTO> createOrganization(
            @RequestBody @Valid OrganizationRequestDTO requestDTO) {
        return new ResponseEntity<>(organizationService.create(requestDTO), HttpStatus.CREATED);
    }

    @Operation(summary = "Get organization by ID", description = "Returns the organization as a DTO")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Organization found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrganizationResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Organization not found", content = @Content)
    })
    @GetMapping("/{id}")
    @PreAuthorize("@authz.hasRoleInOrgByOrgId(#id, T(ua.shpp.model.OrgRole).ADMIN)")
    public ResponseEntity<OrganizationResponseDTO> getOrganization(@PathVariable Long id) {
        return new ResponseEntity<>(organizationService.get(id), HttpStatus.OK);
    }

    @Operation(
            summary = "Get all organizations",
            description = "Returns a paginated list of all organizations"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Paginated list of organizations retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrganizationResponseDTO.class))
            )
    })
    @GetMapping
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<Page<OrganizationResponseDTO>> getAllOrganizations(
            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(organizationService.getAll(pageable));
    }

    @Operation(summary = "Update organization", description = "Updates organization name by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Organization updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrganizationResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Organization not found", content = @Content),
            @ApiResponse(responseCode = "409", description = "An organization with this name already exists",
                    content = @Content)
    })
    @PatchMapping("/{id}")
    @PreAuthorize("@authz.hasRoleInOrgByOrgId(#id, T(ua.shpp.model.OrgRole).ADMIN)")
    public ResponseEntity<OrganizationResponseDTO> updateOrganization(
            @PathVariable Long id,
            @RequestBody @Valid OrganizationRequestDTO requestDTO) {
        return new ResponseEntity<>(organizationService.update(id, requestDTO), HttpStatus.OK);
    }

    @Operation(summary = "Delete organization", description = "Deletes an organization by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Organization successfully deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "Organization not found", content = @Content)
    })

    @DeleteMapping("/{id}")
    @PreAuthorize("@authz.hasRoleInOrgByOrgId(#id, T(ua.shpp.model.OrgRole).ADMIN)")
    public ResponseEntity<Void> deleteOrganization(@PathVariable Long id) {
        organizationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
