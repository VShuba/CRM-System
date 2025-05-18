package ua.shpp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ua.shpp.dto.OneTimeOfferCreateDTO;
import ua.shpp.dto.OneTimeOfferDTO;
import ua.shpp.service.OneTimeOfferService;

import java.net.URI;

@RestController
@RequestMapping("/api/offer/one-time")
@RequiredArgsConstructor
@Tag(name = "OneTimeOffer", description = "One-time service management API")
public class OneTimeOfferController {

    private final OneTimeOfferService oneTimeOfferService;

    @Operation(summary = "Create one-time offer type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "One-time offer successfully created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OneTimeOfferDTO.class))),
            @ApiResponse(responseCode = "404", description = "Service or Event type id not fount", content = @Content),
    })
    @PostMapping
    @PreAuthorize("@authz.hasRoleByServiceId(#oneTimeOfferDTO.activityId(), T(ua.shpp.model.OrgRole).ADMIN)")
    public ResponseEntity<OneTimeOfferDTO> create(
            @RequestBody OneTimeOfferCreateDTO oneTimeOfferDTO) {
        var service = oneTimeOfferService.create(oneTimeOfferDTO);
        URI location = URI.create("/api/offer/one-time/" + service.id());

        return ResponseEntity.created(location).body(service);
    }

    @Operation(summary = "Get one-time offer by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "One-time offer successfully find",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OneTimeOfferDTO.class))),
            @ApiResponse(responseCode = "404", description = "One-time offer id not fount", content = @Content),
    })
    @GetMapping("/{id}")
    @PreAuthorize("@authz.hasRoleByOneTimeOfferId(#id, T(ua.shpp.model.OrgRole).ADMIN)")
    public ResponseEntity<OneTimeOfferDTO> getById(@PathVariable Long id) {
        var dto = oneTimeOfferService.getById(id);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Get all one-time offers for a given Event Type ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "One-time offers for a given Event Type ID successfully found",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = OneTimeOfferDTO.class)))),
    })
    @GetMapping
    @PreAuthorize("@authz.hasRoleInOrgByEventTypeId(#eventTypeId, T(ua.shpp.model.OrgRole).ADMIN)")
    public ResponseEntity<Page<OneTimeOfferDTO>> getAllByEventTypeId(@Parameter(
                                                                             description = "ID of the Event Type to filter offers",
                                                                             required = true) @RequestParam(defaultValue = "1") Long eventTypeId,
                                                                     @ParameterObject() Pageable pageRequest) {
        Page<OneTimeOfferDTO> result = oneTimeOfferService.getAllByEventTypeId(eventTypeId, pageRequest);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Update one-time offer by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "One-time offer successfully updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OneTimeOfferDTO.class))),
            @ApiResponse(responseCode = "404", description = "One-time offer, Service, Event type id not fount", content = @Content),
    })
    @PatchMapping
    @PreAuthorize("@authz.hasRoleByOneTimeOfferId(#oneTimeOfferDTO.id(), T(ua.shpp.model.OrgRole).ADMIN)")
    public ResponseEntity<OneTimeOfferDTO> update(
            @RequestBody OneTimeOfferDTO oneTimeOfferDTO) {
        var service = oneTimeOfferService.update(oneTimeOfferDTO);
        URI location = URI.create("/api/offer/one-time/" + service.id());

        return ResponseEntity
                .ok()
                .header("Location", location.toString())
                .body(service);
    }

    @Operation(summary = "Delete a one‑time offer by id",
            description = "Delete a one‑time offer by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted successfully"),
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("@authz.hasRoleByOneTimeOfferId(#id, T(ua.shpp.model.OrgRole).ADMIN)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        oneTimeOfferService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
