package ua.shpp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.shpp.entity.ClientEntity;

public interface ClientRepository extends JpaRepository<ClientEntity, Long> {
}