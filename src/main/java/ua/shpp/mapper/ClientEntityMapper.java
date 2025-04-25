package ua.shpp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ua.shpp.dto.ClientRequestDto;
import ua.shpp.dto.ClientResponseDto;
import ua.shpp.entity.ClientEntity;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ClientEntityMapper {
    ClientEntity toEntity(ClientRequestDto clientRequestDto);

    ClientResponseDto toDto(ClientEntity clientEntity);

    List<ClientResponseDto> toDtoList(List<ClientEntity> clientEntityList);
}