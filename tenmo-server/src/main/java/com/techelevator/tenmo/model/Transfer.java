package com.techelevator.tenmo.model;
import javax.validation.constraints.*;
import java.lang.*;
import java.math.BigDecimal;

public class Transfer {


    @Size(min = 1)
    private long transferId;

      @NotBlank
    private String transferType;

    @NotBlank
    private String transferStatus;

    @Size(min = 0)
    private long accountFrom;

    @Size( min = 0)
    private long accountTo;

      @NotNull
    @Positive
    private BigDecimal amount;


    public Transfer(long transferId, long accountFrom, long accountTo, BigDecimal amount, String transferType, String transferStatus) {
        this.transferId = transferId;
        this.transferType = transferType;
        this.transferStatus = transferStatus;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
    }

    public Transfer() {}

    public long getTransferId() {
        return transferId;
    }

    public void setTransferId(long transferId) {
        this.transferId = transferId;
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

    public long getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(long accountFromId) {
        this.accountFrom = accountFromId;
    }

    public long getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(long accountTo) {
        this.accountTo = accountTo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

}
