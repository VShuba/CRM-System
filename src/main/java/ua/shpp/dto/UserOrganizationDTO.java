package ua.shpp.dto;

import ua.shpp.model.OrgRole;
import ua.shpp.model.Role;

public record UserOrganizationDTO(
        UserOrganizationIdDTO userOrganizationId,
        OrgRole role
) {
}
