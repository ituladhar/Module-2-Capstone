package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exceptions.TransferNotFoundException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferStatus;
import com.techelevator.tenmo.model.TransferType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component // constructor in com.techelevator.tenmo.controller.TenmoController required a bean of type 'com.techelevator.tenmo.dao.TransferDao' that could not be found.
public class JdbcTransferDao implements TransferDao {

    public JdbcTemplate jdbcTemplate;
    Boolean getRowSet = false;

    @Autowired
    private AccountDao accountDao;

    public JdbcTransferDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.accountDao = new JdbcAccountDao(dataSource);
    }

    @Override
    public List<Transfer> getAllApprovedTransfers(long accountId) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer.transfer_id, transfer.account_from, transfer.account_to, transfer.amount, transferType.transfer_type_desc, transferStatus.transfer_status_desc FROM transfer transfer " +
                "JOIN transfer_status ts ON transfer.transfer_status_id = transferStatus.transfer_status_id " +
                "JOIN transfer_type transferType ON transfer.transfer_type_id = transferType.transfer_type_id " +
                "WHERE (account_from = ? OR account_to = ?) AND transfer.transfer_status_id = 2";
        SqlRowSet results = this.jdbcTemplate.queryForRowSet(sql, accountId, accountId);
        while (results.next()) {
            transfers.add(mapRowToTransfer(results));
        }
        return transfers;
    }

    @Override
    public List<Transfer> getAllPendingTransfers(long accountId) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer.transfer_id, transfer.account_from, transfer.account_to, transfer.amount, transferType.transfer_type_desc, transferStatus.transfer_status_desc FROM transfer transfer " +
                "JOIN transfer_status ts ON transfer.transfer_status_id = transferStatus.transfer_status_id " +
                "JOIN transfer_type transferType ON transfer.transfer_type_id = transferType.transfer_type_id " +
                "WHERE account_from = ? AND t.transfer_status_id = 1";
        SqlRowSet results = this.jdbcTemplate.queryForRowSet(sql, accountId);
        while (results.next()) {
            transfers.add(mapRowToTransfer(results));
        }
        return transfers;
    }

    @Override
    public Transfer getTransferById(long transferId) {
        Transfer transfer = new Transfer();
        String sql = "SELECT transfer.transfer_id, transfer.account_from, transfer.account_to, transfer.amount, transferType.transfer_type_desc, transferStatus.transfer_status_desc FROM transfer transfer " +
                "JOIN transfer_status ts ON transfer.transfer_status_id = transferStatus.transfer_status_id " +
                "JOIN transfer_type transferType ON transfer.transfer_type_id = transferType.transfer_type_id " +
                "WHERE t.transfer_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        if (results.next()) {
            transfer = mapRowToTransfer(results);
        }
        return transfer;
    }

    @Override
    public Transfer newTransfer(long userFrom, long userTo, BigDecimal amount) {
        String sql = "INSERT INTO transfer " +
                "(account_from, account_to, amount, transfer_status_id, transfer_type_id) " +
                "Values (?, ?, ?, ?, ?) RETURNING transfer_id";
        long newTransferId = 0;
        Account accountFrom = accountDao.getAnAccountByUserId(userFrom);
        Account accountTo = accountDao.getAnAccountByUserId(userTo);
        if (userFrom != userTo) {
            try {
                if (accountDao.subtractBalance(amount, userFrom)) {
                    accountDao.addBalance(amount, userTo);
                    newTransferId = jdbcTemplate.queryForObject(sql, Long.class, accountFrom.getAccountId(), accountTo.getAccountId(), amount, 2, 2);
                } else {
                    newTransferId = jdbcTemplate.queryForObject(sql, Long.class, accountFrom.getAccountId(), accountTo.getAccountId(), amount, 3, 2);
                }
            } catch (DataAccessException e) {
                System.out.println("Something went wrong while making transfer");
            }

        } else {
            newTransferId = jdbcTemplate.queryForObject(sql, Long.class, accountFrom.getAccountId(), accountTo.getAccountId(), amount, 3, 2);

        }
        return getTransferById(newTransferId);
    }

    @Override
    public Transfer newRequest(long userFrom, long userTo, BigDecimal amount) {
        String sql = "INSERT INTO transfer " +
                "(account_from, account_to, amount, transfer_status_id, transfer_type_id) " + "" +
                "Values (?, ?, ?, ?, ?) RETURNING transfer_id";
        long newTransferId = 0;
        Account accountFrom = accountDao.getAnAccountByUserId(userFrom);
        Account accountTo = accountDao.getAnAccountByUserId(userTo);
        if (userFrom != userTo) {
            try {
                newTransferId = jdbcTemplate.queryForObject(sql, Long.class, accountFrom.getAccountId(), accountTo.getAccountId(), amount, 1, 1);
            } catch (DataAccessException e) {
                System.out.println("Something went wrong while making transfer");
            }
        } else {
            newTransferId = jdbcTemplate.queryForObject(sql, Long.class, accountFrom.getAccountId(), accountTo.getAccountId(), amount, 3, 1);
        }
        return getTransferById(newTransferId);
    }

    @Override
    public boolean acceptRequest(long userFrom, long userTo, BigDecimal amount, long transferId) {
        String sql = "UPDATE transfer SET transfer_status_id = ?  WHERE transfer_id = ?";
        try {
            if (accountDao.subtractBalance(amount, userFrom)) {
                accountDao.addBalance(amount, userTo);
                jdbcTemplate.update(sql, 2, transferId);
                return true;
            } else {
                jdbcTemplate.update(sql, 3, transferId);
            }
        } catch (DataAccessException e) {
            System.out.println("omething went wrong while making transfer");
        }
        return false;
    }

    @Override
    public boolean rejectRequest(long transferId) {
        String sql = "UPDATE transfer SET transfer_status_id = ?  WHERE transfer_id = ?";
        try {
            jdbcTemplate.update(sql, 3, transferId);
            return true;
        } catch (DataAccessException e) {
            System.out.println("Error while rejecting transfer");
        }
        return false;
    }


    private Transfer mapRowToTransfer(SqlRowSet rowSet) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rowSet.getLong("transfer_id"));
        transfer.setAccountFrom(rowSet.getLong("account_from"));
        transfer.setAccountTo(rowSet.getLong("account_to"));
        transfer.setAmount(rowSet.getBigDecimal("amount"));
        transfer.setTransferType(rowSet.getString("transfer_type_desc"));
        transfer.setTransferStatus(rowSet.getString("transfer_status_desc"));
        return transfer;

    }
}
