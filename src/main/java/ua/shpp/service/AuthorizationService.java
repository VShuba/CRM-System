package ua.shpp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
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

    @Deprecated
    public boolean hasRoleInOrg(Long organizationId, String expectedRole) {

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


    /*<---------------------------------------------------------------------------------->*/

    private boolean hasRoleInOrgByOrgId(Long organizationId, OrgRole requiredAccessRole) {
        Long userId = userService.getCurrentUserId();

        OrgRole currentUserRole = userOrganizationRepository.getUserRoleInOrganization(userId, organizationId);

        return currentUserRole.hasAccessLevelTo(requiredAccessRole);
    }

    public boolean hasRoleInOrgByBranchId(Long branchId, OrgRole requiredAccessRole) {
        Long organizationId = branchRepository.findOrganizationIdByBranchId(branchId);

        if (organizationId == null) {
            return false;
        }

        log.info("OrganizationId: {}", organizationId);
        return hasRoleInOrgByOrgId(organizationId, requiredAccessRole);
    }

    public boolean hasRoleInOrgByEmployeeId(Long employeeId, OrgRole requiredAccessRole) {
        Long branchId = employeeRepository.getBranchIdById(employeeId);

        if (branchId == null) {
            return false;
        }

        log.info("BranchId: {}", branchId);
        return hasRoleInOrgByBranchId(branchId, requiredAccessRole);
    }
}