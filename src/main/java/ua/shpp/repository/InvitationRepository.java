package ua.shpp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.shpp.entity.InvitationEntity;

@Repository
public interface InvitationRepository extends JpaRepository<InvitationEntity, String> {
}
