package com.divy.sanlam.service;

import com.divy.sanlam.event.WithdrawalEvent;
import com.divy.sanlam.model.WithdrawalRecord;
import com.divy.sanlam.publisher.EventPublisher;
import com.divy.sanlam.repository.BankAccountRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class BankAccountService {

    private final JdbcTemplate jdbcTemplate;
    private final EventPublisher eventPublisher;
    private final BankAccountRepository bankAccountRepository;

    public BankAccountService(JdbcTemplate jdbcTemplate, EventPublisher eventPublisher, BankAccountRepository bankAccountRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.eventPublisher = eventPublisher;
        this.bankAccountRepository = bankAccountRepository;
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

        jdbcTemplate.update(
            "INSERT INTO withdrawals (account_id, amount, status) VALUES (?, ?, ?)",
            accountId, amount, "SUCCESSFUL"
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

    public List<WithdrawalRecord> getWithdrawalHistory(Long accountId) {
        return bankAccountRepository.findWithdrawalsByAccountId(accountId);
    }
}
