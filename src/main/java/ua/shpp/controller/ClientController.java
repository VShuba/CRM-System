package ua.shpp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ua.shpp.dto.ClientRequestDto;
import ua.shpp.dto.ClientResponseDto;
import ua.shpp.service.ClientService;

import java.util.List;

@RestController
@RequestMapping("/api/organizations/{orgId}/clients")
@RequiredArgsConstructor
@Tag(name = "Clients", description = "API for managing organization customers")
public class ClientController {

    private final ClientService clientService;

    @Operation(summary = "Create a new client",
            description = "Creates a new customer in the organization by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client successfully created"),
            @ApiResponse(responseCode = "400", description = "Incorrect input data"),
            @ApiResponse(responseCode = "404", description = "Organization not found")
    })
    @PostMapping
    @PreAuthorize("@authz.hasRoleInOrgByOrgId(#orgId, T(ua.shpp.model.OrgRole).ADMIN)")
    public ResponseEntity<ClientResponseDto> createClient(
            @PathVariable(name = "orgId") Long orgId,
            @RequestBody ClientRequestDto requestDto
    ) {
        return ResponseEntity.ok(clientService.createClient(orgId, requestDto));
    }

    @Operation(summary = "Update customer details",
            description = "Updates an existing customer belonging to the organization")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client successfully updated"),
            @ApiResponse(responseCode = "400",
                    description = "Customer does not belong to organization or validation error"),
            @ApiResponse(responseCode = "404", description = "Customer or organization not found")
    })
    @PutMapping("/{clientId}")
    @PreAuthorize("@authz.hasRoleInOrgByOrgId(#orgId, T(ua.shpp.model.OrgRole).ADMIN)")
    public ResponseEntity<ClientResponseDto> updateClient(
            @Parameter(description = "Organization ID") @PathVariable Long orgId,
            @Parameter(description = "Client ID") @PathVariable Long clientId,
            @RequestBody @Valid ClientRequestDto requestDto
    ) {
        return ResponseEntity.ok(clientService.updateClient(orgId, clientId, requestDto));
    }

    @Operation(summary = "Get customer by ID",
            description = "Returns a customer by their ID if they belong to the given organization")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer found"),
            @ApiResponse(responseCode = "400", description = "The customer does not belong to an organization"),
            @ApiResponse(responseCode = "404", description = "Customer or organization not found")
    })
    @GetMapping("/{clientId}")
    @PreAuthorize("@authz.hasRoleInOrgByOrgId(#orgId, T(ua.shpp.model.OrgRole).ADMIN)")
    public ResponseEntity<ClientResponseDto> getClientById(
            @Parameter(description = "Organization ID") @PathVariable Long orgId,
            @Parameter(description = "Client ID") @PathVariable Long clientId
    ) {
        return ResponseEntity.ok(clientService.getClientById(orgId, clientId));
    }

    @Operation(summary = "Get a list of the organization's clients",
            description = "Returns a page with customers belonging to the organization")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer list successfully received"),
            @ApiResponse(responseCode = "404", description = "Organization not found")
    })
    @GetMapping
    @PreAuthorize("@authz.hasRoleInOrgByOrgId(#orgId, T(ua.shpp.model.OrgRole).ADMIN)")
    public ResponseEntity<Page<ClientResponseDto>> getClients(
            @Parameter(description = "Organization ID") @PathVariable Long orgId,
            @ParameterObject Pageable pageable
    ) {
        Page<ClientResponseDto> clients = clientService.getClientsByOrganization(orgId, pageable);
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/search")
    @PreAuthorize("@authz.hasRoleInOrgByOrgId(#orgId, T(ua.shpp.model.OrgRole).ADMIN)")
    public ResponseEntity<List<ClientResponseDto>> getClientByKeyword(@PathVariable Long orgId, @RequestParam String keyword) {
        return ResponseEntity.ok().body(clientService.getClientsByKeyword(keyword, orgId));
    }

    @Operation(summary = "Delete user by id", description = "Deletes user in organization by orgId and userId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Client successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @DeleteMapping("/{clientId}")
    @PreAuthorize("@authz.hasRoleInOrgByOrgId(#orgId, T(ua.shpp.model.OrgRole).ADMIN)")
    public ResponseEntity<Void> deleteClient(@PathVariable Long orgId, @PathVariable Long clientId) {
        clientService.delete(orgId, clientId);
        return ResponseEntity.ok().build();
    }

}
