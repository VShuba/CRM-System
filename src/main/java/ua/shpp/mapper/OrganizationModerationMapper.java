package ua.shpp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ua.shpp.dto.BranchForModerationDTO;
import ua.shpp.dto.OrganizationModerationDTO;
import ua.shpp.entity.BranchEntity;
import ua.shpp.entity.Organization;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrganizationModerationMapper {

    @Mapping(target = "accessAllowed", source = "accessAllowed")
    @Mapping(target = "branches", source = "organization.branchEntities")
    OrganizationModerationDTO toDto(Organization organization, boolean accessAllowed);

    List<BranchForModerationDTO> toBranchDtoList(List<BranchEntity> branches);
}
