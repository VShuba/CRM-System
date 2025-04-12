package ua.shpp.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ua.shpp.dto.OrganizationRequestDTO;
import ua.shpp.dto.OrganizationResponseDTO;
import ua.shpp.entity.Organization;
import ua.shpp.exception.OrganizationNotFound;
import ua.shpp.repository.OrganizationRepository;

@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository repository;

    public ResponseEntity<OrganizationResponseDTO> create(OrganizationRequestDTO organizationRequestDTO) {

        // check before save !!! how to check before?

        Organization organization = Organization.builder()
                .name(organizationRequestDTO.name())
                .build();

        return new ResponseEntity<>(toOrgDTO(organization), HttpStatus.CREATED);
    }

    public ResponseEntity<OrganizationResponseDTO> get(Long orgID) {

        Organization organization = repository.findById(orgID).orElseThrow(
                () -> new OrganizationNotFound("Failed to find organization in DB."));

        return new ResponseEntity<>(toOrgDTO(organization), HttpStatus.OK);
    }

    public ResponseEntity<OrganizationResponseDTO> update(Long orgID, String newName) {

        Organization organization = repository.findById(orgID).orElseThrow(
                () -> new OrganizationNotFound("Failed to find organization in DB."));

        // check if allowed name of organization?

        organization.setName(newName);

        return new ResponseEntity<>(toOrgDTO(organization), HttpStatus.OK);
    }

    public void delete(Long orgID) {

        Organization organization = repository.findById(orgID).orElseThrow(
                () -> new OrganizationNotFound("Can not delete it. Cause failed to find organization in DB."));

        repository.delete(organization);
    }

    private OrganizationResponseDTO toOrgDTO(Organization organization) {
        return new OrganizationResponseDTO(organization.getId(), organization.getName());
    }

}
