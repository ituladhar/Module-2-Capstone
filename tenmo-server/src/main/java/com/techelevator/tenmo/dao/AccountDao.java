package com.techelevator.tenmo.dao;
import com.techelevator.tenmo.model.User;
import java.math.BigDecimal;

public interface AccountDao {
     BigDecimal getBalance(int id);


}
