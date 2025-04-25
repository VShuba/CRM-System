package ua.shpp.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.shpp.dto.branch.BranchPatchRequestDTO;
import ua.shpp.dto.branch.BranchRequestDTO;
import ua.shpp.dto.branch.BranchResponseDTO;
import ua.shpp.dto.branch.WorkingHourDTO;
import ua.shpp.entity.BranchEntity;
import ua.shpp.entity.Organization;
import ua.shpp.exception.BranchAlreadyExistsException;
import ua.shpp.exception.BranchNotFoundException;
import ua.shpp.exception.BranchOrganizationMismatchException;
import ua.shpp.exception.OrganizationNotFound;
import ua.shpp.mapper.BranchMapper;
import ua.shpp.mapper.WorkingHourMapper;
import ua.shpp.model.WorkingHour;
import ua.shpp.repository.BranchRepository;
import ua.shpp.repository.OrganizationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BranchService {
    private final OrganizationRepository organizationRepository;
    private final BranchRepository branchRepository;
    private final BranchMapper branchMapper;
    private final WorkingHourMapper workingHourMapper;

    public BranchResponseDTO create(Long orgId, BranchRequestDTO requestDTO) {

        if (branchRepository.existsByNameAndOrganizationId(requestDTO.name(), orgId)) {
            throw new BranchAlreadyExistsException("Branch with name " + requestDTO.name() + " already exists");
        }

        Organization org = organizationRepository.findById(orgId)
                .orElseThrow(() -> new OrganizationNotFound("Organization not found"));

        BranchEntity branch = BranchEntity.builder()
                .name(requestDTO.name())
                .organization(org)
                .build();

        branchRepository.save(branch);

        return branchMapper.toResponseDTO(branch);
    }

    public BranchResponseDTO get(Long orgId, Long branchId) {
        BranchEntity branchEntity = validateBranch(orgId, branchId);
        return branchMapper.toResponseDTO(branchEntity);
    }

    public Page<BranchResponseDTO> getAll(Long orgId, Pageable pageable) {
        if (!organizationRepository.existsById(orgId))
            throw new OrganizationNotFound("Organization with id: " + orgId + " not found");
        Page<BranchEntity> allByOrganizationId = branchRepository.findAllByOrganizationId(orgId, pageable);
        return allByOrganizationId.map(branchMapper::toResponseDTO);
    }

    public BranchResponseDTO updateName(Long orgId, Long branchId, BranchPatchRequestDTO request) {

        if (branchRepository.existsByNameAndOrganizationId(request.name(), orgId)) {
            throw new BranchAlreadyExistsException("Branch with name " + request.name() + " already exists");
        }

        BranchEntity branchEntity = validateBranch(orgId, branchId);
        branchEntity.setName(request.name());
        branchRepository.save(branchEntity);

        return branchMapper.toResponseDTO(branchEntity);
    }

    public BranchResponseDTO updateWorkingHours(Long orgId, Long branchId, @Valid List<WorkingHourDTO> workingHourDTOS) {
        BranchEntity branchEntity = validateBranch(orgId, branchId);

        List<WorkingHour> workingHours = workingHourDTOS.stream().map(workingHourMapper::toEntity).collect(Collectors.toList());
        branchEntity.setWorkingHours(workingHours);
        branchRepository.save(branchEntity);

        return branchMapper.toResponseDTO(branchEntity);
    }

    public void delete(Long orgId, Long branchId) {

        BranchEntity branchEntity = validateBranch(orgId, branchId);

        branchRepository.delete(branchEntity);
    }

    protected BranchEntity validateBranch(Long orgId, Long branchId) {
        BranchEntity branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new BranchNotFoundException("Branch with branchId: " + branchId + " not found"));

        if (!branch.getOrganization().getId().equals(orgId)) {
            throw new BranchOrganizationMismatchException(branchId, orgId);
        }

        return branch;
    }
}
