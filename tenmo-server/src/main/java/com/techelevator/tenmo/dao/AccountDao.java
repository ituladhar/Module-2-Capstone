package com.techelevator.tenmo.dao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import java.math.BigDecimal;

public interface AccountDao {
     BigDecimal getBalance(long id);
     Account getAnAccountByUserId(long userId);
     void addBalance(BigDecimal amount, long accountId);
     boolean subtractBalance(BigDecimal amount, long accountId);
}
