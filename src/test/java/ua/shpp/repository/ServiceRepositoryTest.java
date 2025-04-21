package ua.shpp.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ua.shpp.entity.BranchEntity;
import ua.shpp.entity.Organization;
import ua.shpp.entity.ServiceEntity;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
class ServiceRepositoryTest {
    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private BranchRepository branchRepository;

    Long branchId;

    @BeforeEach
    void setUp() {
        Organization organization = new Organization();
        organization.setName("ServiceRepositoryTest Organization");
        organizationRepository.save(organization);

        BranchEntity branch = new BranchEntity();
        branch.setName("ServiceRepositoryTest Branch");
        branch.setOrganization(organization);
        branch = branchRepository.save(branch);
        branchId = branch.getId();

        ServiceEntity service1 = ServiceEntity.builder()
                .name("ServiceRepositoryTest service1")
                .color("red")
                .branch(branch)
                .build();

        ServiceEntity service2 = ServiceEntity.builder()
                .name("ServiceRepositoryTest service2")
                .color("red")
                .branch(branch)
                .build();

        serviceRepository.save(service1);
        serviceRepository.save(service2);
    }

    @Test
    void shouldReturnMatchingServiceNamesTest() {
        List<String> names = List.of("ServiceRepositoryTest service1", "ServiceRepositoryTest service2");
        List<String> found = serviceRepository.findAllServiceNamesByNamesAndBranch(names, branchId);

        assertThat(found).containsAll(names);
    }

}