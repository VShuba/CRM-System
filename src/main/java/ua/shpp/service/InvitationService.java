package ua.shpp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.shpp.dto.InvitationRequestDTO;
import ua.shpp.dto.ResponseInvitationDTO;
import ua.shpp.entity.InvitationEntity;
import ua.shpp.entity.Organization;
import ua.shpp.entity.UserEntity;
import ua.shpp.model.InvitationStatus;
import ua.shpp.model.Role;
import ua.shpp.repository.InvitationRepository;
import ua.shpp.repository.OrganizationRepository;
import ua.shpp.repository.UserRepository;

import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@Slf4j
@Service
public class InvitationService {
    private final InvitationRepository invitationRepository;

    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;

    public InvitationService(InvitationRepository invitationRepository, UserRepository userRepository, OrganizationRepository organizationRepository) {
        this.invitationRepository = invitationRepository;
        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
    }


    public ResponseInvitationDTO createInvitationLink(InvitationRequestDTO invitationRequestDTO, String currentUser) {
        UserEntity recipientUser = userRepository.findByEmail(invitationRequestDTO.email())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Organization organization = organizationRepository.findById(invitationRequestDTO.organizationId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));


        UserEntity creator = userRepository.findById(Long.parseLong(currentUser))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String invitationLink = getBase64UUID();
        InvitationEntity invitationEntity = InvitationEntity.builder()
                .invitationId(invitationLink)
                //todo
                .role(Role.MANAGER)
                .creator(creator)
                //todo
                .expiresAt(LocalDateTime.now().plusDays(10))
                .recipient(recipientUser)
                .status(InvitationStatus.ACTIVE)
                .organization(organization).build();
        invitationRepository.save(invitationEntity);
        return new ResponseInvitationDTO(invitationLink);
    }

    private static String getBase64UUID() {
        UUID uuid = UUID.randomUUID();
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[16]);
        byteBuffer.putLong(uuid.getMostSignificantBits());
        byteBuffer.putLong(uuid.getLeastSignificantBits());
        return Base64.getUrlEncoder().withoutPadding()
                .encodeToString(byteBuffer.array());
    }


}
