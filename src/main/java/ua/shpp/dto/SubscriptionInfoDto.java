package ua.shpp.dto;

import java.time.LocalDate;

public record SubscriptionInfoDto(Long id,
                                  Long clientId,
                                  Long subscriptionId,
                                  byte visitsUsed,
                                  LocalDate expirationDate,
                                  Boolean paid,
                                  Long checkId) {
}