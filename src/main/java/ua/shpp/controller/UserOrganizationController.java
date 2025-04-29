package ua.shpp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.shpp.dto.UserOrganizationDTO;
import ua.shpp.model.OrgRole;
import ua.shpp.model.Role;
import ua.shpp.service.UserOrganizationService;

@RestController
public class UserOrganizationController {

    private final UserOrganizationService userOrganizationService;

    public UserOrganizationController(UserOrganizationService userOrganizationService) {
        this.userOrganizationService = userOrganizationService;
    }

    @DeleteMapping("organizations/{organizationId}/user/{userId}")
    public ResponseEntity<Void> deleteUserOrganization(@PathVariable Long organizationId, @PathVariable Long userId) {
        userOrganizationService.deleteUserOrganizationRole(organizationId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("organizations/{organizationId}/user/{userId}")
    public ResponseEntity<UserOrganizationDTO> updateUserOrganizationRole(@PathVariable Long organizationId,
                                                                          @PathVariable Long userId,
                                                                          @RequestBody OrgRole role) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userOrganizationService.changeUserOrganizationRole(userId, organizationId, role));
    }

}
