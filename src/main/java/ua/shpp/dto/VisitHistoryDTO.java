package ua.shpp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import ua.shpp.model.PaymentMethodForStory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Schema(description = "Історія візитів - відповідь")
public record VisitHistoryDTO(
        @Schema(description = "Ідентифікатор історії візитів")
        Long id,

        @Schema(description = "Ідентифікатор клієнта")
        Long clientId,

        @Schema(description = "Колір привязаний до послуги")
        String serviceColor,

        @Schema(description = "Назва послуги")
        String serviceName,

        @Schema(description = "Повне ім’я тренера")
        String trainerFullName,

        @Schema(description = "Дата візиту", example = "2025-04-27")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,

        @Schema(description = "Час візиту", example = "15:30")
        @JsonFormat(pattern = "HH:mm")
        LocalTime time,

        @Schema(description = "Приміщення, де проводилося заняття", example = "Головний зал")
        String roomName,

        @Schema(description = "Метод оплати", example = "CASH")
        PaymentMethodForStory paymentMethodForStory,  // "CASH", "CARD" або "SUBSCRIPTION"

        @Schema(description = "Сума, сплачена за візит. Якщо абонемент — null", example = "120.00")
        BigDecimal amountPaid
) {
}
