package ua.shpp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.shpp.dto.OrganizationRequestDTO;
import ua.shpp.dto.OrganizationResponseDTO;
import ua.shpp.entity.Organization;
import ua.shpp.entity.UserEntity;
import ua.shpp.entity.UserOrganization;
import ua.shpp.entity.UserOrganizationId;
import ua.shpp.exception.OrganizationAlreadyExists;
import ua.shpp.exception.OrganizationNotFound;
import ua.shpp.mapper.OrganizationEntityToOrganizationDTOMapper;
import ua.shpp.model.Role;
import ua.shpp.repository.OrganizationRepository;
import ua.shpp.repository.UserOrganizationRepository;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository repository;
    private final UserService userService;
    private final UserOrganizationRepository userOrganizationRepository;

    private final OrganizationEntityToOrganizationDTOMapper mapper;

    public OrganizationResponseDTO create(OrganizationRequestDTO organizationRequestDTO) {

        if (repository.existsByName(organizationRequestDTO.name())) {
            // checking before save in DB
            throw new OrganizationAlreadyExists("Organization with name '"
                    + organizationRequestDTO.name() +
                    "' already exists.");
        }

        Organization organization = Organization.builder()
                .name(organizationRequestDTO.name())
                .build();

        Organization savedOrganization = repository.save(organization);

        UserEntity currentUser = userService.getCurrentUser();

        UserOrganization userOrganization = UserOrganization.builder()
                .id(new UserOrganizationId(currentUser.getId(), organization.getId()))
                .organization(savedOrganization)
                .user(currentUser)
                .joinedAt(LocalDate.now())
                .role(Role.OWNER) // <- даємо OWNER'a тому хто створює організацію
                .build();

        userOrganizationRepository.save(userOrganization);

        return mapper.organizationEntityToOrganizationResponseDTO(organization);
    }

    public OrganizationResponseDTO get(Long orgID) {

        Organization organization = repository.findById(orgID).orElseThrow(
                () -> new OrganizationNotFound("Failed to find organization in DB."));

        return mapper.organizationEntityToOrganizationResponseDTO(organization);
    }

    Organization getEntityById(Long orgId) {
        return repository.findById(orgId).orElseThrow(
                () -> new OrganizationNotFound("Failed to find organization in DB."));
    }

    public OrganizationResponseDTO update(Long orgID, OrganizationRequestDTO organizationRequestDTO) {

        Organization organization = repository.findById(orgID).orElseThrow(
                () -> new OrganizationNotFound("Failed to find organization in DB."));

        if (repository.existsByName(organizationRequestDTO.name()) // Check for duplicate in DB
                && !organization.getName().equalsIgnoreCase(organizationRequestDTO.name())) {
            throw new OrganizationAlreadyExists(
                    "Organization with name '" + organizationRequestDTO.name() + "' already exists.");
        }

        organization.setName(organizationRequestDTO.name());

        repository.save(organization);

        return mapper.organizationEntityToOrganizationResponseDTO(organization);
    }

    public void delete(Long orgID) {

        Organization organization = repository.findById(orgID).orElseThrow(
                () -> new OrganizationNotFound("Can not delete it. Cause failed to find organization in DB."));

        repository.delete(organization);
    }


}
