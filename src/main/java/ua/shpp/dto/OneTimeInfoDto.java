package ua.shpp.dto;

public record OneTimeInfoDto(Long id,
                             Long clientId,
                             Long oneTimeId,
                             Boolean visitUsed,
                             Boolean paid,
                             Long checkId) {
}