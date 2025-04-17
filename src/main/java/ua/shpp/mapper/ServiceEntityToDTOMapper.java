package ua.shpp.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.shpp.dto.ServiceResponseDTO;
import ua.shpp.entity.ServiceEntity;

@Component
@RequiredArgsConstructor
public class ServiceEntityToDTOMapper {

    public ServiceResponseDTO toResponse(ServiceEntity entity) {
        return new ServiceResponseDTO(
                entity.getId(),
                entity.getName(),
                entity.getColor(),
                entity.getBranch() != null ? entity.getBranch().getName() : null,
                entity.getRoom() != null ? entity.getRoom().getName() : null
        );
    }

}
