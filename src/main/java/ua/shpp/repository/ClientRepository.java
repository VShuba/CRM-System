package ua.shpp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.shpp.entity.ClientEntity;

import java.util.List;

public interface ClientRepository extends JpaRepository<ClientEntity, Long> {
    Page<ClientEntity> findAllByOrganizationId(Long organizationId, Pageable pageable);


    @Query(
            """
                    SELECT cl
                    FROM ClientEntity as cl
                    inner join Organization as o
                                ON cl.organization.id = o.id        
                    WHERE cl.organization.id = :organizationId
                                AND LOWER(CONCAT('%',cl.name, ' ',cl.phone, '%'))
                                            like (LOWER(CONCAT('%',:keyword ,'%')))
                                            ORDER BY cl.id LIMIT 10            
                    
                    """
    )
    public List<ClientEntity> findByKeyword(String keyword, Long organizationId);

    @Query("SELECT c.organization.id FROM ClientEntity c WHERE c.id = :clientId")
    Long findOrganizationIdByClientId(@Param("clientId") Long clientId);
}
