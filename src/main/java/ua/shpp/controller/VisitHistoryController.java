package ua.shpp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.shpp.dto.VisitHistoryDTO;
import ua.shpp.service.history.VisitHistoryService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/history_visit")
@Tag(name = "Visit History", description = "API for retrieving customer visit history")
public class VisitHistoryController {
    private final VisitHistoryService visitHistoryService;

    @Operation(summary = "Get visit history for a client",
            description = "Retrieves the full list of visit history records associated with the specified client ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved visit history",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = VisitHistoryDTO.class)))),
            @ApiResponse(responseCode = "404",
                    description = "Client not found (if you implement client existence check)", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/clients/{clientId}")
    @PreAuthorize("@authz.hasRoleInOrgByClientId(#clientId, T(ua.shpp.model.OrgRole).ADMIN)")
    public ResponseEntity<List<VisitHistoryDTO>> getClientVisitHistory(@PathVariable Long clientId) {
        log.debug("Received request to get visit history for client ID: {}", clientId);
        List<VisitHistoryDTO> history = visitHistoryService.getVisitHistoryByClientId(clientId);
        log.info("Returning {} history entries for client ID: {}", history.size(), clientId);
        return ResponseEntity.ok(history);
    }
}
