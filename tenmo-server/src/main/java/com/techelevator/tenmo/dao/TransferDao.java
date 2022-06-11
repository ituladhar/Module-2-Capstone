package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exceptions.TransferNotFoundException;
import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {
    void transferMoney(Transfer transfer);
    List<Transfer> viewTransfers(int id) throws TransferNotFoundException;
    List<Transfer> viewPendingTransfer(int id) throws TransferNotFoundException;
  //  BigDecimal sendTransfer(int id);
  //  BigDecimal requestTransfer (Transfer transfer);
    Transfer createTransfer(Transfer transfer);
    void updateTransfer(Transfer transfer);
    void updateBalance(Transfer transfer);
    void rejectRequest(Transfer transfer);
    void acceptRequest(Transfer transfer);

}
