package com.divy.sanlam.publisher;

import com.divy.sanlam.model.WithdrawalEvent;

public interface EventPublisher {
    void publish(WithdrawalEvent event);
}
