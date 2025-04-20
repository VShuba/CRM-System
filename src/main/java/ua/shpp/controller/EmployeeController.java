package ua.shpp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ua.shpp.dto.EmployeeRequestDTO;
import ua.shpp.dto.EmployeeResponseDTO;
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
    private ResponseEntity<EmployeeResponseDTO> addEmployee(@RequestPart(name = "avatar") MultipartFile avatarImg,
                                                            @RequestPart(name = "employee") String employeeDTOStr) {
        try {
            EmployeeRequestDTO employeeRequestDTO = objectMapper.readValue(employeeDTOStr, EmployeeRequestDTO.class);
            log.info("Deserialized string into EmployeeRequestDTO: {}", employeeRequestDTO.toString());

            EmployeeResponseDTO employeeResponseDTO = employeeService.createEmployee(avatarImg, employeeRequestDTO);

            return ResponseEntity.status(HttpStatus.CREATED).body(employeeResponseDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}