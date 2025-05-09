package ua.shpp.service.history;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscriptionValidatorScheduler {
    private final SubscriptionHistoryService subscriptionHistoryService;

    /**
     * Checks subscriptions every day at midnight Kyiv time
     */
    @Scheduled(cron = "1 0 0 * * *", zone = "Europe/Kyiv")
    public void runDailyValidation() {
        subscriptionHistoryService.validateActiveSubscriptions();
    }
}
