package ua.shpp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.shpp.dto.EmployeeRequestDTO;
import ua.shpp.dto.EmployeeResponseDTO;
import ua.shpp.dto.EmployeeServiceCreateDTO;
import ua.shpp.entity.BranchEntity;
import ua.shpp.entity.EmployeeEntity;
import ua.shpp.entity.ServiceEntity;
import ua.shpp.mapper.EmployeeMapper;
import ua.shpp.repository.BranchRepository;
import ua.shpp.repository.EmployeeRepository;
import ua.shpp.repository.ServiceRepository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeMapper employeeMapper;
    private final EmployeeRepository employeeRepository;
    private final ServiceRepository serviceRepository;
    private final BranchRepository branchRepository;
    private final int AVATAR_WIDTH = 75;
    private final int AVATAR_HEIGHT = 75;
    private final int MAX_AVATAR_SIZE_MB = 3;

    public EmployeeResponseDTO createEmployee(MultipartFile avatarImg, EmployeeRequestDTO employeeDTO) {
        BranchEntity branch = branchRepository.findById(employeeDTO.branchId())
                .orElseThrow(() -> new RuntimeException("Branch with id " + employeeDTO.branchId() + " not found"));

        Set<Long> expectedExistingServicesIds = employeeDTO.existingServicesIds();

        log.info("Finding in db service entities with ids: {}", expectedExistingServicesIds);
        List<ServiceEntity> existingServicesEntity = serviceRepository.findAllById(expectedExistingServicesIds);

        checkExistingServicesById(employeeDTO.existingServicesIds(), existingServicesEntity);

        int kilobyte = 1024;
        log.info("Image size: {} MB", avatarImg.getSize() / (kilobyte * kilobyte));
        if (avatarImg.getSize() > MAX_AVATAR_SIZE_MB * kilobyte * kilobyte) {
            throw new RuntimeException("File size exceeds the maximum allowed size of " + MAX_AVATAR_SIZE_MB + " MB");
        }
        EmployeeEntity employeeEntity = employeeMapper.EmployeeRequestDTOToEmployeeEntity(employeeDTO);
        employeeEntity.setBranch(branch);

        Set<EmployeeServiceCreateDTO> newServices = employeeDTO.newServicesDTO();

        List<ServiceEntity> newServiceEntities = newServices.stream()
                .map(dto -> {
                    ServiceEntity service = new ServiceEntity();
                    service.setName(dto.name());
                    service.setColor(dto.color());
                    service.setBranch(branch);
                    return service;
                })
                .toList();

        newServiceEntities = serviceRepository.saveAll(newServiceEntities);
        newServiceEntities.addAll(existingServicesEntity);
        employeeEntity.setServices(new HashSet<>(existingServicesEntity));

        byte[] resizedBytesAvatar = resizeImage(avatarImg, AVATAR_WIDTH, AVATAR_HEIGHT);

        employeeEntity.setAvatar(resizedBytesAvatar);

        employeeEntity = employeeRepository.save(employeeEntity);

        String base64Avatar = convertImageToBase64(resizedBytesAvatar);

        return employeeMapper.employeeEntityToEmployeeResponseDTO(employeeEntity, base64Avatar);
    }

    /**
     * Checks if all the service IDs in the given set exist in the database.
     * If any of the service IDs do not exist, an exception is thrown.
     *
     * @param expectedExistingServicesIds - a set of service IDs that are expected to exist in the database.
     * @param existingServicesEntity      - a list of service entities fetched from the database.
     * @throws RuntimeException if one or more service IDs from the expected list do not exist in the database.
     */
    private void checkExistingServicesById(Set<Long> expectedExistingServicesIds, List<ServiceEntity> existingServicesEntity) {
        List<Long> existingServicesIds = existingServicesEntity.stream()
                .map(ServiceEntity::getId)
                .toList();

        List<Long> notFoundServiceIds = expectedExistingServicesIds.stream()
                .filter(id -> !existingServicesIds.contains(id))
                .toList();

        if (!notFoundServiceIds.isEmpty()) {
            throw new RuntimeException("There are no services with id " + notFoundServiceIds);
        }
    }

    private byte[] resizeImage(MultipartFile img, int targetWidth, int targetHeight) {
        BufferedImage avatar = multipartToBufferedImage(img);
        String imgFormat = getImageFormat(img);

        log.info("Resizing image width: {}, height: {}", avatar.getWidth(), avatar.getHeight());
        BufferedImage resizedAvatar = Scalr.resize(avatar, Scalr.Mode.FIT_EXACT,  targetWidth, targetHeight);
        log.info("Resizing image width: {}, height: {}", resizedAvatar.getWidth(), resizedAvatar.getHeight());

        return bufferedImageToBytes(resizedAvatar, imgFormat);
    }

    private String convertImageToBase64(byte[] imageBytes) {
        return Base64.getEncoder().encodeToString(imageBytes);
    }

    private byte[] bufferedImageToBytes(BufferedImage bufferedImage, String imgFormat) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            ImageIO.write(bufferedImage, imgFormat, baos);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private BufferedImage multipartToBufferedImage(MultipartFile img) {
        try {
            InputStream inputStream = img.getInputStream();

            return ImageIO.read(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getImageFormat(MultipartFile img) {
        log.info("Image name: {}. Image type: {}", img.getOriginalFilename(), img.getContentType());
        String contentType = img.getContentType();

        if (contentType != null) {
            if (contentType.contains("jpeg")) {
                return "JPEG";
            } else if (contentType.contains("png")) {
                return "PNG";
            } else if (contentType.contains("heic")) {
                return "HEIC";
            } else if (contentType.contains("webp")) {
                return "WEBP";
            } else {
                throw new RuntimeException("Unsupported image format: " + contentType);
            }
        }
        throw new RuntimeException("Unable to determine image format");
    }
}