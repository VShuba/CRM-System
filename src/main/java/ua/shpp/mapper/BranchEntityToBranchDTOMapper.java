package ua.shpp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import ua.shpp.dto.BranchRequestDTO;
import ua.shpp.dto.BranchResponseDTO;
import ua.shpp.dto.BranchShortResponseDTO;
import ua.shpp.entity.BranchEntity;
import ua.shpp.entity.Organization;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = RoomEntityToRoomDTOMapper.class)
public interface BranchEntityToBranchDTOMapper {

    @Mapping(target = "organizationId", source = "organization", qualifiedByName = "organizationIdFromOrganization")
    @Mapping(target = "rooms", source = "rooms")
    @Mapping(target = "workingHours", source = "workingHours")
    BranchResponseDTO branchEntityToBranchResponseDTO(BranchEntity source);
    BranchShortResponseDTO branchEntityToBranchShortResponseDTO(BranchEntity source);

    BranchEntity branchRequestDTOToBranchEntity(BranchRequestDTO source);

    @Named("organizationIdFromOrganization")
    default Long organizationIdFromOrganization(Organization org) {
        return org.getId();
    }
}
