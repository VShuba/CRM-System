package ua.shpp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ua.shpp.model.OrgRole;
import ua.shpp.repository.UserOrganizationRepository;

@Slf4j
@Component("authz") // чтобы обращаться как @authz в @PreAuthorize
@RequiredArgsConstructor
public class AuthorizationService {

    private final UserOrganizationRepository userOrganizationRepository;
    private final UserService userService;

    public boolean hasRoleInOrg(Long organizationId, String expectedRole) {
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//
//        if (!(principal instanceof UserEntity user)) {
//            log.warn("Principal is not a UserEntity");
//            return false;
//        }


        OrgRole requiredRole;
        try {
            requiredRole = OrgRole.valueOf(expectedRole);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid role: {}", expectedRole);
            return false;
        }

        Long userId = userService.getCurrentUserId();

        return userOrganizationRepository.existsByUserIdAndOrganizationIdAndRole(
                userId,
                organizationId,
                requiredRole
        );
    }
}