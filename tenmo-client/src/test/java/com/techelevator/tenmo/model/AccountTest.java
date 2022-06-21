package com.techelevator.tenmo.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
// Server
class AccountTest {

    Account test = new Account();


    @Test
    public void Test_AccountId() {
        long accountId = 3003;
        test.setAccountId(accountId);
        assertEquals(test.getAccountId(), accountId);
    }

    @Test
    public void Test_UserId() {
        long userId = 1002;
        test.setUserId(userId);
        assertEquals(test.getUserId(), userId);
    }

 @Test
    public void Test_AccountBalance() {
        BigDecimal accountBalance = BigDecimal.valueOf(999.00);
        test.setBalance(accountBalance);
        assertEquals(test.getBalance(), accountBalance);
    }


}
