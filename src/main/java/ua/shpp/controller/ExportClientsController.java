package ua.shpp.controller;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.shpp.service.ExportClientsService;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ExportClientsController {

    private final ExportClientsService exportClientsService;

    public ExportClientsController(ExportClientsService exportClientsService) {
        this.exportClientsService = exportClientsService;
    }

    @GetMapping("organization/{orgId}/clients/export")
    @PreAuthorize("@authz.hasRoleInOrgByOrgId(#orgId, T(ua.shpp.model.OrgRole).ADMIN)")
    public void exportClients(@PathVariable("orgId") Long orgId, HttpServletResponse response) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        response.setContentType("text/tsv; charset=UTF-8");
        response.addHeader("Content-Disposition", "attachment; filename=\"clients.tsv\"");
        exportClientsService.exportClientsForOrganization(orgId, response.getWriter());
    }
}
