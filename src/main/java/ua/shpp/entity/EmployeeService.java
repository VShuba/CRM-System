package ua.shpp.entity;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.shpp.dto.EmployeeRequestDTO;
import ua.shpp.dto.EmployeeResponseDTO;
import ua.shpp.mapper.EmployeeMapper;
import ua.shpp.repository.EmployeeRepository;

import java.io.IOException;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeMapper employeeMapper;
    private final EmployeeRepository employeeRepository;

    public EmployeeResponseDTO createEmployee(MultipartFile avatarImg, EmployeeRequestDTO employeeDTO) {
        EmployeeEntity employeeEntity = employeeMapper.EmployeeRequestDTOToEmployeeEntity(employeeDTO);

        try {
            String base64Avatar = convertImageToBase64(avatarImg.getBytes());
            employeeEntity.setAvatar(avatarImg.getBytes());

            employeeEntity = employeeRepository.save(employeeEntity);

            return employeeMapper.EmployeeEntityToEmployeeResponseDTO(employeeEntity, base64Avatar);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String convertImageToBase64(byte[] imageBytes) {
        return Base64.getEncoder().encodeToString(imageBytes);
    }
}