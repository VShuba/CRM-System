package ua.shpp.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ua.shpp.dto.BranchPatchRequestDTO;
import ua.shpp.dto.BranchRequestDTO;
import ua.shpp.dto.BranchResponseDTO;
import ua.shpp.dto.WorkingHourDTO;
import ua.shpp.entity.BranchEntity;
import ua.shpp.entity.Organization;
import ua.shpp.exception.BranchNotFoundException;
import ua.shpp.exception.OrganizationNotFound;
import ua.shpp.mapper.BranchEntityToBranchDTOMapper;
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
    private final BranchEntityToBranchDTOMapper branchMapper;
    private final WorkingHourMapper workingHourMapper;

    public BranchResponseDTO create(BranchRequestDTO requestDTO) {

        Organization org = organizationRepository.findById(requestDTO.organizationId())
                .orElseThrow(() -> new OrganizationNotFound("Organization not found"));

        BranchEntity branch = BranchEntity.builder()
                .name(requestDTO.name())
                .organization(org)
                .build();

        branchRepository.save(branch);

        return branchMapper.branchEntityToBranchResponseDTO(branch);
    }

    public BranchResponseDTO get(Long id) {

        BranchEntity branchEntity = branchRepository.findById(id)
                .orElseThrow(BranchNotFoundException::new);

        return branchMapper.branchEntityToBranchResponseDTO(branchEntity);
    }

    public BranchResponseDTO updateName(Long id, BranchPatchRequestDTO request) {

        BranchEntity branch = branchRepository.findById(id)
                .orElseThrow(BranchNotFoundException::new);

        branch.setName(request.name());
        branchRepository.save(branch);

        return branchMapper.branchEntityToBranchResponseDTO(branch);
    }

    public BranchResponseDTO updateWorkingHours(Long orgId, Long branchId, @Valid List<WorkingHourDTO> workingHourDTOS) {
        BranchEntity branchEntity = validateBranch(orgId, branchId);

        List<WorkingHour> workingHours = workingHourDTOS.stream().map(workingHourMapper::toEntity).collect(Collectors.toList());
        branchEntity.setWorkingHours(workingHours);
        branchRepository.save(branchEntity);

        return branchMapper.branchEntityToBranchResponseDTO(branchEntity);
    }

    public void delete(Long id) {

        BranchEntity branchEntity = branchRepository.findById(id)
                .orElseThrow(BranchNotFoundException::new);

        branchRepository.delete(branchEntity);
    }

    protected BranchEntity validateBranch(Long orgId, Long branchId) {
        BranchEntity branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new BranchNotFoundException("Branch with id: " + branchId + " not found"));

        if (!branch.getOrganization().getId().equals(orgId)) {
            throw new IllegalArgumentException("Branch does not belong to the given organization");
        }

        return branch;
    }
}
