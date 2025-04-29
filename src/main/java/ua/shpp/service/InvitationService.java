package ua.shpp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.shpp.dto.InvitationRequestDTO;
import ua.shpp.dto.ResponseInvitationDTO;
import ua.shpp.entity.*;
import ua.shpp.exception.*;
import ua.shpp.model.InvitationStatus;
import ua.shpp.model.OrgRole;
import ua.shpp.repository.InvitationRepository;
import ua.shpp.repository.OrganizationRepository;
import ua.shpp.repository.UserOrganizationRepository;
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

    private final UserOrganizationRepository userOrganizationRepository;

    private final int DEFAULT_EXPIRATION_DAYS = 10;

    public InvitationService(InvitationRepository invitationRepository, UserRepository userRepository, OrganizationRepository organizationRepository, UserOrganizationRepository userOrganizationRepository) {
        this.invitationRepository = invitationRepository;
        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
        this.userOrganizationRepository = userOrganizationRepository;
    }

    //todo if user_id not a number

    public ResponseInvitationDTO createInvitationLink(InvitationRequestDTO invitationRequestDTO, String currentUser) {

        Organization organization = organizationRepository.findById(invitationRequestDTO.organizationId())
                .orElseThrow(() -> new OrganizationNotFound("Organization not found"));

        UserEntity creator = userRepository.findById(Long.parseLong(currentUser))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String invitationLink = getBase64UUID();
        InvitationEntity invitationEntity = InvitationEntity.builder()
                .invitationId(invitationLink)
                .role(OrgRole.MANAGER) //TODO  <-- UPD по лінку він отримає роль manager`a
                .creator(creator)
                .expiresAt(LocalDateTime.now().plusDays(DEFAULT_EXPIRATION_DAYS))
                .recipientEmail(invitationRequestDTO.email())
                .status(InvitationStatus.ACTIVE)
                .organization(organization).build();
        invitationRepository.save(invitationEntity);
        return new ResponseInvitationDTO(invitationLink);
    }

    public ResponseInvitationDTO acceptLink(String invitationId, String currentUser) {
        InvitationEntity invitationEntity = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new InvitationNotFoundException("Invitation not found"));
        if (invitationEntity.getStatus() == InvitationStatus.USED)
            throw new InvitationAlreadyUsedException("Invitation is already accepted");
        if (invitationEntity.getExpiresAt().isBefore(LocalDateTime.now()))
            throw new InvitationExpiredException("Invitation is expired");
        UserEntity recipient = userRepository.findById(Long.valueOf(currentUser))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (!invitationEntity.getRecipientEmail().equals(recipient.getEmail()))
            throw new WrongInvitationRecipient("Wrong user for invitation");
        //todo delete entry
        //todo organization deleted


        UserOrganization userOrganization = UserOrganization.builder()
                .id(new UserOrganizationId(recipient.getId(),
                        invitationEntity.getOrganization().getId()
                ))
                .user(recipient)
                .organization(invitationEntity.getOrganization())
                .role(invitationEntity.getRole()).build();

        userOrganizationRepository.save(userOrganization);

        invitationEntity.setStatus(InvitationStatus.USED);
        invitationRepository.save(invitationEntity);
        return null;
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
