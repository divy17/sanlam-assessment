package com.divy.sanlam.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public class BankAccountRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BankAccountRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public BigDecimal getBalance(Long accountId) {
        String sql = "SELECT balance FROM accounts WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{accountId}, BigDecimal.class);
    }

    public int updateBalance(Long accountId, BigDecimal amount) {
        String sql = "UPDATE accounts SET balance = balance - ? WHERE id = ?";
        return jdbcTemplate.update(sql, amount, accountId);
    }
}
