package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.math.BigDecimal;

public class JdbcTransferDao implements TransferDao{
    public JdbcTemplate jdbcTemplate;

    @Override
    public void transferMoney(Transfer transfer) {

    }

    @Override
    public void viewTransfers(int id) {


    }

    @Override
    public void viewPendingTransfer(int id) {

    }

    @Override
    public BigDecimal sendTransfer(int id) {
        return null;
    }

    @Override
    public BigDecimal requestTransfer(Transfer transfer) {
        return null;
    }

    @Override
    public BigDecimal requestTransfer(int id) {
        return null;
    }


    @Override
    public void createTransfer(Transfer transfer) {

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

    private int findAccountByUserId(long id) {
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
    @Override
    public void rejectRequest(Transfer transfer){
        String sql = "UPDATE transfer  " +
                "SET transfer_status_id = ?  " +
                "WHERE transfer_id = ?;";
        jdbcTemplate.update(sql, TransferStatus.REJECTED,
                transfer.getAccountToId());

    }

    private Transfer mapTransferToRowSet(SqlRowSet rowSet) {
        Transfer transfer = new Transfer();

        transfer.setTransferId(rowSet.getInt("transfer_id"));
        transfer.setTransferType(rowSet.getString("transfer_type_desc"));
        transfer.setTransferStatus(rowSet.getString("transfer_status_desc"));
        transfer.setAccountFromId(rowSet.getInt("account_from"));
        transfer.setAccountToId(rowSet.getInt("account_to"));
        transfer.setAmount(rowSet.getBigDecimal("amount"));

        return transfer;
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
