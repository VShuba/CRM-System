package ua.shpp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.shpp.dto.OrganizationResponseDTO;
import ua.shpp.entity.Organization;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING,
        uses = BranchMapper.class)
public interface OrganizationEntityToOrganizationDTOMapper {

    @Mapping(target = "branches", source = "branchEntities")
    OrganizationResponseDTO organizationEntityToOrganizationResponseDTO(Organization source);
}
