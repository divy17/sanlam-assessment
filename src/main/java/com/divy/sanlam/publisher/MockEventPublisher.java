package com.divy.sanlam.publisher;

import com.divy.sanlam.model.WithdrawalEvent;
import org.springframework.stereotype.Component;

@Component
public class MockEventPublisher implements EventPublisher {
    @Override
    public void publish(WithdrawalEvent event) {
        System.out.println("📣 [Mock Publish] Withdrawal Event: " + event);
    }
}
