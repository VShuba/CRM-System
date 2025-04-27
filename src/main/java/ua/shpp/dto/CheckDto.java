package ua.shpp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import ua.shpp.model.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public record CheckDto(
        @Schema(description = "Check id", example = "1")
        Long id,
        @Schema(description = "Check number", example = "20250426-001")
        String checkNumber,
        @Schema(description = "Creation timestamp", example = "26-04-2025 21:35:00",
                type = "string", format = "date-time")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
        LocalDateTime createdAt,
        @Schema(description = "Organization name", example = "Name")
        String organizationName,
        @Schema(description = "Organization address", example = "Address 1")
        String branchAddress,
        @Schema(description = "Branch phone number", example = "+380593452365")
        String branchPhoneNumber,
        @Schema(description = "Customer name", example = "Name Surname")
        String customerName,
        @Schema(description = "Customer phone number", example = "+380593452366")
        String customerPhoneNumber,
        @Schema(description = "Subscription offer name", example = "Name dance")
        String offerName,
        @Schema(description = "Subscription offer price", example = "5000")
        BigDecimal price,
        @Schema(description = "Payment method")
        PaymentMethod paymentMethod) {
}