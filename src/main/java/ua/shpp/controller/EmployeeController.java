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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.shpp.dto.employee.EmployeeDeleteRequestDTO;
import ua.shpp.dto.employee.EmployeeRequestDTO;
import ua.shpp.dto.employee.EmployeeResponseDTO;
import ua.shpp.exception.InvalidJsonFormatException;
import ua.shpp.service.EmployeeService;

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
                                                           @Schema(description = "JSON-formatted employee data (EmployeeRequestDTO) as string",
                                                                   requiredMode = Schema.RequiredMode.REQUIRED,
                                                                   implementation = EmployeeRequestDTO.class)
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

    @DeleteMapping("/employee")
    public ResponseEntity<Void> deleteEmployee(@RequestBody EmployeeDeleteRequestDTO requestDTO) {
        if (employeeService.deleteEmployee(requestDTO.employeeId(), requestDTO.branchId())) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}