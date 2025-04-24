package ua.shpp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import ua.shpp.dto.branch.BranchRequestDTO;
import ua.shpp.dto.branch.BranchResponseDTO;
import ua.shpp.dto.branch.BranchShortResponseDTO;
import ua.shpp.entity.BranchEntity;
import ua.shpp.entity.Organization;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = RoomMapper.class)
public interface BranchMapper {

    @Mapping(target = "organizationId", source = "organization", qualifiedByName = "organizationIdFromOrganization")
    @Mapping(target = "rooms", source = "rooms")
    @Mapping(target = "workingHours", source = "workingHours")
    BranchResponseDTO toResponseDTO(BranchEntity source);

    BranchShortResponseDTO toShortResponseDTO(BranchEntity source);

    BranchEntity toEntity(BranchRequestDTO source);

    @Named("organizationIdFromOrganization")
    default Long organizationIdFromOrganization(Organization org) {
        return org.getId();
    }
}
