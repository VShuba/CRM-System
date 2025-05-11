package ua.shpp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ua.shpp.service.ImportClientsService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ImportClientsController {

    private final ImportClientsService service;

    @PostMapping("/organizations/{orgId}/clients/import")
    @PreAuthorize("@authz.hasRoleInOrgByOrgId(#orgId, T(ua.shpp.model.OrgRole).ADMIN)")
    public ResponseEntity<Void> importClientsFromSheet(@PathVariable(name = "orgId") Long orgId, @RequestBody String sheetId) {
        service.importClients(orgId, sheetId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/import/sheet-template", produces = "text/tab-separated-values")
    public ResponseEntity<Resource> getImportSheetTemplate() throws IOException {
        ClassPathResource resource = new ClassPathResource("import-sheet-template.tsv");

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("text/tab-separated-values"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"import-sheet-template.tsv\"")
                .body(resource);
    }
}
