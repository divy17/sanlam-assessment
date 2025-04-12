package com.divy.sanlam.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class WithdrawalRecord {
    private Long id;
    private Long accountId;
    private BigDecimal amount;
    private String status;
    private LocalDateTime createdAt;

    // No-arg constructor (required for row mapping)
    public WithdrawalRecord() {}

    // All-args constructor (optional convenience)
    public WithdrawalRecord(Long accountId, BigDecimal amount, String status, LocalDateTime createdAt) {
        this.accountId = accountId;
        this.amount = amount;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
