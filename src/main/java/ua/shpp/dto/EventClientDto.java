package ua.shpp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import ua.shpp.model.ClientEventStatus;

public record EventClientDto(
        @Schema(description = "Ідентифікатор клієнта", example = "1")
        Long clientId,

        @Schema(description = "Ідентифікатор події, в якій бере участь клієнт (розклад події)", example = "1")
        Long scheduleId,

        @Schema(description = "Статус участі клієнта в події ", example = "ASSIGNED")
        ClientEventStatus clientEventStatus,

        @Schema(description = "Ідентифікатор придбаного одноразового візиту (якщо є)")
        Long oneTimeInfoId,

        @Schema(description = "Ідентифікатор придбаного абонементу (якщо є)")
        Long subscriptionInfoId
) {
}
