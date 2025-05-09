package ua.shpp.service.history;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SubscriptionValidatorSchedulerTest {

    @Mock
    private SubscriptionHistoryService subscriptionHistoryService;

    @InjectMocks
    private SubscriptionValidatorScheduler subscriptionValidatorScheduler;

    @Test
    void shouldRunDailyValidation() {
        // when
        subscriptionValidatorScheduler.runDailyValidation();

        // then
        verify(subscriptionHistoryService).validateActiveSubscriptions();
    }
}