package com.techelevator;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import com.techelevator.DAOIntegrationTest;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;

public class AccountSqlDAOTest extends DAOIntegrationTest {

    private JdbcTransferDao jdbcTransferDao;
    private JdbcTemplate jdbcTemplate;
    private User defaultDummy;
    private Account account;
    private static final String DEFAULT_USER_NAME = "tester";
    private static final String DEFAULT_PASSWORD = "12234567";
    private static final BigDecimal DUMMY_BALANCE = new BigDecimal("9999.99");

    @BeforeClass
    public static void setupBeforeClass() throws Exception {
        setupDataSource();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        closeDataSource();
    }

    @Before
    public void setup() throws Exception {
        jdbcTemplate = new JdbcTemplate(getDataSource());
        jdbcTransferDao = new JdbcTransferDao(jdbcTemplate);

        // Add dummy user to users and accounts tables
        defaultDummy = new User();
        defaultDummy.setUsername(DEFAULT_USER_NAME);
        addDummyToUserTable(defaultDummy);
        account = new Account();
        addDummyAccount(defaultDummy);
    }

    private void addDummyToUserTable(User dummy) {
        dummy.setPassword(DEFAULT_PASSWORD);
        SqlRowSet result = jdbcTemplate.queryForRowSet("INSERT INTO users (username, password_hash) VALUES (?, ?) RETURNING user_id",
                dummy.getUsername(), dummy.getPassword());
        if (result.next())
            dummy.setId(result.getLong("user_id"));
    }

    private void addDummyAccount(User dummy) {
        account.setUserId(dummy.getId());
        account.setAccountBalance(DUMMY_BALANCE);
        SqlRowSet result = jdbcTemplate.queryForRowSet("INSERT INTO accounts (user_id, balance) VALUES (?, ?) RETURNING account_id",
                account.getUserId(), account.getAccountBalance());
        if(result.next()) {
            account.setAccountId(result.getLong("account_id"));
        }
    }

    @After
    public void tearDown() throws Exception {
        rollback();
    }

    @Test
    public void get_correct_current_balance() {
        BigDecimal actual = accountDao.viewCurrentBalance(defaultDummy.getId());
        assertEquals(DUMMY_BALANCE, actual);
    }

    @Test
    public void get_correct_balance_after_adding_money() {
        accountDao.creditBalance(account, BigDecimal.TEN);
        BigDecimal actual = account.getAccountBalance();
        assertEquals(DUMMY_BALANCE.add(BigDecimal.TEN), actual);
    }

    @Test
    public void cannot_deduct_more_than_existing_balance() {
        BigDecimal actual = accountDao.deductBalance(account, DUMMY_BALANCE.add(BigDecimal.TEN));
        assertEquals(DUMMY_BALANCE, actual);
    }

    @Test
    public void get_correct_balance_after_deducting_all_money() {
        BigDecimal actual = accountDao.deductBalance(account, DUMMY_BALANCE);
        assertEquals(0, BigDecimal.ZERO.compareTo(actual));
    }

    @Test
    public void get_correct_balance_after_deducting_some_money() {
        BigDecimal actual = accountDao.deductBalance(account, DUMMY_BALANCE.subtract(BigDecimal.TEN));
        assertEquals(0, BigDecimal.TEN.compareTo(actual));
    }

    @Test
    public void find_correct_account_by_user_id() {
        Account actual = jdbcTransferDao.findAccountByUserId(defaultDummy.getId());
        assertEquals(account, actual);
    }

}