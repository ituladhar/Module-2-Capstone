package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exceptions.TransferNotFoundException;
import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {
    void transferMoney(Transfer transfer);
    List<Transfer> viewTransfers(int userId) throws TransferNotFoundException;
    List<Transfer> viewPendingTransfer(int userId) throws TransferNotFoundException;
  //  BigDecimal sendTransfer(int id);
  //  BigDecimal requestTransfer (Transfer transfer);
    Transfer createTransfer(Transfer transfer);
    void updateTransfer(Transfer transfer);

    void updateBalanceSend(Transfer transfer);

    void updateBalanceRequest(Transfer transfer);
    void rejectRequest(Transfer transfer);
    void acceptRequest(Transfer transfer);

    // Additional methods to check transfers
    int findAccountByUserId(int id);
}
