package com.divy.sanlam.publisher;

import com.divy.sanlam.event.WithdrawalEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("mock")
@Component
public class MockEventPublisher implements EventPublisher {
    @Override
    public void publish(WithdrawalEvent event) {
        System.out.println("📣 [Mock Publish] Withdrawal Event: " + event);
    }
}
