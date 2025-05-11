package ua.shpp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ua.shpp.model.GlobalRole;
import ua.shpp.model.OrgRole;
import ua.shpp.repository.*;

import java.util.function.BooleanSupplier;

@Slf4j
@Component("authz") // @authz в @PreAuthorize
@RequiredArgsConstructor
public class AuthorizationService {

    private final UserOrganizationRepository userOrganizationRepository;
    private final UserService userService;
    private final BranchRepository branchRepository;
    private final EmployeeRepository employeeRepository;
    private final ServiceRepository serviceRepository;
    private final ClientRepository clientRepository;
    private final ScheduleEventRepository scheduleEventRepository;

    /**
     * Hot to use?
     * ->>>
     * public boolean yourMethodName(YourParams params) {
     * return withSuperAdminCheck(() -> {
     * // your implementation
     * });
     * }
     */
    private boolean withSuperAdminCheck(BooleanSupplier check) {
        return isSuperAdmin() || check.getAsBoolean();
    }

    /*<---------------------------------------------------------------------------------->*/
    // ORGANIZATION (FINAL METHOD)
    public boolean hasRoleInOrgByOrgId(Long organizationId, OrgRole requiredAccessRole) {
        return withSuperAdminCheck(() ->
        {
            Long userId = userService.getCurrentUserId();
            OrgRole currentUserRole = userOrganizationRepository.getUserRoleInOrganization(userId, organizationId);
            return currentUserRole != null && currentUserRole.hasAccessLevelTo(requiredAccessRole);
        });
    }

    // BRANCH
    public boolean hasRoleInOrgByBranchId(Long branchId, OrgRole requiredAccessRole) {
        return withSuperAdminCheck(() ->
        {
            Long organizationId = branchRepository.findOrganizationIdByBranchId(branchId);
            return organizationId != null && hasRoleInOrgByOrgId(organizationId, requiredAccessRole);
        });
    }
    /*<---------------------------------------------------------------------------------->*/

    public boolean hasRoleByServiceId(Long serviceId, OrgRole expectedRole) {
        return withSuperAdminCheck(() ->
        {
            Long branchId = serviceRepository.findBranchIdByServiceId(serviceId);
            return branchId != null && hasRoleInOrgByBranchId(branchId, expectedRole);
        });
    }

    public boolean hasRoleInOrgByEmployeeId(Long employeeId, OrgRole requiredAccessRole) {
        return withSuperAdminCheck(() ->
        {
            Long branchId = employeeRepository.getBranchIdById(employeeId);
            return branchId != null && hasRoleInOrgByBranchId(branchId, requiredAccessRole);
        });
    }

    public boolean hasRoleInOrgByClientId(Long clientId, OrgRole expectedRole) {
        return withSuperAdminCheck(() ->
        {
            Long organizationId = clientRepository.findOrganizationIdByClientId(clientId);
            return organizationId != null && hasRoleInOrgByOrgId(organizationId, expectedRole);
        });
    }

    public boolean hasRoleByScheduleEventId(Long eventId, OrgRole expectedRole) {
        return withSuperAdminCheck(() -> {
            Long serviceId = scheduleEventRepository.findServiceIdByEventId(eventId);
            return serviceId != null && hasRoleByServiceId(serviceId, expectedRole);
        });
    }

    private boolean isSuperAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null &&
                authentication.getAuthorities().contains(new SimpleGrantedAuthority(GlobalRole.SUPER_ADMIN.name()));
    }
}