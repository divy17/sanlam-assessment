package com.divy.sanlam.service;

import com.divy.sanlam.event.WithdrawalEvent;
import com.divy.sanlam.publisher.EventPublisher;
import com.divy.sanlam.repository.BankAccountRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.CompletableFuture;


import java.math.BigDecimal;

@Service
public class BankAccountService {

    private final JdbcTemplate jdbcTemplate;
    private final EventPublisher eventPublisher;

    public BankAccountService(JdbcTemplate jdbcTemplate, EventPublisher eventPublisher) {
        this.jdbcTemplate = jdbcTemplate;
        this.eventPublisher = eventPublisher;
    }

    public void withdraw(Long accountId, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than zero");
        }
        BigDecimal currentBalance = getBalance(accountId);

        if (currentBalance == null) {
            throw new IllegalArgumentException("Account not found");
        }

        if (currentBalance.compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient funds for withdrawal");
        }

        int updated = jdbcTemplate.update(
            "UPDATE accounts SET balance = balance - ? WHERE id = ?",
            amount, accountId
        );

        if (updated == 0) {
            throw new RuntimeException("Withdrawal failed, no rows affected");
        }

        WithdrawalEvent event = new WithdrawalEvent(amount, accountId, "SUCCESSFUL");
        CompletableFuture.runAsync(() -> eventPublisher.publish(event));
    }

    private BigDecimal getBalance(Long accountId) {
        try {
            return jdbcTemplate.queryForObject(
                "SELECT balance FROM accounts WHERE id = ?",
                new Object[]{accountId},
                BigDecimal.class
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
