package ua.shpp.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
public class EmployeeController {
    private final EmployeeService employeeService;

    @PostMapping(path = "/employee", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    private ResponseEntity<EmployeeResponseDTO> addEmployee(@RequestPart(name = "avatar") MultipartFile avatarImg,
                                                            @RequestPart(name = "employee") EmployeeRequestDTO employeeDTO) {
        EmployeeResponseDTO employeeResponseDTO = employeeService.createEmployee(avatarImg, employeeDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(employeeResponseDTO);
    }
}