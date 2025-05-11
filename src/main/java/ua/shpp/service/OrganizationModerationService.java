package ua.shpp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.shpp.dto.OrganizationModerationDTO;
import ua.shpp.entity.Organization;
import ua.shpp.entity.OrganizationAccessEntity;
import ua.shpp.exception.EntityNotFoundException;
import ua.shpp.mapper.OrganizationModerationMapper;
import ua.shpp.repository.OrganizationAccessRepository;
import ua.shpp.repository.OrganizationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizationModerationService {
    private final OrganizationRepository organizationRepository;
    private final OrganizationAccessRepository accessRepository;
    private final OrganizationModerationMapper organizationModerationMapper;

    public List<OrganizationModerationDTO> getAllForModeration() {
        return organizationRepository.findAll().stream()
                .map(org -> {
                    boolean access = accessRepository.findById(org.getId())
                            .map(OrganizationAccessEntity::isAccessAllowed)
                            .orElse(false);
                    return organizationModerationMapper.toDto(org, access);
                })
                .toList();
    }

    public void deleteOrganization(Long id) {
        organizationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Organization not found"));

        accessRepository.deleteById(id);
        organizationRepository.deleteById(id);
    }

    public void toggleAccess(Long id) {
        Organization org = organizationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Organization not found"));

        OrganizationAccessEntity access = accessRepository.findById(id)
                .orElse(OrganizationAccessEntity.builder()
                        .organization(org)
                        .accessAllowed(false)
                        .build());

        access.setAccessAllowed(!access.isAccessAllowed());
        accessRepository.save(access);
    }
}
