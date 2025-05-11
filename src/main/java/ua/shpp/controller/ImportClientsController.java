package ua.shpp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.shpp.service.ImportClientsService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ImportClientsController {

    private final ImportClientsService service;

    @PostMapping("/organizations/{orgId}/clients/import")
    public ResponseEntity<Void> importClientsFromSheet(@PathVariable(name = "orgId") Long orgId, @RequestBody String sheetId) {
        service.importClients(orgId, sheetId);
        return ResponseEntity.ok().build();
    }
}
