package com.divy.sanlam.event;

import java.math.BigDecimal;

public class WithdrawalEvent {
    private BigDecimal amount;
    private Long accountId;
    private String status;

    public WithdrawalEvent(BigDecimal amount, Long accountId, String status) {
        this.amount = amount;
        this.accountId = accountId;
        this.status = status;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Long getAccountId() {
        return accountId;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "WithdrawalEvent{" +
                "amount=" + amount +
                ", accountId=" + accountId +
                ", status='" + status + '\'' +
                '}';
    }
    public String toJson() {
    return String.format("{\"amount\":\"%s\",\"accountId\":%d,\"status\":\"%s\"}",
            amount, accountId, status);
}

}
