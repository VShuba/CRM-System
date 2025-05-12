package ua.shpp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ua.shpp.dto.ClientRequestDto;
import ua.shpp.dto.EventClientDto;
import ua.shpp.service.EventClientService;

@RestController
@RequestMapping("events")
public class EventClientController {

    private final EventClientService eventClientService;

    public EventClientController(EventClientService eventClientService) {
        this.eventClientService = eventClientService;
    }

    @PreAuthorize("@authz.hasRoleByClientAndScheduleEventId(#clientId, #eventId, T(ua.shpp.model.OrgRole).ADMIN)")
    @PostMapping("/{eventId}/clients/{clientId}")
    public ResponseEntity<Void> addClientToEvent(@PathVariable Long clientId, @PathVariable Long eventId) {
        eventClientService.addClientToEvent(clientId, eventId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("@authz.hasRoleByClientAndScheduleEventId" +
            "(#eventClientDto.clientId(), #eventClientDto.scheduleId(), T(ua.shpp.model.OrgRole).ADMIN)")
    @PutMapping("/")
    public ResponseEntity<EventClientDto> changeClientEventStatus(@RequestBody EventClientDto eventClientDto) {
        return ResponseEntity.ok().body(eventClientService.changeClientStatus(eventClientDto));
    }

    @PreAuthorize("@authz.hasRoleInOrgByOrgId(#orgId, T(ua.shpp.model.OrgRole).ADMIN)")
    @PostMapping("/{eventId}/organizations/{orgId}/clients")
    public ResponseEntity<EventClientDto> addClientAndAssignEvent(@PathVariable Long eventId,
                                                                  @PathVariable Long orgId,
                                                                  @RequestBody ClientRequestDto eventClientDto) {
        return ResponseEntity.ok().body(eventClientService.addClientAndAssignEvent(eventId, orgId, eventClientDto));
    }
}
