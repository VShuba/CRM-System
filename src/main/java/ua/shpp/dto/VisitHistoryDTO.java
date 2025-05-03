package ua.shpp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import ua.shpp.model.PaymentMethodForStory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Schema(description = "History of visits")
public record VisitHistoryDTO(
        @Schema(description = "Visit history identifier", example = "1")
        Long id,

        @Schema(description = "Customer ID", example = "1")
        Long clientId,

        @Schema(description = "Color is tied to the service", example = "#AABBCC")
        String serviceColor,

        @Schema(description = "Name of the service", example = "Pool-dance")
        String serviceName,

        @Schema(description = "Full name of the trainer", example = "Alina Kucheryava")
        String trainerFullName,

        @Schema(description = "Date of visit", example = "2025-04-27")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,

        @Schema(description = "Time of visit", example = "15:30")
        @JsonFormat(pattern = "HH:mm")
        LocalTime time,

        @Schema(description = "The room where the class was held", example = "The main hall")
        String roomName,

        @Schema(description = "Payment method", example = "CASH")
        PaymentMethodForStory paymentMethodForStory,  // "CASH", "CARD", "SUBSCRIPTION", "SKIPPED"

        @Schema(description = "Amount paid for the visit. If subscription is null", example = "120.00")
        BigDecimal amountPaid
) {
}
