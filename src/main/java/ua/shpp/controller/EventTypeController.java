package ua.shpp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
import ua.shpp.dto.EventTypeRequestDTO;
import ua.shpp.dto.EventTypeResponseDTO;
import ua.shpp.service.EventTypeService;

import java.util.List;

@RestController
@RequestMapping("/api/event_types")
@RequiredArgsConstructor
@Tag(name = "Event Types", description = "API for managing event types")
public class EventTypeController {

    private final EventTypeService eventTypeService;

    @Operation(summary = "Create event type",
            description = "Creates a new type of event, including one-time and subscription services tied to a branch")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Event type successfully created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EventTypeResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Incorrect data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Branch not found for the specified branchId",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "An event type with this name already exists",
                    content = @Content)
    })
    @PostMapping
    @PreAuthorize("@authz.hasRoleInOrgByBranchId(#dto.branchId(), T(ua.shpp.model.OrgRole).ADMIN)")
    public ResponseEntity<EventTypeResponseDTO> create(@RequestBody @Valid EventTypeRequestDTO dto) {
        EventTypeResponseDTO response = eventTypeService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Get all event types by branch ID",
            description = "Returns all event types that belong to a specific branch")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of event types returned",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = EventTypeResponseDTO.class))))
    })
    @GetMapping("/branch/{branchId}")
    @PreAuthorize("@authz.hasRoleInOrgByBranchId(#branchId, T(ua.shpp.model.OrgRole).ADMIN)")
    public ResponseEntity<List<EventTypeResponseDTO>> getAllByBranch(@PathVariable Long branchId) {
        List<EventTypeResponseDTO> response = eventTypeService.getAllByBranch(branchId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get event type by ID (useful when copying)",
            description = "Returns the event type by ID. Can be used to copy data when creating a new type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event type found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EventTypeResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Event type not found", content = @Content)
    })
    @GetMapping("/{id}")
    @PreAuthorize("@authz.hasRoleInOrgByEventTypeId(#id, T(ua.shpp.model.OrgRole).ADMIN)")
    public ResponseEntity<EventTypeResponseDTO> getById(@PathVariable Long id) {
        EventTypeResponseDTO response = eventTypeService.getById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update event type",
            description = "Updates the event type with a new name, services, and branch link")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event type updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EventTypeResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Incorrect data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Event type not found", content = @Content),
            @ApiResponse(responseCode = "409", description = "New event type name already exists", content = @Content)
    })
    @PutMapping("/{id}")
    @PreAuthorize("@authz.hasRoleInOrgByEventTypeId(#id, T(ua.shpp.model.OrgRole).ADMIN) && " +
            "@authz.hasRoleInOrgByBranchId(#dto.branchId(), T(ua.shpp.model.OrgRole).ADMIN)")
    public ResponseEntity<EventTypeResponseDTO> update(@PathVariable Long id,
                                                       @RequestBody @Valid EventTypeRequestDTO dto) {
        EventTypeResponseDTO response = eventTypeService.update(id, dto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete event type",
            description = "Deletes the event type by ID. If the ID is not found, returns an error")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "The event type has been deleted"),
            @ApiResponse(responseCode = "404", description = "Event type not found")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("@authz.hasRoleInOrgByEventTypeId(#id, T(ua.shpp.model.OrgRole).ADMIN)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        eventTypeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get filtered and paginated event types",
            description = "Returns event types filtered by optional name, branchId or serviceId. Supports pagination and sorting.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Page of event types returned",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EventTypeResponseDTO.class)))
    })
    @GetMapping
    @PreAuthorize("(@branchId != null && @authz.hasRoleInOrgByBranchId(#branchId, T(ua.shpp.model.OrgRole).ADMIN)) || " +
            "(#serviceId != null && @authz.hasRoleByServiceId(#serviceId, T(ua.shpp.model.OrgRole).ADMIN))")
    public ResponseEntity<Page<EventTypeResponseDTO>> getFiltered(
            @Parameter(description = "Filter by event type name (optional)")
            @RequestParam(required = false) String name,

            @Parameter(description = "Filter by branch ID (optional)")
            @RequestParam(required = false) Long branchId,

            @Parameter(description = "Filter by service ID (optional, either one-time or subscription)")
            @RequestParam(required = false) Long serviceId,

            @ParameterObject Pageable pageable) {

        Page<EventTypeResponseDTO> page = eventTypeService.getFiltered(name, branchId, serviceId, pageable);
        return ResponseEntity.ok(page);
    }
}
