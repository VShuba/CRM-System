package ua.shpp.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.shpp.dto.BranchPatchRequestDTO;
import ua.shpp.dto.BranchRequestDTO;
import ua.shpp.dto.BranchResponseDTO;
import ua.shpp.entity.BranchEntity;
import ua.shpp.entity.Organization;
import ua.shpp.exception.OrganizationNotFound;
import ua.shpp.mapper.BranchEntityToBranchDTOMapper;
import ua.shpp.repository.BranchRepository;
import ua.shpp.repository.OrganizationRepository;

@Service
@RequiredArgsConstructor
public class BranchService {
    private final OrganizationRepository organizationRepository;
    private final BranchRepository branchRepository;
    private final BranchEntityToBranchDTOMapper mapper;

    public BranchResponseDTO create(BranchRequestDTO requestDTO) {
        Organization org = organizationRepository.findById(requestDTO.organizationId())
                .orElseThrow(() -> new OrganizationNotFound("Organization not found"));

        BranchEntity branch = BranchEntity.builder()
                .name(requestDTO.name())
                .organization(org)
                .build();
        branchRepository.save(branch);
        return mapper.branchEntityToBranchResponseDTO(branch);
    }

    public BranchResponseDTO get(Long id) {
        BranchEntity branchEntity = branchRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Branch not found"));
        return mapper.branchEntityToBranchResponseDTO(branchEntity);
    }

    public BranchResponseDTO updateName(Long id, BranchPatchRequestDTO request) {
        BranchEntity branch = branchRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Branch not found"));

        branch.setName(request.name());
        branchRepository.save(branch);

        return mapper.branchEntityToBranchResponseDTO(branch);
    }


    public void delete(Long id) {
        BranchEntity branchEntity = branchRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can not delete it. Cause failed to find organization in DB."));

        branchRepository.delete(branchEntity);
    }
}
