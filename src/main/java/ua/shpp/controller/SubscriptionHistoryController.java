package ua.shpp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ua.shpp.dto.SubscriptionHistoryDTO;
import ua.shpp.service.history.SubscriptionHistoryService;

import java.util.List;

@RestController
@RequestMapping("/api/subscription-history")
@RequiredArgsConstructor
@Tag(name = "Subscription History", description = "API for viewing customer subscription history")
public class SubscriptionHistoryController {

    private final SubscriptionHistoryService subscriptionHistoryService;

    @Operation(summary = "Get a filtered history of a customer's subscriptions",
            description = "Returns a list of valid or invalid client subscriptions from history by" +
                    " the `isValid` parameter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subscription list successfully retrieved",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SubscriptionHistoryDTO[].class))),
            @ApiResponse(responseCode = "400", description = "Invalid query (e.g. missing 'isValid' parameter)",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "No client with the specified ID was found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/clients/{clientId}/filter")
    @PreAuthorize("@authz.hasRoleInOrgByClientId(#clientId, T(ua.shpp.model.OrgRole).ADMIN)")
    public ResponseEntity<List<SubscriptionHistoryDTO>> getClientSubscriptionHistory(
            @Parameter(description = "Client ID", required = true, example = "1")
            @PathVariable Long clientId,
            @Parameter(description = "Filter by validity (true - valid, false - invalid)",
                    required = true, example = "true")
            @RequestParam Boolean isValid) {

        List<SubscriptionHistoryDTO> history =
                subscriptionHistoryService.getSubscriptionHistoryByClientAndValidity(clientId, isValid);
        return ResponseEntity.ok(history);
    }

    @Operation(summary = "Get a client's entire subscription history",
            description = "Returns a complete list of all client subscriptions from history (valid and invalid)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully received full list of subscriptions",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SubscriptionHistoryDTO[].class))),
            @ApiResponse(responseCode = "404", description = "No client with the specified ID was found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/clients/{clientId}")
    @PreAuthorize("@authz.hasRoleInOrgByClientId(#clientId, T(ua.shpp.model.OrgRole).ADMIN)")
    public ResponseEntity<List<SubscriptionHistoryDTO>> getAllClientSubscriptionHistory(
            @Parameter(description = "Client ID", required = true, example = "1")
            @PathVariable Long clientId) {

        List<SubscriptionHistoryDTO> history = subscriptionHistoryService.getAllSubscriptionHistoryByClient(clientId);
        return ResponseEntity.ok(history);
    }
}
