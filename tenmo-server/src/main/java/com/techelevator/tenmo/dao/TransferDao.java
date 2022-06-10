package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;

public interface TransferDao {
    void transferMoney(Transfer transfer);
    void viewTransfers(int id);
    void viewPendingTransfer(int id);
    BigDecimal sendTransfer(int id);
    BigDecimal requestTransfer (Transfer transfer);
    void createTransfer(Transfer transfer);
    void updateTransfer(Transfer transfer);
    void updateBalance(Transfer transfer);
    void rejectRequest(Transfer transfer);
    void acceptRequest(Transfer transfer);

}
