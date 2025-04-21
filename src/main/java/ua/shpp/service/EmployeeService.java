package ua.shpp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import ua.shpp.util.ImageUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        log.debug("Find by id: {} branch", employeeDTO.branchId());
        BranchEntity branch = branchRepository.findById(employeeDTO.branchId())
                .orElseThrow(() -> new RuntimeException("Branch with id " + employeeDTO.branchId() + " not found"));
        log.debug("Branch was found with name: {}, employees num: {}, services num: {}",
                branch.getName(), branch.getEmployees().size(), branch.getServiceEntities().size());

        if (employeeRepository.existsByEmailAndBranchId(employeeDTO.email(), branch.getId())) {
            throw new RuntimeException("Employee with email " + employeeDTO.email() + " already exists");
        }

        ImageUtil.checkMaxImgSizeInMB(avatarImg, MAX_AVATAR_SIZE_MB);

        List<ServiceEntity> existingServicesEntity = new ArrayList<>();
        Set<Long> expectedExistingServicesIds = employeeDTO.existingServicesIds();

        if (!expectedExistingServicesIds.isEmpty()) {
            log.info("Finding in db service entities with ids: {}", expectedExistingServicesIds);
            existingServicesEntity = serviceRepository.findAllById(expectedExistingServicesIds);
            log.info("Found services with names: {}, ids: {}", existingServicesEntity.stream().map(ServiceEntity::getName),
                    expectedExistingServicesIds);

            checkExistingServicesById(expectedExistingServicesIds, existingServicesEntity);
        }

        Set<EmployeeServiceCreateDTO> newServices = employeeDTO.newServicesDTO();
        log.debug("New services: {}", newServices);

        List<ServiceEntity> newServiceEntities = newServices.stream()
                .map(dto -> {
                    log.debug("Adding new service with name: {}, color: {}, branchId: {}",
                            dto.name(), dto.color(), branch.getId());
                    return ServiceEntity.builder()
                            .name(dto.name())
                            .color(dto.color())
                            .branch(branch)
                            .build();
                }).collect(Collectors.toCollection(ArrayList::new));

        log.debug("New services names: {}, ids: {}",
                newServiceEntities.stream().map(ServiceEntity::getName).collect(Collectors.toList()),
                newServiceEntities.stream().map(ServiceEntity::getId).collect(Collectors.toList()));

        newServiceEntities = serviceRepository.saveAll(newServiceEntities);


        byte[] resizedBytesAvatar = ImageUtil.resizeImage(avatarImg, AVATAR_WIDTH, AVATAR_HEIGHT);

        log.debug("Mapping EmployeeRequestDTO to EmployeeEntity");
        EmployeeEntity employeeEntity = employeeMapper.EmployeeRequestDTOToEmployeeEntity(employeeDTO);
        log.debug("After mapping: employee entity id: {}, branch: {}, services: {}", employeeEntity.getId(),
                employeeEntity.getBranch(), employeeEntity.getServices());

        employeeEntity.setBranch(branch);
        log.debug("After set branch: employee entity id: {}, branchId: {}, services: {}", employeeEntity.getId(),
                employeeEntity.getBranch().getId(), employeeEntity.getServices());
        newServiceEntities.addAll(existingServicesEntity);
        employeeEntity.setServices(new HashSet<>(newServiceEntities));

        employeeEntity.setAvatar(resizedBytesAvatar);

        employeeEntity = employeeRepository.save(employeeEntity);

        String base64Avatar = ImageUtil.convertImageToBase64(resizedBytesAvatar);

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
        log.info("Checking service existence. Expected IDs: {}", expectedExistingServicesIds);
        List<Long> existingServicesIds = existingServicesEntity.stream()
                .map(ServiceEntity::getId)
                .toList();
        log.info("Services found in DB (IDs): {}", existingServicesIds);

        List<Long> notFoundServiceIds = expectedExistingServicesIds.stream()
                .filter(id -> !existingServicesIds.contains(id))
                .toList();

        if (!notFoundServiceIds.isEmpty()) {
            throw new RuntimeException("There are no services with id " + notFoundServiceIds);
        }
        log.info("All provided service IDs are valid and exist in the database.");
    }
}