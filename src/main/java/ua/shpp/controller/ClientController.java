package ua.shpp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.shpp.dto.ClientRequestDto;
import ua.shpp.dto.ClientResponseDto;
import ua.shpp.service.ClientService;

@RestController
@RequestMapping("/api/organizations/{orgId}/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    public ResponseEntity<ClientResponseDto> createClient(
            @PathVariable(name = "orgId") Long orgId,
            @RequestBody ClientRequestDto requestDto
    ) {
        return ResponseEntity.ok(clientService.createClient(orgId, requestDto));
    }
}
