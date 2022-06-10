package com.techelevator.tenmo.dao;

import java.math.BigDecimal;

public interface TransferDao {

    void viewTransfers(int id);
    void viewPendingTransfer(int id);
    BigDecimal sendTransfer(int id);
    BigDecimal requestTransfer (int id);
    void createTransfer();
    void updateBalance();

}
