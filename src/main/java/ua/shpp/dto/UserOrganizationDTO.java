package ua.shpp.dto;

import ua.shpp.model.OrgRole;

public record UserOrganizationDTO(
        UserOrganizationIdDTO userOrganizationId,
        OrgRole role
) {
}
