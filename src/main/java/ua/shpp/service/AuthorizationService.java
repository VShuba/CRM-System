package ua.shpp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ua.shpp.model.GlobalRole;
import ua.shpp.model.OrgRole;
import ua.shpp.repository.BranchRepository;
import ua.shpp.repository.EmployeeRepository;
import ua.shpp.repository.ServiceRepository;
import ua.shpp.repository.UserOrganizationRepository;

@Slf4j
@Component("authz") // чтобы обращаться как @authz в @PreAuthorize
@RequiredArgsConstructor
public class AuthorizationService {

    private final UserOrganizationRepository userOrganizationRepository;
    private final UserService userService;
    private final BranchRepository branchRepository;
    private final EmployeeRepository employeeRepository;
    private final ServiceRepository serviceRepository;

    /*<---------------------------------------------------------------------------------->*/
    // for ORGANIZATION
    public boolean hasRoleInOrgByOrgId(Long organizationId, OrgRole requiredAccessRole) {
        Long userId = userService.getCurrentUserId();
        OrgRole currentUserRole = userOrganizationRepository.getUserRoleInOrganization(userId, organizationId);
        return currentUserRole != null && currentUserRole.hasAccessLevelTo(requiredAccessRole);
    }
    // for BRANCH
    public boolean hasRoleInOrgByBranchId(Long branchId, OrgRole requiredAccessRole) {
        Long organizationId = branchRepository.findOrganizationIdByBranchId(branchId);
        return organizationId != null && hasRoleInOrgByOrgId(organizationId, requiredAccessRole);
    }
    /*<---------------------------------------------------------------------------------->*/

    @Deprecated
    public boolean hasRoleInOrg(Long organizationId, String expectedRole) {


        // excpetedRole == currentUser.principal.getRole(SUPER_ADMIN) -> RETURN TRUE
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

    @Deprecated
    public boolean hasRoleByBranchId(Long branchId, String expectedRole) {
        OrgRole requiredRole;
        try {
            requiredRole = OrgRole.valueOf(expectedRole);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid role: {}", expectedRole);
            return false;
        }

        Long userId = userService.getCurrentUserId();

        // Найди организацию по branchId
        Long organizationId = branchRepository.findOrganizationIdByBranchId(branchId);
        if (organizationId == null) {
            return false;
        }

        return userOrganizationRepository.existsByUserIdAndOrganizationIdAndRole(userId, organizationId, requiredRole);
    }

    public boolean hasRoleByServiceId(Long serviceId, String expectedRole) {
        Long branchId = serviceRepository.findBranchIdByServiceId(serviceId);
        if (branchId == null) {
            log.warn("Branch not found for serviceId: {}", serviceId);
            return false;
        }
        return hasRoleByBranchId(branchId, expectedRole);
    }

    public boolean hasRoleInOrgByEmployeeId(Long employeeId, OrgRole requiredAccessRole) {
        log.info("employeeId: {}. requiredAccessRole: {}", employeeId, requiredAccessRole.name());
        Long branchId = employeeRepository.getBranchIdById(employeeId);

        if (branchId == null) {
            return false;
        }

        log.info("BranchId: {}", branchId);
        return hasRoleInOrgByBranchId(branchId, requiredAccessRole);
    }

    private boolean isSuperAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null &&
                authentication.getAuthorities().contains(new SimpleGrantedAuthority(GlobalRole.SUPER_ADMIN.name()));
    }
}