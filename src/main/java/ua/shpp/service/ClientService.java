package ua.shpp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.shpp.dto.ClientRequestDto;
import ua.shpp.dto.ClientResponseDto;
import ua.shpp.entity.ClientEntity;
import ua.shpp.entity.Organization;
import ua.shpp.exception.ClientNotFoundException;
import ua.shpp.exception.ClientOrganizationMismatchException;
import ua.shpp.mapper.ClientEntityMapper;
import ua.shpp.repository.ClientRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientService {
    private final ClientRepository clientRepository;
    private final ClientEntityMapper clientEntityMapper;
    private final OrganizationService organizationService;

    public ClientResponseDto createClient(Long orgId, ClientRequestDto requestDto) {
        log.info("Creating new client for organization ID: {}", orgId);

        Organization organization = organizationService.getEntityByIdOrThrow(orgId);
        ClientEntity entity = ClientEntity.builder()
                .name(requestDto.name())
                .phone(requestDto.phone())
                .birthday(requestDto.birthday())
                .comment(requestDto.comment())
                .organization(organization)
                .build();

        ClientEntity saved = clientRepository.save(entity);
        log.info("Created client with ID: {}", saved.getId());

        return clientEntityMapper.toDto(saved);
    }

    public ClientResponseDto updateClient(Long orgId, Long clientId, ClientRequestDto requestDto) {
        log.info("Updating client with ID: {} for organization ID: {}", clientId, orgId);

        ClientEntity client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException(clientId));

        Organization organization = organizationService.getEntityByIdOrThrow(orgId);

        if (!client.getOrganization().getId().equals(orgId)) {
            throw new ClientOrganizationMismatchException();
        }

        client.setName(requestDto.name());
        client.setPhone(requestDto.phone());
        client.setBirthday(requestDto.birthday());
        client.setComment(requestDto.comment());
        client.setOrganization(organization);

        ClientEntity updated = clientRepository.save(client);
        log.info("Updated client with ID: {}", updated.getId());

        return clientEntityMapper.toDto(updated);
    }

    public ClientResponseDto getClientById(Long orgId, Long clientId) {
        log.info("Fetching client with ID: {} for organization ID: {}", clientId, orgId);

        ClientEntity client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException(clientId));

        if (!client.getOrganization().getId().equals(orgId)) {
            throw new ClientOrganizationMismatchException();
        }

        log.info("Client with ID: {} found", clientId);
        return clientEntityMapper.toDto(client);
    }

    public Page<ClientResponseDto> getClientsByOrganization(Long orgId, Pageable pageable) {
        log.info("Fetching clients for organization ID: {}", orgId);

        Page<ClientEntity> page = clientRepository.findAllByOrganizationId(orgId, pageable);
        log.info("Fetched {} clients for organization ID: {}", page.getTotalElements(), orgId);

        return page.map(clientEntityMapper::toDto);
    }

    public List<ClientResponseDto> getClientsByKeyword(String keyword, Long orgId) {
        return clientEntityMapper.toDtoList(clientRepository.findByKeyword(keyword, orgId));
    }

    public void delete(Long orgId, Long clientId) {
        log.info("Deleting client with ID: {} for organization ID: {}", clientId, orgId);

        ClientEntity client = clientRepository.findById(clientId)
                .orElseThrow(() -> {
                    log.error("Client with ID: {} not found", clientId);
                    return new ClientNotFoundException(clientId);
                });

        if (!client.getOrganization().getId().equals(orgId)) {
            log.warn("Client organization mismatch: client {} does not belong to organization {}", clientId, orgId);
            throw new ClientOrganizationMismatchException();
        }

        clientRepository.delete(client);
    }
}
