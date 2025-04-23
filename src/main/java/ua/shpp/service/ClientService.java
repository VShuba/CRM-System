package ua.shpp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.shpp.dto.ClientRequestDto;
import ua.shpp.dto.ClientResponseDto;
import ua.shpp.entity.ClientEntity;
import ua.shpp.entity.Organization;
import ua.shpp.exception.OrganizationNotFound;
import ua.shpp.mapper.ClientEntityMapper;
import ua.shpp.repository.ClientRepository;
import ua.shpp.repository.OrganizationRepository;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    private final ClientEntityMapper clientEntityMapper;
    private final OrganizationRepository organizationRepository;

    public ClientResponseDto createClient(Long orgId, ClientRequestDto requestDto) {
        Organization organization = organizationRepository.findById(orgId).orElseThrow(() -> new OrganizationNotFound("Organization not found exception"));
        ClientEntity entity = ClientEntity.builder()
                .name(requestDto.name())
                .phone(requestDto.phone())
                .birthday(requestDto.birthday())
                .comment(requestDto.comment())
                .organization(organization)
                .build();

        ClientEntity saved = clientRepository.save(entity);
        return clientEntityMapper.toDto(saved);
    }
}
