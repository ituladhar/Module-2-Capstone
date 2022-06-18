package com.techelevator.tenmo.model;
import javax.validation.constraints.*;
import java.lang.*;
import java.math.BigDecimal;

public class Transfer {
    @Size(min = 1)
    private long transferId;

    @NotBlank
    private String transferTypeDesc;

   @NotBlank
    private String transferStatusDesc;

    @Size(min = 0)
    private long accountFrom;

    @Size( min = 0)
    private long accountTo;

    @NotNull
    @Positive
    private BigDecimal amount;


    public Transfer(long transferId, long accountFrom, long accountTo, BigDecimal amount, String transferTypeDesc, String transferStatusDesc) {
        this.transferId = transferId;
        this.transferTypeDesc = transferTypeDesc;
        this.transferStatusDesc = transferStatusDesc;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
    }

    public Transfer() {

    }


    public long getTransferId() {
        return transferId;
    }

    public void setTransferId(long transferId) {
        this.transferId = transferId;
    }

    public String getTransferTypeDesc() {
        return transferTypeDesc;
    }

    public void setTransferTypeDesc(String transferTypeDesc) {
        this.transferTypeDesc = transferTypeDesc;
    }

    public String getTransferStatusDesc() {
        return transferStatusDesc;
    }

    public void setTransferStatusDesc(String transferStatusDesc) {
        this.transferStatusDesc = transferStatusDesc;
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
