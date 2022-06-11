package com.techelevator.tenmo.dao;
import com.techelevator.tenmo.model.User;
import java.math.BigDecimal;

public interface AccountDao {
    public BigDecimal getBalance(int id);
}
