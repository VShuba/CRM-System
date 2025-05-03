package ua.shpp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.shpp.dto.ClientRequestDto;
import ua.shpp.dto.EventClientDto;
import ua.shpp.entity.payment.OneTimeInfoEntity;
import ua.shpp.entity.payment.SubscriptionInfoEntity;
import ua.shpp.service.EventClientService;

@RestController
@RequestMapping("events")
public class EventClientController {

    private final EventClientService eventClientService;

    public EventClientController(EventClientService eventClientService) {
        this.eventClientService = eventClientService;
    }

    @PostMapping("/{eventId}/clients/{clientId}")
    public ResponseEntity addClientToEvent(@PathVariable Long clientId, @PathVariable Long eventId) {
        eventClientService.addClientToEvent(clientId, eventId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/")
    public ResponseEntity<EventClientDto> changeClientEventStatus(@RequestBody EventClientDto eventClientDto) {
        return ResponseEntity.ok().body(eventClientService.changeClientStatus(eventClientDto));
    }

    @PostMapping("/organizations/{orgId}/clients/event/{eventId}")
    public ResponseEntity<EventClientDto> addClientAndAssignEvent(@PathVariable Long eventId,
                                                                  @PathVariable Long orgId,
                                                                  @RequestBody ClientRequestDto eventClientDto) {
        return ResponseEntity.ok().body(eventClientService.addClientAndAssignEvent(eventId, orgId, eventClientDto));
    }


}
