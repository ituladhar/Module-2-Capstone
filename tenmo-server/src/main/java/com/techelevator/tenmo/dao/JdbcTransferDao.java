package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exceptions.TransferNotFoundException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferStatus;
import com.techelevator.tenmo.model.TransferType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component // constructor in com.techelevator.tenmo.controller.TenmoController required a bean of type 'com.techelevator.tenmo.dao.TransferDao' that could not be found.
public class JdbcTransferDao implements TransferDao{
    public JdbcTemplate jdbcTemplate;
    Boolean getRowSet = false;

    @Override
    public void transferMoney(Transfer transfer) {
        checkUserExists(transfer.getAccountFromId());
        checkUserExists(transfer.getAccountToId());

        String sql = "UPDATE account SET balance = (balance - ?) " +
                "WHERE user_id = ?;";
        jdbcTemplate.update(sql, transfer.getAmount(), transfer.getAccountFromId());                   // TODO use created update balance instead

        sql = "UPDATE account SET balance = (balance + ?) " +
                "WHERE user_id = ?;";
        jdbcTemplate.update(sql, transfer.getAmount(), transfer.getAccountToId());

        transfer.setTransferTypeId(TransferType.SEND);
        transfer.setTransferStatusId(TransferStatus.APPROVED);

        createTransfer(transfer);
    }

    @Override
    public List<Transfer> viewTransfers(int userId) throws TransferNotFoundException {
        List<Transfer> transfers = new ArrayList<>();

        String sql = "SELECT transfer_id, transfer_type_desc, transfer_status_desc, account_from, account_to, amount " +
                "FROM transfer " +
                "JOIN transfer_type ON transfer.transfer_type_id = transfer_type.transfer_type_id " +
                "JOIN transfer_status ON transfer.transfer_status_id = transfer_status.transfer_status_id " +
                "JOIN account ON transfer.account_from = account.account_id OR transfer.account_to = account.account_id " +
                "WHERE user_id = ?;";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId);
        while (rowSet.next()) {
            getRowSet = true;
            Transfer transfer = mapTransferToRowSet(rowSet);
            transfer.setAccountFromUsername(getUserRowSet(transfer.getAccountFromId()).getString("username"));
            transfer.setAccountToUsername(getUserRowSet(transfer.getAccountToId()).getString("username"));

            transfers.add(transfer);
        }
        if (getRowSet) {
            return transfers;
        }
        throw new TransferNotFoundException("Error. No such transfer exists or you do not have permission to view it.");
    }



    @Override
    public List<Transfer> viewPendingTransfer(int userId) throws TransferNotFoundException {
        List<Transfer> pendingTransfers = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_desc, transfer_status_desc, account_from, account_to, amount " +
                    "FROM transfer " +
                    "JOIN transfer_type ON transfer.transfer_type_id = transfer_type.transfer_type_id " +
                    "JOIN transfer_status ON transfer.transfer_status_id = transfer_status.transfer_status_id " +
                    "JOIN account ON transfer.account_from = account.account_id " +
                    "WHERE account.user_id = ? AND transfer.transfer_status_id = ?; ";
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId, TransferStatus.PENDING);
            while (rowSet.next()) {
                getRowSet = true;
                Transfer transfer = mapTransferToRowSet(rowSet);
                transfer.setAccountFromUsername(getUserRowSet(transfer.getAccountFromId()).getString("username"));
                transfer.setAccountToUsername(getUserRowSet(transfer.getAccountToId()).getString("username"));
                pendingTransfers.add(transfer);
            }

        if (getRowSet)
                return pendingTransfers;
            throw new TransferNotFoundException("Error. No such transfer exists or you do not have permission to view it.");
        }




