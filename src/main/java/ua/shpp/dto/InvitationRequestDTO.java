package ua.shpp.dto;

import jakarta.validation.constraints.Email;

public record InvitationRequestDTO(
        @Email String email,
        Long organizationId) {
}
