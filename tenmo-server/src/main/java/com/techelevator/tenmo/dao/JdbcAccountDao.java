package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;


@Component // Fixed:required a bean of type 'com.techelevator.tenmo.dao.AccountDao' that could not be found.
public class JdbcAccountDao implements AccountDao {
    private JdbcTemplate jdbcTemplate;


    @Override
    public BigDecimal getBalance(int userId) {
        BigDecimal balance;
        String sql = "SELECT balance " +
                "FROM account " +
                "JOIN tenmo_user ON account.user_id = tenmo_user.user_id " +
                "WHERE tenmo_user.user_id = ?";
        balance = jdbcTemplate.queryForObject(sql, BigDecimal.class, userId);
        return balance;
    }


    private Account mapRowToAccount(SqlRowSet rowSet) {
        Account account = new Account();
        account.setAccountId(rowSet.getInt("account_id"));
        account.setUserId(rowSet.getInt("user_id"));
        account.setBalance(rowSet.getBigDecimal("balance"));
        return account;
    }


    public  JdbcAccountDao(JdbcTemplate jdbcTemplate){
       this.jdbcTemplate = jdbcTemplate;
   }
}
