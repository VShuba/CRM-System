package ua.shpp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ua.shpp.dto.OrganizationRequestDTO;
import ua.shpp.dto.OrganizationResponseDTO;
import ua.shpp.entity.Organization;
import ua.shpp.exception.OrganizationAlreadyExists;
import ua.shpp.exception.OrganizationNotFound;
import ua.shpp.mapper.OrganizationEntityToOrganizationDTOMapper;
import ua.shpp.repository.OrganizationRepository;

@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository repository;

    private final OrganizationEntityToOrganizationDTOMapper mapper;

    public ResponseEntity<OrganizationResponseDTO> create(OrganizationRequestDTO organizationRequestDTO) {

        if (repository.existsByName(organizationRequestDTO.name())) {
            // checking before save in DB
            throw new OrganizationAlreadyExists("Organization with name '"
                    + organizationRequestDTO.name() +
                    "' already exists.");
        }

        Organization organization = Organization.builder()
                .name(organizationRequestDTO.name())
                .build();

        repository.save(organization);

        return new ResponseEntity<>(mapper.organizationEntityToOrganizationResponseDTO(organization), HttpStatus.CREATED);
    }

    public ResponseEntity<OrganizationResponseDTO> get(Long orgID) {

        Organization organization = repository.findById(orgID).orElseThrow(
                () -> new OrganizationNotFound("Failed to find organization in DB."));

        return new ResponseEntity<>(mapper.organizationEntityToOrganizationResponseDTO(organization), HttpStatus.OK);
    }

    public ResponseEntity<OrganizationResponseDTO> update(Long orgID, OrganizationRequestDTO organizationRequestDTO) {

        Organization organization = repository.findById(orgID).orElseThrow(
                () -> new OrganizationNotFound("Failed to find organization in DB."));

        if (repository.existsByName(organizationRequestDTO.name()) // Check for duplicate in DB
                && !organization.getName().equalsIgnoreCase(organizationRequestDTO.name())) {
            throw new OrganizationAlreadyExists(
                    "Organization with name '" + organizationRequestDTO.name() + "' already exists.");
        }

        organization.setName(organizationRequestDTO.name());

        repository.save(organization);

        return new ResponseEntity<>(mapper.organizationEntityToOrganizationResponseDTO(organization), HttpStatus.OK);
    }

    public void delete(Long orgID) {

        Organization organization = repository.findById(orgID).orElseThrow(
                () -> new OrganizationNotFound("Can not delete it. Cause failed to find organization in DB."));

        repository.delete(organization);
    }


}
