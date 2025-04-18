package ua.shpp.dto;

import ua.shpp.model.Role;

public record UserOrganizationDTO(
        UserOrganizationIdDTO userOrganizationId,
        Role role
) {
}
