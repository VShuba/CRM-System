package ua.shpp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import ua.shpp.model.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public record CheckDto(
        @Schema(description = "Check id", example = "1")
        Long id,

        @Schema(description = "Check number", example = "20250426-001")
        @Size(min = 1, max = 50, message = "The check number must be 1-50 characters long")
        String checkNumber,

        @Schema(description = "Creation timestamp", example = "26-04-2025 21:35:00",
                type = "string", format = "date-time")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
        LocalDateTime createdAt,

        @Schema(description = "Organization name", example = "Name")
        @Size(min = 5, max = 50, message = "The organization name must be 5-50 characters long")
        String organizationName,

        @Schema(description = "Organization address", example = "Address 1")
        @Size(min = 5, max = 50, message = "The organization address must be 5-50 characters long")
        String branchAddress,

        @Schema(description = "Branch phone number", example = "+380593452365")
        @Pattern(message = "Wrong phone format",
                regexp = "^\\+?3?8?[-\\s\\(]?(0\\d{2})[-\\s\\)]?\\s?\\d{3}[-\\s]?\\d{2}[-\\s]?\\d{2}$")
        String branchPhoneNumber,

        @Schema(description = "Customer name", example = "Name Surname")
        @Size(min = 3, max = 50, message = "The customer name must be 3-50 characters long")
        String customerName,

        @Schema(description = "Customer phone number", example = "+380593452366")
        @Pattern(message = "Wrong phone format",
                regexp = "^\\+?3?8?[-\\s\\(]?(0\\d{2})[-\\s\\)]?\\s?\\d{3}[-\\s]?\\d{2}[-\\s]?\\d{2}$")
        String customerPhoneNumber,

        @Schema(description = "Subscription offer name", example = "Name dance")
        @Size(min = 3, max = 50, message = "The subscription offer name must be 3-50 characters long")
        String offerName,

        @Schema(description = "Subscription offer price", example = "5000")
        BigDecimal price,

        @Schema(description = "Payment method")
        PaymentMethod paymentMethod) {
}