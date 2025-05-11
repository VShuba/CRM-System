package ua.shpp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ua.shpp.dto.InvitationRequestDTO;
import ua.shpp.dto.ResponseInvitationDTO;
import ua.shpp.service.InvitationService;

@RestController
@RequestMapping("/api/invitations")
public class InvitationController {

    private final InvitationService invitationService;

    public InvitationController(InvitationService invitationService) {
        this.invitationService = invitationService;
    }

    @PostMapping
    @PreAuthorize("@authz.hasRoleInOrgByOrgId(#invitationRequestDTO.organizationId(), T(ua.shpp.model.OrgRole).ADMIN)")
    public ResponseEntity<ResponseInvitationDTO> createInvitation(
            @RequestBody InvitationRequestDTO invitationRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = authentication.getName();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(invitationService.createInvitationLink(invitationRequestDTO, currentUser));
    }

    @PostMapping("/{id}")
    public ResponseEntity<ResponseInvitationDTO> acceptInvitation(@PathVariable String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = authentication.getName();
        invitationService.acceptLink(id, currentUser);
        return ResponseEntity.status(HttpStatus.OK).build();

    }
}
