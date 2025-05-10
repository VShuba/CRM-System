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
import ua.shpp.dto.employee.EmployeeRequestDTO;
import ua.shpp.dto.employee.EmployeeResponseDTO;
import ua.shpp.dto.employee.EmployeeServicesResponseDTO;
import ua.shpp.dto.employee.EmployeeUpdateRequestDTO;
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

    @PostMapping(path = "/employees",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Add new employee with avatar and JSON data")
    public ResponseEntity<EmployeeResponseDTO> addEmployee(@RequestPart(name = "avatar") MultipartFile avatarImg,
                                                           @Schema(
                                                                   description = "JSON-formatted employee data " +
                                                                           "(EmployeeRequestDTO) as string",
                                                                   requiredMode = Schema.RequiredMode.REQUIRED,
                                                                   implementation = EmployeeRequestDTO.class
                                                           )
                                                           @RequestPart(name = "employee") String employeeDTOStr) {
        try {
            EmployeeRequestDTO employeeRequestDTO = objectMapper.readValue(employeeDTOStr, EmployeeRequestDTO.class);
            log.info("Deserialized string into EmployeeRequestDTO: {}", employeeRequestDTO.toString());

            EmployeeResponseDTO employeeResponseDTO = employeeService.createEmployee(avatarImg, employeeRequestDTO);

            return ResponseEntity.status(HttpStatus.CREATED).body(employeeResponseDTO);
        } catch (JsonProcessingException e) {
            throw new InvalidJsonFormatException("Invalid JSON format for EmployeeRequestDTO", e);
        }
    }

    @PutMapping(path = "/employees/{id}",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update employee with avatar and JSON data") // todo where is  PreAuthorize?
    public ResponseEntity<EmployeeResponseDTO> updateEmployee(@PathVariable Long id, @RequestPart(name = "avatar", required = false) MultipartFile avatarImg,
                                                              @Schema(
                                                                      description = "JSON-formatted employee data " +
                                                                              "(EmployeeRequestDTO) as string",
                                                                      requiredMode = Schema.RequiredMode.REQUIRED,
                                                                      implementation = EmployeeUpdateRequestDTO.class
                                                              )
                                                              @RequestPart(name = "employee", required = false) String employeeDTOStr) {
        try {
            EmployeeUpdateRequestDTO employeeUpdateRequestDTO = null;
            if (employeeDTOStr != null) {
                employeeUpdateRequestDTO = objectMapper.readValue(employeeDTOStr, EmployeeUpdateRequestDTO.class);
                log.info("Deserialized string into EmployeeRequestDTO: {}", employeeUpdateRequestDTO.toString());
            }
            EmployeeResponseDTO employeeResponseDTO = employeeService.updateEmployee(id, avatarImg, employeeUpdateRequestDTO);

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