//    @Override
//    public Transfer sendMoneyByTransfer(BigDecimal amountToSend, int id) {
//
//        return null;
//    }
//
//    @Override
//    public Transfer requestMoneyByTransfer(Transfer transfer) {
//        return null;
//    }




    @Override
    public Transfer createTransfer(Transfer transfer) {
        transfer.setAccountFromId(findAccountByUserId(transfer.getAccountFromId()));
        transfer.setAccountToId(findAccountByUserId(transfer.getAccountToId()));
        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (?, ?, ?, ?, ?) returning transfer_id;";

        transfer.setTransferId(jdbcTemplate.queryForObject(sql, Integer.class, transfer.getTransferTypeId(),
                transfer.getTransferStatusId(), transfer.getAccountFromId(), transfer.getAccountToId(), transfer.getAmount()));
        return transfer;
    }




    @Override
    public void updateTransfer(Transfer transfer) {
        String sql = "UPDATE transfer " +
                "SET transfer_status_id = ? " +
                "WHERE transfer_id = ?;";
        jdbcTemplate.update(sql, transfer.getTransferStatusId(), transfer.getTransferId());

    }


    @Override
    public void updateBalance(Transfer transfer) {
        String sql = "Update account " +
                "SET balance = balance - ? " +
                "WHERE account_id = ?;";
        jdbcTemplate.update(sql, transfer.getAmount(), transfer.getAccountFromId());

        sql = "Update account " +
                "SET balance = balance + ?" +
                "WHERE account_id = ?;";
        jdbcTemplate.update(sql, transfer.getAmount(), transfer.getAccountToId());
    }



    @Override
    public void rejectRequest(Transfer transfer){
        String sql = "UPDATE transfer  " +
                "SET transfer_status_id = ?  " +
                "WHERE transfer_id = ?;";
        jdbcTemplate.update(sql, TransferStatus.REJECTED,
                transfer.getAccountToId());

    }


    @Override
    public void acceptRequest(Transfer transfer){
        setTransferStatusToApproved(transfer);
        subtractAmountFromSender(transfer);
        addAmountToRequester(transfer);
    }


    public Transfer mapTransferToRowSet(SqlRowSet rowSet) {
        Transfer transfer = new Transfer();

        transfer.setTransferId(rowSet.getInt("transfer_id"));
        transfer.setTransferType(rowSet.getString("transfer_type_desc"));
        transfer.setTransferStatus(rowSet.getString("transfer_status_desc"));
        transfer.setAccountFromId(rowSet.getInt("account_from"));
        transfer.setAccountToId(rowSet.getInt("account_to"));
        transfer.setAmount(rowSet.getBigDecimal("amount"));

        return transfer;
    }

    private SqlRowSet getUserRowSet(int userId) {
        //Gets usernames for Transfer object using account_id
        String sql = "SELECT username FROM tenmo_user " +
                "JOIN account ON tenmo_user.user_id = account.user_id " +
                "WHERE account_id = ?;";

       // Used to prevent java.sql.SQLException (Invalid cursor position)
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId);
        rowSet.next();
        return rowSet;
    }

// Additional methods to check transfers
    @Override
    public int findAccountByUserId(int id) {
        Account account = new Account();
        String sql = "SELECT account_id " +
                "FROM account " +
                "WHERE user_id = ?;";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id);
        if (rowSet.next()) {
            account.setAccountId(rowSet.getInt("account_id"));
        }
        return account.getAccountId();
    }

    private void checkUserExists(long userId) {
        String sql = "SELECT user_id, username FROM tenmo_user WHERE user_id = ?;";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId);

        if (!rowSet.next()) {
            throw new UsernameNotFoundException("UserId " + userId + " was not found");
        }
    }


    private void addAmountToRequester(Transfer transfer){
        String sql = "UPDATE account  " +
                "SET balance = (balance + ?)" +
                "WHERE account_id = ?;";
        jdbcTemplate.update(sql, transfer.getAmount(),
                transfer.getAccountToId());

    }

    private void subtractAmountFromSender(Transfer transfer){
        String sql = "UPDATE account  " +
                "SET balance = (balance - ?)" +
                "WHERE account_id = ?;";
        jdbcTemplate.update(sql, transfer.getAmount(),
                transfer.getAccountFromId());

    }

    private void setTransferStatusToApproved(Transfer transfer){
        String sql = "UPDATE transfer  " +
                "SET transfer_status_id = ?  " +
                "WHERE transfer_id = ?;";
        jdbcTemplate.update(sql, TransferStatus.APPROVED,
                transfer.getAccountToId());

    }

}
