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

@Component
public class JdbcTransferDao implements TransferDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AccountDao accountDao;

    public JdbcTransferDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.accountDao = new JdbcAccountDao(dataSource);
    }

    // List of approved transfers
    @Override
    public List<Transfer> getAllApprovedTransfers(long accountId) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT t.transfer_id, t.account_from, t.account_to, t.amount, tt.transfer_type_desc, ts.transfer_status_desc FROM transfer t " +
                "JOIN transfer_status ts ON t.transfer_status_id = ts.transfer_status_id " +
                "JOIN transfer_type tt ON t.transfer_type_id = tt.transfer_type_id " +
                "WHERE (account_from = ? OR account_to = ?) AND t.transfer_status_id = 2" ;
        SqlRowSet results = this.jdbcTemplate.queryForRowSet(sql, accountId, accountId);
        while (results.next()) {
            transfers.add(mapRowToTransfer(results));
        }
        return transfers;
    }


// List of all pending transfers
    @Override
    public List<Transfer> getAllPendingTransfers(long accountId) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT t.transfer_id, t.account_from, t.account_to, t.amount, tt.transfer_type_desc, ts.transfer_status_desc FROM transfer t " +
                "JOIN transfer_status ts ON t.transfer_status_id = ts.transfer_status_id " +
                "JOIN transfer_type tt ON t.transfer_type_id = tt.transfer_type_id " +
                "WHERE account_from = ? AND t.transfer_status_id = 1 ";
        SqlRowSet results = this.jdbcTemplate.queryForRowSet(sql, accountId);
        while (results.next()) {
            transfers.add(mapRowToTransfer(results));
        }
        return transfers;
    }



    // Method to get the transfer by Id
    @Override
    public Transfer getTransferById(long transferId) {
        Transfer transfer = new Transfer();
        String sql = "SELECT t.transfer_id, t.account_from, t.account_to, t.amount, tt.transfer_type_desc, ts.transfer_status_desc FROM transfer t " +
                "JOIN transfer_status ts ON t.transfer_status_id = ts.transfer_status_id " +
                "JOIN transfer_type tt ON t.transfer_type_id = tt.transfer_type_id " +
                "WHERE t.transfer_id = ? ";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        if (results.next()) {
            transfer = mapRowToTransfer(results);
        }
        return transfer;
    }



// Create a new transfer
    @Override
    public Transfer newTransfer(long userFrom, long userTo, BigDecimal amount) {
        String sql = "INSERT INTO transfer (account_from, account_to, amount, transfer_status_id, transfer_type_id) " +
                "Values (?, ?, ?, ?, ?) RETURNING transfer_id ";
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
                System.out.println("Error while making transfer");
            }
        } else {
            newTransferId = jdbcTemplate.queryForObject(sql, Long.class, accountFrom.getAccountId(), accountTo.getAccountId(), amount, 3, 2);
        }
        return getTransferById(newTransferId);
    }




    // Make a new request
    @Override
    public Transfer newRequest(long userFrom, long userTo, BigDecimal amount) {
        String sql = "INSERT INTO transfer (account_from, account_to, amount, transfer_status_id, transfer_type_id) "+
                "Values (?, ?, ?, ?, ?) RETURNING transfer_id ";
        long newTransferId = 0;
        Account accountFrom = accountDao.getAnAccountByUserId(userFrom);
        Account accountTo = accountDao.getAnAccountByUserId(userTo);

        if (userFrom != userTo) {
            try {
                newTransferId = jdbcTemplate.queryForObject(sql, Long.class, accountFrom.getAccountId(), accountTo.getAccountId(), amount, 1, 1);
            } catch (DataAccessException e) {
                System.out.println("Error while requesting transfer");
            }
        } else {
            newTransferId = jdbcTemplate.queryForObject(sql, Long.class, accountFrom.getAccountId(), accountTo.getAccountId(), amount, 3, 1);
        }
        return getTransferById(newTransferId);
    }



// Method to accept or request a transfer
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
            System.out.println("Error while accepting transfer");
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
        transfer.setTransferTypeDesc(rowSet.getString("transfer_type_desc"));
        transfer.setTransferStatusDesc(rowSet.getString("transfer_status_desc"));

        return transfer;

    }
}