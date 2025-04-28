package ua.shpp.service;

import org.springframework.stereotype.Service;
import ua.shpp.dto.UserOrganizationDTO;
import ua.shpp.dto.UserOrganizationIdDTO;
import ua.shpp.entity.UserOrganization;
import ua.shpp.entity.UserOrganizationId;
import ua.shpp.exception.UserOrganizationNotFoundException;
import ua.shpp.model.OrgRole;
import ua.shpp.repository.UserOrganizationRepository;

@Service
public class UserOrganizationService {


    private final UserOrganizationRepository userOrganizationRepository;

    public UserOrganizationService(UserOrganizationRepository userOrganizationRepository) {
        this.userOrganizationRepository = userOrganizationRepository;
    }

    public UserOrganizationDTO changeUserOrganizationRole(
            Long userId, Long organizationId, OrgRole role
    ) {
        UserOrganizationId userOrganizationId = new UserOrganizationId(
                userId,
                organizationId
        );

        UserOrganization userOrganization
                = userOrganizationRepository.findById(userOrganizationId)
                .orElseThrow(() -> new UserOrganizationNotFoundException("User Organization Not Found"));
        userOrganization.setRole(role);
        userOrganizationRepository.save(userOrganization);
        return new UserOrganizationDTO(new UserOrganizationIdDTO(
                userOrganizationId.getUserId(),
                userOrganizationId.getOrganizationId()
        ), userOrganization.getRole());
    }

    public void deleteUserOrganizationRole(Long organizationId, Long userId) {
        UserOrganizationId userOrganizationId = new UserOrganizationId(
                userId,
                organizationId
        );
        userOrganizationRepository.findById(userOrganizationId)
                .orElseThrow(() -> new UserOrganizationNotFoundException("User Organization Not Found"));
        userOrganizationRepository.deleteById(userOrganizationId);
    }


}
