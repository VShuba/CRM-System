package ua.shpp.dto;

import ua.shpp.model.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public record CheckDto(Long id,
                       String checkNumber,
                       LocalDateTime createAt,
                       String organizationName,
                       String branchAddress,
                       String branchPhoneNumber,
                       String customerName,
                       String customerPhoneNumber,
                       String offerName,
                       BigDecimal price,
                       PaymentMethod paymentMethod) {
}