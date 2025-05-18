package ua.shpp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ua.shpp.entity.OneTimeOfferEntity;
import ua.shpp.entity.SubscriptionOfferEntity;
import ua.shpp.entity.payment.CheckEntity;
import ua.shpp.exception.CheckNotFoundException;
import ua.shpp.exception.OfferNotFoundException;
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
    private final EventTypeRepository eventTypeRepository;
    private final SubscriptionDealRepository subscriptionDealRepository;
    private final OneTimeDealRepository oneTimeDealRepository;
    private final CheckRepository checkRepository;
    private final OneTimeOfferRepository oneTimeOfferRepository;
    private final SubscriptionOfferRepository subscriptionOfferRepository;

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

    public boolean hasRoleByClientAndScheduleEventId(Long clientId, Long scheduleEventId, OrgRole expectedRole) {
        return withSuperAdminCheck(() ->
                hasRoleInOrgByClientId(clientId, expectedRole) &&
                        hasRoleByScheduleEventId(scheduleEventId, expectedRole)
        );
    }

    public boolean hasRoleInOrgByEventTypeId(Long eventTypeId, OrgRole expectedRole) {
        return withSuperAdminCheck(() -> {
            Long branchId = eventTypeRepository.findBranchIdByEventTypeId(eventTypeId);
            return branchId != null && hasRoleInOrgByBranchId(branchId, expectedRole);
        });
    }

    public boolean hasRoleInOrgBySubscriptionDealId(Long dealId, OrgRole expectedRole) {
        return withSuperAdminCheck(() -> {
            Long clientId = subscriptionDealRepository.findClientIdBySubscriptionDealId(dealId);
            return clientId != null && hasRoleInOrgByClientId(clientId, expectedRole);
        });
    }

    public boolean hasRoleInOrgByOneTimeDealId(Long dealId, OrgRole expectedRole) {
        return withSuperAdminCheck(() -> {
            Long clientId = oneTimeDealRepository.findClientIdByOneTimeDealId(dealId);
            return clientId != null && hasRoleInOrgByClientId(clientId, expectedRole);
        });
    }

    public boolean hasRoleBySubscriptionDealAndScheduleEventId(Long subscriptionDealId, Long scheduleEventId, OrgRole expectedRole) {
        return withSuperAdminCheck(() -> {
            Long clientId = subscriptionDealRepository.findClientIdBySubscriptionDealId(subscriptionDealId);
            return clientId != null &&
                    hasRoleInOrgByClientId(clientId, expectedRole) &&
                    hasRoleByScheduleEventId(scheduleEventId, expectedRole);
        });
    }

    public boolean hasRoleByCheckId(Long checkId, OrgRole expectedRole) {
        return withSuperAdminCheck(() -> {
            CheckEntity checkEntity = checkRepository.findById(checkId)
                    .orElseThrow(() -> new CheckNotFoundException(
                            String.format("Check id: %d, not found", checkId)));
            Long orgId = checkEntity.getOneTimeInfo() != null
                    ? checkEntity.getOneTimeInfo().getClient().getOrganization().getId()
                    : checkEntity.getSubscriptionInfo().getClient().getOrganization().getId();
            return orgId != null && hasRoleInOrgByOrgId(orgId, expectedRole);
        });
    }

    public boolean hasRoleByOneTimeOfferId(Long offerId, OrgRole expectedRole) {
        OneTimeOfferEntity offerEntity = oneTimeOfferRepository.findById(offerId)
                .orElseThrow(() -> new OfferNotFoundException(
                        String.format("Offer id: %d, not found", offerId)));
        Long orgId = offerEntity
                .getActivity()
                .getBranch()
                .getOrganization()
                .getId();
        return orgId != null && hasRoleInOrgByOrgId(orgId, expectedRole);
    }

    public boolean hasRoleSubscriptionOfferId(Long offerId, OrgRole expectedRole) {
        SubscriptionOfferEntity offerEntity = subscriptionOfferRepository.findById(offerId)
                .orElseThrow(() -> new OfferNotFoundException(
                        String.format("Offer id: %d, not found", offerId)));
        Long orgId = offerEntity
                .getActivities()
                .getFirst()
                .getBranch()
                .getOrganization()
                .getId();
        return orgId != null && hasRoleInOrgByOrgId(orgId, expectedRole);
    }

    private boolean isSuperAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null &&
                authentication.getAuthorities().contains(new SimpleGrantedAuthority(GlobalRole.SUPER_ADMIN.name()));
    }
}