package com.techelevator.tenmo.model;
import javax.validation.constraints.*;
import java.lang.*;
import java.math.BigDecimal;

public class Transfer {


    @Size(min = 1)
    private int transferId;

    @Size(min = 1)
    private int transferTypeId;

    @Size(min = 1, max=3)
    private int transferStatusId;

    @NotBlank
    private String transferType;

    @NotBlank
    private String transferStatus;

    @Size(min = 0)
    private int accountFromId;

    @Size( min = 0)
    private int accountToId;

    @NotBlank
    private String accountFromUsername;

    @NotBlank
    private String accountToUsername;

    @NotNull
    @Positive
    private BigDecimal amount;

    @NotNull
    private long fromUserId;

    @NotNull
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


    public String getAccountFromUsername() {
        return accountFromUsername;
    }

    public void setAccountFromUsername(String accountFromUsername) {
        this.accountFromUsername = accountFromUsername;
    }

    public String getAccountToUsername() {
        return accountToUsername;
    }

    public void setAccountToUsername(String accountToUsername) {
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
