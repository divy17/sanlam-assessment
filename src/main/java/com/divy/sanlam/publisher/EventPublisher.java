package com.divy.sanlam.publisher;

import com.divy.sanlam.event.WithdrawalEvent;

public interface EventPublisher {
    void publish(WithdrawalEvent event);
}
