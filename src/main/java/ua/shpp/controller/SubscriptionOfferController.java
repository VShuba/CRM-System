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
import ua.shpp.dto.OneTimeOfferDTO;
import ua.shpp.dto.SubscriptionOfferCreateDTO;
import ua.shpp.dto.SubscriptionOfferDTO;
import ua.shpp.service.SubscriptionOfferService;

import java.net.URI;

@RestController
@RequestMapping("/api/offer/subscription")
@RequiredArgsConstructor
@Tag(name = "SubscriptionOffer", description = "Subscription offer management API")
public class SubscriptionOfferController {
    private final SubscriptionOfferService subscriptionOfferService;

    @Operation(summary = "Create subscription offer type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Subscription offer successfully created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SubscriptionOfferDTO.class))),
            @ApiResponse(responseCode = "404", description = "Service or Event type id not fount", content = @Content),
    })
    @PostMapping
    @PreAuthorize("@authz.hasRoleInOrgByEventTypeId(#subscriptionOfferDTO.eventTypeId(), T(ua.shpp.model.OrgRole).ADMIN)")
    public ResponseEntity<SubscriptionOfferDTO> create(
            @RequestBody SubscriptionOfferCreateDTO subscriptionOfferDTO) {
        var offer = subscriptionOfferService.create(subscriptionOfferDTO);
        URI location = URI.create("/api/offer/subscription/" + offer.id());

        return ResponseEntity.created(location).body(offer);
    }

    @Operation(summary = "Get subscription offer by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subscription offer successfully find",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OneTimeOfferDTO.class))),
            @ApiResponse(responseCode = "404", description = "Subscription offer id not fount", content = @Content),
    })
    @GetMapping("/{id}")
    @PreAuthorize("@authz.hasRoleSubscriptionOfferId(#id, T(ua.shpp.model.OrgRole).ADMIN)")
    public ResponseEntity<SubscriptionOfferDTO> getById(@PathVariable Long id) {
        var dto = subscriptionOfferService.getById(id);
        return ResponseEntity.ok(dto);
    }


    @Operation(summary = "Get all subscription offers for a given Event Type ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subscription offers for a given Event Type ID successfully found",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = SubscriptionOfferDTO.class)))),
    })
    @GetMapping
    @PreAuthorize("@authz.hasRoleInOrgByEventTypeId(#eventTypeId, T(ua.shpp.model.OrgRole).ADMIN)")
    public ResponseEntity<Page<SubscriptionOfferDTO>> getAllByEventTypeId(
            @Parameter(description = "ID of the Event Type to filter offers", required = true)
            @RequestParam(defaultValue = "1") Long eventTypeId,
            @ParameterObject() Pageable pageRequest
    ) {
        Page<SubscriptionOfferDTO> result = subscriptionOfferService
                .getAllByEventTypeId(eventTypeId, pageRequest);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Update subscription  offer by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subscription offer successfully updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OneTimeOfferDTO.class))),
            @ApiResponse(responseCode = "404", description = "Subscription offer, Service, Event type id not fount", content = @Content),
    })
    @PatchMapping
    @PreAuthorize("@authz.hasRoleSubscriptionOfferId(#subscriptionOfferDTO.id(), T(ua.shpp.model.OrgRole).ADMIN)")
    public ResponseEntity<SubscriptionOfferDTO> update(
            @RequestBody SubscriptionOfferDTO subscriptionOfferDTO) {
        var service = subscriptionOfferService.update(subscriptionOfferDTO);
        URI location = URI.create("/api/offer/subscription/" + service.id());

        return ResponseEntity
                .ok()
                .header("Location", location.toString())
                .body(service);
    }

    @Operation(summary = "Delete a subscription offer by its id",
            description = "Delete a subscription offer by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted successfully"),
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("@authz.hasRoleSubscriptionOfferId(#id, T(ua.shpp.model.OrgRole).ADMIN)")
    public ResponseEntity<SubscriptionOfferDTO> delete(@PathVariable Long id) {
        subscriptionOfferService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
