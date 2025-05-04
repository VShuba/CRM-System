package ua.shpp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.shpp.dto.employee.EmployeeCreateRequestDTO;
import ua.shpp.dto.employee.EmployeeResponseDTO;
import ua.shpp.dto.employee.EmployeeServicesResponseDTO;
import ua.shpp.exception.InvalidJsonFormatException;
import ua.shpp.service.EmployeeService;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "Employees", description = "Operations related to employee")
@Slf4j
public class EmployeeController {
    private final EmployeeService employeeService;
    private final ObjectMapper objectMapper;

    @PostMapping(path = "/employee",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Add new employee with avatar and JSON data")
    public ResponseEntity<EmployeeResponseDTO> addEmployee(@RequestPart(name = "avatar") MultipartFile avatarImg,
                                                           @Schema(
                                                                   description = "JSON-formatted employee data " +
                                                                           "(EmployeeCreateRequestDTO) as string",
                                                                   requiredMode = Schema.RequiredMode.REQUIRED,
                                                                   implementation = EmployeeCreateRequestDTO.class
                                                           )
                                                           @RequestPart(name = "employee") String employeeDTOStr) {
        try {
            EmployeeCreateRequestDTO employeeCreateRequestDTO = objectMapper.readValue(employeeDTOStr, EmployeeCreateRequestDTO.class);
            log.info("Deserialized string into EmployeeRequestDTO: {}", employeeCreateRequestDTO.toString());

            EmployeeResponseDTO employeeResponseDTO = employeeService.createEmployee(avatarImg, employeeCreateRequestDTO);

            return ResponseEntity.status(HttpStatus.CREATED).body(employeeResponseDTO);
        } catch (JsonProcessingException e) {
            throw new InvalidJsonFormatException("Invalid JSON format for EmployeeRequestDTO", e);
        }
    }

    @GetMapping("/employee/{id}")
    @Operation(summary = "Get employee")
    @PreAuthorize("@authz.hasRoleInOrgByEmployeeId(#id, T(ua.shpp.model.OrgRole).MANAGER)")
    public ResponseEntity<EmployeeResponseDTO> getEmployee(@PathVariable Long id) {
        EmployeeResponseDTO employeeResponseDTO = employeeService.getEmployee(id);

        return ResponseEntity.status(HttpStatus.OK).body(employeeResponseDTO);
    }

    @DeleteMapping("/employee/{id}")
    @Operation(summary = "Delete employee")
    @PreAuthorize("@authz.hasRoleInOrgByEmployeeId(#id, T(ua.shpp.model.OrgRole).ADMIN)")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        if (employeeService.deleteEmployee(id)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/employee/{id}/services")
    @Operation(summary = "Get employee services")
    @PreAuthorize("@authz.hasRoleInOrgByEmployeeId(#id, T(ua.shpp.model.OrgRole).MANAGER)")
    public ResponseEntity<List<EmployeeServicesResponseDTO>> getEmployeeServices(@PathVariable Long id) {
        List<EmployeeServicesResponseDTO> employeeServicesResponseDTO = employeeService.getEmployeeServices(id);

        return ResponseEntity.status(HttpStatus.OK).body(employeeServicesResponseDTO);
    }
}