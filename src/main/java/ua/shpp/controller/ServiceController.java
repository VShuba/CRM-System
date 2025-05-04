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
import ua.shpp.dto.ServiceRequestDTO;
import ua.shpp.dto.ServiceResponseDTO;
import ua.shpp.service.ServiceService;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
@Tag(name = "Services", description = "Service management API")
public class ServiceController {

    private final ServiceService serviceService;

    @Operation(summary = "Create new service", description = "Creates a new service and returns its DTO")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Service created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ServiceResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
    @PostMapping
    @PreAuthorize("@authz.hasRoleInOrgByBranchId(#requestDTO.branchId(), T(ua.shpp.model.OrgRole).ADMIN)")
    public ResponseEntity<ServiceResponseDTO> createService(
            @RequestBody @Valid ServiceRequestDTO requestDTO) {
        return new ResponseEntity<>(serviceService.create(requestDTO), HttpStatus.CREATED);
    }

    @Operation(summary = "Get service by ID", description = "Returns service DTO by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ServiceResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Service not found", content = @Content)
    })
    @GetMapping("/{id}")
    @PreAuthorize("@authz.hasRoleByServiceId(#id, T(ua.shpp.model.OrgRole).ADMIN)")
    public ResponseEntity<ServiceResponseDTO> getService(@PathVariable Long id) {
        return new ResponseEntity<>(serviceService.get(id), HttpStatus.OK);
    }

    @Operation(
            summary = "Get all services by branch ID",
            description = "Returns a paginated list of services for the specified branch"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Paginated list of services retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ServiceResponseDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid branch ID", content = @Content),
            @ApiResponse(responseCode = "404", description = "Branch not found", content = @Content)
    })
    @GetMapping("/branches/{branchId}")
    @PreAuthorize("@authz.hasRoleInOrgByBranchId(#branchId, T(ua.shpp.model.OrgRole).ADMIN)")
    public ResponseEntity<Page<ServiceResponseDTO>> getAllServices(
            @PathVariable Long branchId,
            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(serviceService.getAll(branchId, pageable));
    }

    @Operation(summary = "Update service", description = "Updates a service by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ServiceResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "404", description = "Service not found", content = @Content)
    })
    @PutMapping("/{id}")
    @PreAuthorize("@authz.hasRoleByServiceId(#id, T(ua.shpp.model.OrgRole).ADMIN)" +
            " and @authz.hasRoleInOrgByBranchId(#requestDTO.branchId, T(ua.shpp.model.OrgRole).ADMIN)")
    public ResponseEntity<ServiceResponseDTO> updateService(
            @PathVariable Long id,
            @RequestBody @Valid ServiceRequestDTO requestDTO) {
        return new ResponseEntity<>(serviceService.update(id, requestDTO), HttpStatus.OK);
    }

    @Operation(summary = "Delete service", description = "Deletes a service by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Service deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Service not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("@authz.hasRoleByServiceId(#id, T(ua.shpp.model.OrgRole).ADMIN)")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        serviceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
