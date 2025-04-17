package ua.shpp.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ua.shpp.dto.BranchPatchRequestDTO;
import ua.shpp.dto.BranchRequestDTO;
import ua.shpp.dto.BranchResponseDTO;
import ua.shpp.entity.BranchEntity;
import ua.shpp.entity.Organization;
import ua.shpp.exception.BranchNotFoundException;
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

    public ResponseEntity<BranchResponseDTO> create(BranchRequestDTO requestDTO) {

        Organization org = organizationRepository.findById(requestDTO.organizationId())
                .orElseThrow(() -> new OrganizationNotFound("Organization not found"));

        BranchEntity branch = BranchEntity.builder()
                .name(requestDTO.name())
                .organization(org)
                .build();

        branchRepository.save(branch);

        return new ResponseEntity<>(mapper.branchEntityToBranchResponseDTO(branch), HttpStatus.CREATED);
    }

    public ResponseEntity<BranchResponseDTO> get(Long id) {

        BranchEntity branchEntity = branchRepository.findById(id)
                .orElseThrow(BranchNotFoundException::new);

        return new ResponseEntity<>(mapper.branchEntityToBranchResponseDTO(branchEntity), HttpStatus.OK);
    }

    public ResponseEntity<BranchResponseDTO> updateName(Long id, BranchPatchRequestDTO request) {

        BranchEntity branch = branchRepository.findById(id)
                .orElseThrow(BranchNotFoundException::new);

        branch.setName(request.name());
        branchRepository.save(branch);

        return new ResponseEntity<>(mapper.branchEntityToBranchResponseDTO(branch), HttpStatus.OK);
    }

    public void delete(Long id) {

        BranchEntity branchEntity = branchRepository.findById(id)
                .orElseThrow(BranchNotFoundException::new);

        branchRepository.delete(branchEntity);
    }
}
