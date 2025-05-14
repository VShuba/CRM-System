package ua.shpp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ua.shpp.model.PaymentMethodForStory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Schema(description = "History of visits")
public record VisitHistoryDTO(
        @Schema(description = "Visit history identifier", example = "1")
        @NotNull
        Long id,

        @Schema(description = "Customer ID", example = "1")
        @NotNull
        Long clientId,

        @Schema(description = "Color is tied to the service", example = "#AABBCC")
        @Size(min = 3, max = 10, message = "The color must be 3-10 characters long")
        String serviceColor,

        @Schema(description = "Name of the service", example = "Pool-dance")
        @Size(min = 3, max = 50, message = "The service name must be 3-50 characters long")
        String serviceName,

        @Schema(description = "Full name of the trainer", example = "Alina Kucheryava")
        @Size(min = 3, max = 70, message = "The trainer name must be 3-70 characters long")
        String trainerFullName,

        @Schema(description = "Date of visit", example = "2025-04-27")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,

        @Schema(description = "Time of visit", example = "15:30")
        @JsonFormat(pattern = "HH:mm")
        LocalTime time,

        @Schema(description = "The room where the class was held", example = "The main hall")
        @Size(min = 3, max = 50, message = "The room name must be 3-50 characters long")
        String roomName,

        @Schema(description = "Payment method", example = "CASH")
        PaymentMethodForStory paymentMethodForStory,  // "CASH", "CARD", "SUBSCRIPTION", "SKIPPED"

        @Schema(description = "Amount paid for the visit. If subscription is null", example = "120.00")
        BigDecimal amountPaid
) {
}
