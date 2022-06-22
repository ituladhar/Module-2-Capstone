package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {
    public static final String TEXT_RESET = "\u001B[0m";
    public static final String TEXT_BLACK = "\u001B[30m";
    public static final String TEXT_RED = "\u001B[31m";
    public static final String TEXT_GREEN = "\u001B[32m";
    public static final String TEXT_YELLOW = "\u001B[33m";
    public static final String TEXT_BLUE = "\u001B[34m";
    public static final String TEXT_PURPLE = "\u001B[35m";
    public static final String TEXT_CYAN = "\u001B[36m";
    public static final String TEXT_WHITE = "\u001B[37m";
    private long transferId;
    private long transferTypeId;
    private long transferStatusId;
    private String transferTypeDesc;
    private String transferStatusDesc;
    private long accountFrom;
    private long accountTo;
    private BigDecimal amount;

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

    public void setAccountFrom(long accountFrom) {
        this.accountFrom = accountFrom;
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

    @Override

    public String toString() {
        return  TEXT_BLUE + "\n\n\t\t\t\t\t\tId:\t\t "  + TEXT_RESET + transferId +
                TEXT_YELLOW + "\n\t\t\t\t\t\tType:\t "  + transferTypeDesc +
                TEXT_CYAN +"\n\t\t\t\t\t\tStatus:\t "  + transferStatusDesc +
                TEXT_GREEN +"\n\t\t\t\t\t\tFrom:\t "  + TEXT_RESET + (accountFrom -1000) +
                TEXT_RED +"\n\t\t\t\t\t\tTo:\t\t "  + TEXT_RESET + (accountTo -1000) +
                TEXT_PURPLE +"\n\t\t\t\t\t\tAmount:\t " + TEXT_RESET + "$" + amount + "\n\n";

    }
}

