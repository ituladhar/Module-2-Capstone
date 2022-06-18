package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {


    private long transferId;


    private long transferTypeId;


    private long transferStatusId;


    private String transferType;


    private String transferStatus;


    private long accountFromId;


    private long accountToId;


    private long accountFromUsername;


    private long accountToUsername;


    private BigDecimal amount;


    private long fromUserId;


    private long toUserId;

    public Transfer(long transferId, long accountFromUsername, long accountToUsername, BigDecimal amount, String transferType, String transferStatus) {
        this.transferId = transferId;
        this.transferType = transferType;
        this.transferStatus = transferStatus;
        this.accountFromUsername = accountFromUsername;
        this.accountToUsername = accountToUsername;
        this.amount = amount;
    }
    public Transfer() {}

    public long getTransferId() {
        return transferId;
    }

    public void setTransferId(long transferId) {
        this.transferId = transferId;
    }

    public long getTransferTypeId() {
        return transferTypeId;
    }

    public void setTransferTypeId(long transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public long getTransferStatusId() {
        return transferStatusId;
    }

    public void setTransferStatusId(long transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    public String getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(String transferStatus) {
        this.transferStatus = transferStatus;
    }

    public long getAccountFromId() {
        return accountFromId;
    }

    public void setAccountFromId(long accountFromId) {
        this.accountFromId = accountFromId;
    }

    public long getAccountToId() {
        return accountToId;
    }

    public void setAccountToId(long accountToId) {
        this.accountToId = accountToId;
    }


    public long getAccountFromUsername() {

        return accountFromUsername;
    }

    public void setAccountFromUsername(long accountFromUsername) {
        this.accountFromUsername = accountFromUsername;
    }

    public long getAccountToUsername() {
        return accountToUsername;
    }

    public void setAccountToUsername(long accountToUsername) {
        this.accountToUsername = accountToUsername;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }


    @Override
    public String toString() {
        return "Transfer{" +
                "transferId=" + transferId +
                ", transferTypeId=" + transferTypeId +
                ", transferStatusId=" + transferStatusId +
                ", transferType='" + transferType + '\'' +
                ", transferStatus='" + transferStatus + '\'' +
                ", accountFromId=" + accountFromId +
                ", accountToId=" + accountToId +
                ", accountFromUsername='" + accountFromUsername + '\'' +
                ", accountToUsername='" + accountToUsername + '\'' +
                ", amount=" + amount +
                '}';
    }


}

