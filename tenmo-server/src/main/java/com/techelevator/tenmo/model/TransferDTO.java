package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class TransferDTO {
    private int fromUserId;
    private int toUserId;
    private long userId;
    private BigDecimal amount;

    public int getFrom() {
        return fromUserId;
    }

    public void setFrom(int from) {
        this.fromUserId = from;
    }

    public int getTo() {
        return toUserId;
    }

    public void setTo(int to) {
        this.toUserId = to;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}