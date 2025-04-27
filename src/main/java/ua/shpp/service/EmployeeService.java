package ua.shpp.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.shpp.dto.employee.EmployeeRequestDTO;
import ua.shpp.dto.employee.EmployeeResponseDTO;
import ua.shpp.dto.employee.EmployeeServiceCreateDTO;
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

        if (employeeExistsInBranchByEmail(employeeDTO.email(), branch)) {
            throw new RuntimeException("Employee with email " + employeeDTO.email() + " already exists");
        }

        ImageUtil.checkMaxImgSizeInMB(avatarImg, MAX_AVATAR_SIZE_MB);

        List<ServiceEntity> existingServicesEntity = findExistingServicesById(employeeDTO.existingServicesIds());

        List<ServiceEntity> newServiceEntities = saveNewServices(employeeDTO.newServicesDTO(), branch);

        newServiceEntities.addAll(existingServicesEntity);

        byte[] resizedBytesAvatar = ImageUtil.resizeImage(avatarImg, AVATAR_WIDTH, AVATAR_HEIGHT);

        log.debug("Mapping EmployeeRequestDTO to EmployeeEntity");
        EmployeeEntity employeeEntity = employeeMapper.EmployeeRequestDTOToEmployeeEntity(employeeDTO);
        log.debug("After mapping: employee entity id: {}, branch: {}, services: {}", employeeEntity.getId(),
                branch.getId(), employeeEntity.getServices());

        employeeEntity.getBranches().add(branch);
        log.debug("After set branch: employee entity id: {}, branchId: {}, services: {}", employeeEntity.getId(),
                branch.getId(), employeeEntity.getServices());
        employeeEntity.setServices(new HashSet<>(newServiceEntities));

        employeeEntity.setAvatar(resizedBytesAvatar);

        employeeEntity = employeeRepository.save(employeeEntity);

        String base64Avatar = ImageUtil.convertImageToBase64(resizedBytesAvatar);

        return employeeMapper.employeeEntityToEmployeeResponseDTO(employeeEntity, base64Avatar);
    }

    /**
     * Deletes the relation between an employee and a branch, removes related services for this employee,
     * and deletes the employee record if no other branch associations exist.
     *
     * @param employeeId the ID of the employee to be modified or deleted
     * @param branchId the ID of the branch from which the employee will be unlinked
     * @return true if any records were affected, false otherwise
     */
    @Transactional
    public boolean deleteEmployee(Long employeeId, Long branchId) {
        int deletedRecords = employeeRepository.deleteEmployeeAndRelatedRecords(employeeId, branchId);
        log.info("Records deleted: {}", deletedRecords);
        return deletedRecords > 0;
    }

    /**
     * Save a list of new {@link ServiceEntity} to db based on provided list of {@link EmployeeServiceCreateDTO}
     * <p>
     * If the provided set of IDs is not empty, the method will:
     * <li>Create new {@link ServiceEntity} objects based on the provided {@link EmployeeServiceCreateDTO}</li>
     * <li>Validate that all services that provided to create are not exist in db using {@link #checkNewServicesUniqueInBranch(Set, Long)}</li>
     * <p>
     * If the set is empty, it returns an empty list.
     *
     * @param newServices a set of service DTOs representing the new services to be created and saved in the database.
     * @param branch      the {@link BranchEntity} that will be assigned to each new service.
     * @return a list of saved {@link ServiceEntity} objects.
     * @throws RuntimeException if any error occurs during the saving process (e.g., database issues).
     */
    private List<ServiceEntity> saveNewServices(Set<EmployeeServiceCreateDTO> newServices, BranchEntity branch) {
        if (newServices.isEmpty()) {
            log.debug("New services list is empty");
            return new ArrayList<>();
        }
        log.debug("New services: {}", newServices);
        checkNewServicesUniqueInBranch(newServices, branch.getId());

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

        return serviceRepository.saveAll(newServiceEntities);
    }

    /**
     * Retrieves a list of existing {@link ServiceEntity} from the database by their IDs.
     * <p>
     * If the provided set of IDs is not empty, the method will:
     *
     * <li>Fetch matching service entities from the database</li>
     * <li>Validate that all expected services with IDs actually exist using {@link #checkExistingServicesById(Set, List)}</li>
     * <p>
     * If the set is empty, it returns an empty list.
     *
     * @param expectedExistingServicesIds a set of service IDs that are expected to exist in the database.
     * @return a list of {@link ServiceEntity} corresponding to the provided IDs.
     * @throws RuntimeException if any of the provided IDs do not exist in the database.
     */
    private List<ServiceEntity> findExistingServicesById(Set<Long> expectedExistingServicesIds) {
        List<ServiceEntity> existingServicesEntity = new ArrayList<>();

        if (!expectedExistingServicesIds.isEmpty()) {
            log.info("Looking up services in DB by IDs: {}", expectedExistingServicesIds);
            existingServicesEntity = serviceRepository.findAllById(expectedExistingServicesIds);
            log.info("Found services with names: {}, ids: {}", existingServicesEntity.stream().map(ServiceEntity::getName),
                    expectedExistingServicesIds);

            checkExistingServicesById(expectedExistingServicesIds, existingServicesEntity);
        }

        return existingServicesEntity;
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

    /**
     * Checks if any of the provided services already exist in the database for the specified branchId.
     * If one or more services already exist, an exception is thrown.
     *
     * @param newServices a set of services to be created
     * @param branchId    the ID of the branch
     * @throws RuntimeException if one or more of the provided service names already exist in the specified branch
     */
    private void checkNewServicesUniqueInBranch(Set<EmployeeServiceCreateDTO> newServices, Long branchId) {
        List<String> newServiceNames = newServices.stream()
                .map(EmployeeServiceCreateDTO::name)
                .toList();
        List<String> existingServiceNames = serviceRepository.findAllServiceNamesByNamesAndBranch(newServiceNames, branchId);

        if (!existingServiceNames.isEmpty()) {
            throw new IllegalArgumentException("Сервіси з такими іменами вже існують у гілці: " + existingServiceNames);
        }
    }

    private static boolean employeeExistsInBranchByEmail(String email, BranchEntity branch) {
        return branch.getEmployees().stream()
                .anyMatch(e -> e.getEmail().equals(email));
    }
}