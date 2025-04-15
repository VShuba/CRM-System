package ua.shpp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.shpp.entity.InvitationEntity;

public interface InvitationRepository extends JpaRepository<InvitationEntity, String> {
}
