package ua.shpp.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import ua.shpp.dto.employee.EmployeeRequestDTO;
import ua.shpp.dto.employee.EmployeeServiceCreateDTO;
import ua.shpp.entity.*;
import ua.shpp.model.GlobalRole;
import ua.shpp.repository.*;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EmployeeServiceTest {
    private static byte[] testImageBytes;
    private static MockMultipartFile avatarFile;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private BranchRepository branchRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ServiceRepository serviceRepository;

    private Long branchId;
    private Long serviceId;
    private Long userId;
    private Long organizationId;
    private Set<Long> existingServiceIds;
    Set<EmployeeServiceCreateDTO> newServices;

    @BeforeAll
    void init() throws IOException {

        ClassPathResource imageResource = new ClassPathResource("test.png");
        testImageBytes = Files.readAllBytes(imageResource.getFile().toPath());

        avatarFile = new MockMultipartFile(
                "avatar",
                "test.png",
                "image/png",
                testImageBytes
        );

        // Create a user
        UserEntity user = UserEntity.builder()
                .email("userForEmployeeTest@gmail.com")
                .login("employeeTester")
                .password("pass")
                .globalRole(GlobalRole.USER)
                .build();
        userRepository.save(user);

        // Create an organization
        Organization organization = Organization.builder()
                .name("OrgForEmployeeTest")
                .build();
        organizationRepository.save(organization);

        // Create a branch
        BranchEntity branch = BranchEntity.builder()
                .name("MainBranchForEmployeeTest")
                .organization(organization)
                .build();
        branchRepository.save(branch);
        branchId = branch.getId();

        // Create existing services
        ServiceEntity service1 = ServiceEntity.builder()
                .name("ServiceAsExistForEmployeeTest1")
                .color("HFSFJF")
                .branch(branch)
                .build();

        ServiceEntity service2 = ServiceEntity.builder()
                .name("ServiceAsExistForEmployeeTest2")
                .color("FFFFF")
                .branch(branch)
                .build();

        serviceRepository.save(service1);
        serviceRepository.save(service2);
        existingServiceIds = Set.of(service1.getId(), service2.getId());

        EmployeeServiceCreateDTO newService1 = new EmployeeServiceCreateDTO("Консультація", "#FF5733");
        EmployeeServiceCreateDTO newService2 = new EmployeeServiceCreateDTO("Налаштування ПЗ", "#33C1FF");
        newServices = Set.of(newService1, newService2);
    }

    @Test
    @Transactional
    void createTwoEmployeesInSameOrgDifferentBranchesWithSameServicesTest() {
        EmployeeEntity firstEmployee = createEmployee("John Doe", "employeeTest1@gmail.com", branchId, existingServiceIds, newServices);

        // === Check first employee ===
        assertNotNull(firstEmployee, "First employee was not created");

        // Check existing services
        Set<Long> actualExistingServiceIds = firstEmployee.getServices().stream()
                .map(ServiceEntity::getId)
                .filter(existingServiceIds::contains)
                .collect(java.util.stream.Collectors.toSet());

        assertTrue(actualExistingServiceIds.containsAll(existingServiceIds), "Not all existing services are assigned");

        boolean firstEmployeeHasAllNewServices = newServices.stream()
                .allMatch(dto -> firstEmployee.getServices().stream()
                        .anyMatch(service -> service.getName().equals(dto.name())));

        assertTrue(firstEmployeeHasAllNewServices, "Not all new services were added to the employee");

        // === Create a new branch ===
        BranchEntity newBranch = BranchEntity.builder()
                .name("SecondaryBranchForEmployeeTest")
                .organization(firstEmployee.getBranch().getOrganization())
                .build();
        branchRepository.save(newBranch);

        // === Add a second employee with the same email but different branch ===
        EmployeeEntity secondEmployee = createEmployee("Jane Doe", "employeeTest1@gmail.com", newBranch.getId(), existingServiceIds, newServices);

        // === Check second employee ===
        assertNotNull(secondEmployee, "Second employee was not created");

        // They should belong to different branches
        assertNotEquals(firstEmployee.getBranch().getId(), secondEmployee.getBranch().getId(), "Branches should be different");

        Set<Long> secondActualExistingServiceIds = secondEmployee.getServices().stream()
                .map(ServiceEntity::getId)
                .filter(existingServiceIds::contains)
                .collect(java.util.stream.Collectors.toSet());

        assertTrue(secondActualExistingServiceIds.containsAll(existingServiceIds), "Not all existing services are assigned to second employee");
    }

    @Test
    @Transactional
    void createTwoEmployeesInSameBranchWithSameExistingServicesOnlyTest() {
        // === 1. Create first employee with existing and new services ===
        EmployeeEntity firstEmployee = createEmployee("John Doe", "employeeTest1@gmail.com", branchId, existingServiceIds, newServices);
        assertNotNull(firstEmployee, "First employee was not created");

        // Check existing services
        Set<Long> firstEmployeeExistingServices = firstEmployee.getServices().stream()
                .map(ServiceEntity::getId)
                .filter(existingServiceIds::contains)
                .collect(java.util.stream.Collectors.toSet());
        assertTrue(firstEmployeeExistingServices.containsAll(existingServiceIds), "First employee is missing existing services");

        // Check new services
        boolean hasAllNew = newServices.stream()
                .allMatch(dto -> firstEmployee.getServices().stream()
                        .anyMatch(service -> service.getName().equals(dto.name())));
        assertTrue(hasAllNew, "First employee is missing some new services");

        // === 2. Create second employee with same existing services but no new services ===
        EmployeeEntity secondEmployee = createEmployee("Bob Johnson", "bob.employee@test.com", branchId, existingServiceIds, Set.of());
        assertNotNull(secondEmployee, "Second employee was not created");

        // Ensure both employees are in the same branch
        assertEquals(firstEmployee.getBranch().getId(), secondEmployee.getBranch().getId(), "Employees should be in the same branch");

        // Ensure second employee has only existing services
        Set<Long> secondEmployeeServiceIds = secondEmployee.getServices().stream()
                .map(ServiceEntity::getId)
                .collect(java.util.stream.Collectors.toSet());

        assertTrue(secondEmployeeServiceIds.containsAll(existingServiceIds), "Second employee is missing some existing services");

        // Ensure second employee does NOT have any of the new services
        for (EmployeeServiceCreateDTO newService : newServices) {
            boolean hasNew = secondEmployee.getServices().stream()
                    .anyMatch(service -> service.getName().equals(newService.name()));
            assertFalse(hasNew, "Second employee should not have new service: " + newService.name());
        }
    }

    private EmployeeEntity createFirstDefaultEmployee() {
        return createEmployee("John Doe", "employeeTest1@gmail.com", branchId, existingServiceIds, newServices);
    }

    private EmployeeEntity createEmployee(String name, String email, Long branchId, Set<Long> existingServiceIds, Set<EmployeeServiceCreateDTO> newServices) {
        EmployeeRequestDTO createEmployeeDTO = EmployeeRequestDTO.builder()
                .name(name)
                .email(email)
                .existingServicesIds(existingServiceIds)
                .newServicesDTO(newServices)
                .build();
        employeeService.createEmployee(branchId, avatarFile, createEmployeeDTO);

        return employeeRepository.findByEmailAndBranchId(email, branchId);
    }
}