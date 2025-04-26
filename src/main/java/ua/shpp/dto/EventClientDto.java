package ua.shpp.dto;

import ua.shpp.model.ClientEventStatus;

public record EventClientDto(
        Long clientId,
        Long scheduleId,
        ClientEventStatus clientEventStatus
) {
}
