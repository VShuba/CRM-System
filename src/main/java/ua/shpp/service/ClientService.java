package ua.shpp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.shpp.dto.ClientRequestDto;
import ua.shpp.dto.ClientResponseDto;
import ua.shpp.entity.ClientEntity;
import ua.shpp.entity.Organization;
import ua.shpp.mapper.ClientEntityMapper;
import ua.shpp.repository.ClientRepository;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    private final ClientEntityMapper clientEntityMapper;
    private final OrganizationService organizationService;

    public ClientResponseDto createClient(Long orgId, ClientRequestDto requestDto) {
        Organization organization = organizationService.getEntityById(orgId);
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
