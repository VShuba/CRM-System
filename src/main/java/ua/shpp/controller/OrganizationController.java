package ua.shpp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.shpp.dto.OrganizationRequestDTO;
import ua.shpp.dto.OrganizationResponseDTO;
import ua.shpp.service.OrganizationService;

@RestController
@RequestMapping("/api/organization")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    @PostMapping("/")
    public ResponseEntity<OrganizationResponseDTO> createOrganization(
            @RequestBody @Valid OrganizationRequestDTO requestDTO) {
        return organizationService.create(requestDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizationResponseDTO> getOrganization(@PathVariable Long id) {
        return organizationService.get(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<OrganizationResponseDTO> updateOrganization(
            @PathVariable Long id,
            @RequestBody @Valid OrganizationRequestDTO requestDTO) {
        return organizationService.update(id, requestDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrganization(@PathVariable Long id) {
        organizationService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
