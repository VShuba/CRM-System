package ua.shpp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ua.shpp.dto.*;
import ua.shpp.model.PaymentMethod;
import ua.shpp.service.DealService;

import java.net.URI;

@RestController
@RequestMapping("/api/deal")
@RequiredArgsConstructor
@Tag(name = "Deal", description = "Deal service management API")
public class DealController {
    private final DealService dealService;

    @Operation(summary = "Create one-time deal")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "One-time deal successfully created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OneTimeDealResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Client, Service or Event type id not fount", content = @Content),
    })
    @PostMapping("/one-time/{paymentMethod}")
    @PreAuthorize("@authz.hasRoleInOrgByClientId(#dto.clientId(), T(ua.shpp.model.OrgRole).ADMIN)")
    public ResponseEntity<OneTimeDealResponseDto> create(
            @RequestBody OneTimeDealRequestDto dto,
            @PathVariable("paymentMethod") PaymentMethod paymentMethod) {
        var service = dealService.createOneTime(dto, paymentMethod);
        URI location = URI.create("/api/deal/one-time/" + service.id());

        return ResponseEntity.created(location).body(service);
    }

    @Operation(summary = "Get one-time deal by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "One-time deal successfully find",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OneTimeDealResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "One-time deal id not fount", content = @Content),
    })
    @GetMapping("/one-time/{id}") // todo preAuth
    @PreAuthorize("@authz.hasRoleInOrgByOneTimeDealId(#id, T(ua.shpp.model.OrgRole).ADMIN)")
    public ResponseEntity<OneTimeDealResponseDto> getOneTimeById(@PathVariable Long id) {
        var dto = dealService.getOneTimeById(id);
        return ResponseEntity.ok(dto);
    }

    @Deprecated
    @Operation(summary = "Use by one-time deal id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "One-time deal successfully used",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OneTimeDealResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "One-time deal id not fount", content = @Content),
            @ApiResponse(responseCode = "404", description = "One-time visit id already used", content = @Content),
    })
    @GetMapping("/use/{id}") // todo if it`s deprecated - maybe delete it?
    public ResponseEntity<OneTimeDealResponseDto> useOneTimeById(@PathVariable Long id) {
        var dto = dealService.visitOneTimeById(id);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Use by one-time deal by id and schedule event id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "One-time deal successfully used",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OneTimeDealResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "One-time deal id not fount", content = @Content),
            @ApiResponse(responseCode = "404", description = "One-time visit id already used", content = @Content),
    })
    @GetMapping("/use/{oneId}/schedule/{scheduleId}") // TODO HOW TO preauth
    public ResponseEntity<OneTimeDealResponseDto> useOneTimeById(@PathVariable("oneId") Long oneTimeId,
                                                                 @PathVariable("scheduleId") Long scheduleId) {
        var dto = dealService.visitOneTimeByIdAndScheduleEventId(oneTimeId, scheduleId);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Create subscription offer deal")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Subscription deal successfully created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SubscriptionDealResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Client, Service or Event type id not fount", content = @Content),
    })
    @PostMapping("/subscription/{paymentMethod}")
    @PreAuthorize("@authz.hasRoleInOrgByClientId(#dto.clientId(), T(ua.shpp.model.OrgRole).ADMIN)")
    public ResponseEntity<SubscriptionDealResponseDto> create(
            @RequestBody SubscriptionDealRequestDto dto,
            @PathVariable("paymentMethod") PaymentMethod paymentMethod) {
        var service = dealService.createSubscription(dto, paymentMethod);
        URI location = URI.create("/api/deal/subscription/" + service.id());

        return ResponseEntity.created(location).body(service);
    }

    @Operation(summary = "Get subscription deal by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subscription deal successfully find",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SubscriptionDealResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Subscription deal id not fount", content = @Content),
    })
    @GetMapping("/subscription/{id}")
    @PreAuthorize("@authz.hasRoleInOrgBySubscriptionDealId(#id, T(ua.shpp.model.OrgRole).ADMIN)")
    public ResponseEntity<SubscriptionDealResponseDto> getSubscriptionById(@PathVariable Long id) {
        var dto = dealService.getSubscriptionById(id);
        return ResponseEntity.ok(dto);
    }

    @Deprecated
    @Operation(summary = "Use subscription deal by id and schedule event id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subscription deal visit successfully added",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SubscriptionDealResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Subscription deal id not fount", content = @Content),
            @ApiResponse(responseCode = "404", description = "Subscription visit id already used", content = @Content),
    })
    @GetMapping("/visit/{id}") // todo ендпоінт /visit/{id} змінює дані, але реалізований через GET, а не POST або PATCH. Це антипаттерн з точки зору REST
    @PreAuthorize("@authz.hasRoleInOrgBySubscriptionDealId(#id, T(ua.shpp.model.OrgRole).ADMIN)")
    public ResponseEntity<SubscriptionDealResponseDto> visitSubscriptionById(@PathVariable Long id) {
        var dto = dealService.subscriptionVisitById(id);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Use subscription deal by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subscription deal visit successfully added",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SubscriptionDealResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Subscription deal id not fount", content = @Content),
            @ApiResponse(responseCode = "404", description = "Subscription visit id already used", content = @Content),
    })
    @GetMapping("/visit/{id}/schedule/{scheduleId}")
    @PreAuthorize("@authz.hasRoleBySubscriptionDealAndScheduleEventId(#id, #scheduleId, T(ua.shpp.model.OrgRole).ADMIN)")
    public ResponseEntity<SubscriptionDealResponseDto> visitSubscriptionById(
            @PathVariable("id") Long id,
            @PathVariable("scheduleId") Long scheduleId) {
        var dto = dealService.subscriptionVisitByIdAndScheduleEventId(id, scheduleId);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Get check by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Check successfully find",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CheckDto.class))),
            @ApiResponse(responseCode = "404", description = "Check id not fount", content = @Content),
    })
    @GetMapping("/check/{id}") // todo не можу придумати як прикрутити preAuth
    public ResponseEntity<CheckDto> getCheckById(@PathVariable Long id) {
        var dto = dealService.getCheckById(id);
        return ResponseEntity.ok(dto);
    }
}
