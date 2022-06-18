package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import javax.sql.DataSource;


@Component // Fixed:required a bean of type 'com.techelevator.tenmo.dao.AccountDao' that could not be found.
public class JdbcAccountDao implements AccountDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public BigDecimal getBalance(long userId) {
        String sql = "SELECT balance FROM account WHERE user_id = ?;";
        return jdbcTemplate.queryForObject(sql, BigDecimal.class, userId);
    }

    @Override
    public Account getAnAccountByUserId(long userId) {
        String sql = "SELECT * FROM account WHERE user_id = ?";
        SqlRowSet results = this.jdbcTemplate.queryForRowSet(sql, userId);
        Account account = null;
        if (results.next()) {
            account = accountObjectMapper(results);
        }
        return account;
    }

    @Override
    public void addBalance(BigDecimal amount, long userId) {
        String sql = "UPDATE account SET balance = balance + ? "+
                "WHERE user_id = ?";
        jdbcTemplate.update(sql, amount, userId);


    }

    @Override
    public boolean subtractBalance(BigDecimal amount, long userId) {
        Account account = getAnAccountByUserId(userId);
        int result= account.getBalance().compareTo(amount);

        if (result == 1 || result == 0) {
            String sql = "UPDATE account "+
                    "SET balance = balance - ? "+
                    "WHERE user_id = ?;";
            jdbcTemplate.update(sql, amount, userId);
            return true;
        } else {
            return false;
        }
    }

    // SqlRowSet Object Mapper :)
    private Account accountObjectMapper(SqlRowSet results) {
        Account account = new Account();
        account.setAccountId(results.getLong("account_id"));
        account.setBalance(results.getBigDecimal("balance"));
        return account;
    }

}
