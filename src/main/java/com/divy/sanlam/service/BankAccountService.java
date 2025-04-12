package com.divy.sanlam.service;

import com.divy.sanlam.model.WithdrawalEvent;
import com.divy.sanlam.publisher.EventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BankAccountService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EventPublisher eventPublisher;

    public String withdraw(Long accountId, BigDecimal amount) {
        String checkSql = "SELECT balance FROM accounts WHERE id = ?";
        BigDecimal currentBalance = jdbcTemplate.queryForObject(checkSql, new Object[]{accountId}, BigDecimal.class);

        if (currentBalance == null || currentBalance.compareTo(amount) < 0) {
            return "Insufficient funds for withdrawal";
        }

        String updateSql = "UPDATE accounts SET balance = balance - ? WHERE id = ?";
        int rows = jdbcTemplate.update(updateSql, amount, accountId);

        if (rows > 0) {
            WithdrawalEvent event = new WithdrawalEvent(amount, accountId, "SUCCESSFUL");
            eventPublisher.publish(event);
            return "Withdrawal successful";
        }

        return "Withdrawal failed";
    }
}
