package com.divy.sanlam.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.divy.sanlam.model.WithdrawalRecord;


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
    public List<WithdrawalRecord> findWithdrawalsByAccountId(Long accountId) {
        return jdbcTemplate.query(
            "SELECT * FROM withdrawals WHERE account_id = ? ORDER BY created_at DESC",
            new Object[]{accountId},
            (rs, rowNum) -> {
                WithdrawalRecord record = new WithdrawalRecord();
                record.setId(rs.getLong("id"));
                record.setAccountId(rs.getLong("account_id"));
                record.setAmount(rs.getBigDecimal("amount"));
                record.setStatus(rs.getString("status"));
                record.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                return record;
            }
        );
    }

}
