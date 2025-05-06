package ua.shpp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.shpp.service.ImportClientsService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ImportClientsController {

    private final ImportClientsService service;

    @PostMapping("/organizations/{orgId}/clients/import")
    public ResponseEntity<String> importClientsFromSheet(@RequestBody String sheetId) {
        return ResponseEntity.ok(service.importClients(sheetId));
    }
}
