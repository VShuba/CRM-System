package ua.shpp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestAccessController {

    // Глобальная роль SUPER_ADMIN
    @GetMapping("/global/super-admin")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<String> onlyForSuperAdmin() {
        return ResponseEntity.ok("Access allowed: GLOBAL ROLE = SUPER_ADMIN");
    }

    // Глобальная роль USER
    @GetMapping("/global/user")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<String> onlyForGlobalUser() {
        return ResponseEntity.ok("Access allowed: GLOBAL ROLE = USER");
    }

    // Организационная роль ADMIN
    @GetMapping("/org/{orgId}/admin")
    @PreAuthorize("@authz.hasRoleInOrg(#orgId, 'ADMIN')")
    public ResponseEntity<String> onlyOrgAdmin(@PathVariable Long orgId) {
        return ResponseEntity.ok("Access allowed: ORG ROLE = ADMIN in organization: ID " + orgId);
    }

    // Организационная роль MANAGER
    @GetMapping("/org/{orgId}/manager")
    @PreAuthorize("@authz.hasRoleInOrg(#orgId, 'MANAGER')")
    public ResponseEntity<String> onlyOrgManager(@PathVariable Long orgId) {
        return ResponseEntity.ok("Access allowed: ORG ROLE = MANAGER in organization: ID " + orgId);
    }

    // Супер админ ИЛИ админ организации
    @GetMapping("/org/{orgId}/admin-or-super")
    @PreAuthorize("hasAuthority('SUPER_ADMIN') or @authz.hasRoleInOrg(#orgId, 'ADMIN')")
    public ResponseEntity<String> adminOrSuper(@PathVariable Long orgId) {
        return ResponseEntity.ok("Access allowed: SUPER_ADMIN or ORG ADMIN");
    }

    // Только если НЕ супер админ (пример от обратного)
    @GetMapping("/not-super")
    @PreAuthorize("!hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<String> notForSuperAdmin() {
        return ResponseEntity.ok("You r NOT SUPER_ADMIN — access allowed");
    }

    // Сложное условие: MANAGER в орг и НЕ SUPER_ADMIN
    @GetMapping("/org/{orgId}/manager-not-super")
    @PreAuthorize("@authz.hasRoleInOrg(#orgId, 'MANAGER') and !hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<String> managerButNotSuper(@PathVariable Long orgId) {
        return ResponseEntity.ok("You are MANAGER in org, but not a SUPER_ADMIN — access allowed");
    }
}