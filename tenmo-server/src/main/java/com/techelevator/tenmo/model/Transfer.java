package com.techelevator.tenmo.model;
import java.lang.*;
import java.math.BigDecimal;
public class Transfer {
    private int transferId;
    private int transferTypeId;
    private int transferStatusId;
    private String transferType;
    private String transferStatus;
    private int accountFromId;
    private int accountToId;
    private String accountFromUsername;
    private String accountToUsername;
    private double amount;
    private long fromUserId;
    private long toUserId;

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public int getTransferTypeId() {
        return transferTypeId;
    }

    public void setTransferTypeId(int transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public int getTransferStatusId() {
        return transferStatusId;
    }

    public void setTransferStatusId(int transferStatusId) {
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

    public int getAccountFromId() {
        return accountFromId;
    }

    public void setAccountFromId(int accountFromId) {
        this.accountFromId = accountFromId;
    }

    public int getAccountToId() {
        return accountToId;
    }

    public void setAccountToId(int accountToId) {
        this.accountToId = accountToId;
    }

    public String getAccountUserFrom() {
        return accountUserFrom;
    }

    public void setAccountUserFrom(String accountUserFrom) {
        this.accountUserFrom = accountUserFrom;
    }

    public String getAccountUserTo() {
        return accountUserTo;
    }

    public void setAccountUserTo(String accountUserTo) {
        this.accountUserTo = accountUserTo;
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
                ", accountUserFrom='" + accountUserFrom + '\'' +
                ", accountUserTo='" + accountUserTo + '\'' +
                ", amount=" + amount +
                '}';
    }
}
