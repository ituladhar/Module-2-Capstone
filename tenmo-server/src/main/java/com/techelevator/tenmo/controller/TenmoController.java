package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.exceptions.TransferNotFoundException;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping (path = "users")
public class TenmoController {
    private UserDao userDao;
    private AccountDao accountDao;
    private TransferDao transferDao;



    public TenmoController(UserDao userdao, AccountDao accountDao, TransferDao transferDao) {
        this.userDao = userdao;
        this.accountDao = accountDao;
        this.transferDao = transferDao;
    }



    @GetMapping( path = "")
    public List<User> listUsers() {

        return userDao.findAll();
    }


    @GetMapping (path = "/{id}")
    public BigDecimal getBalance(int id) {

        return accountDao.getBalance(id);
    }



    @PutMapping ( path = "/transfers")
    public void transfer(Transfer transfer) {

        transferDao.transferMoney(transfer);
    }



    @PutMapping ( path = "/transfers")
    public Transfer requestMoney(Transfer transfer) {
        return transferDao.createTransfer(transfer);
    }



    @GetMapping (path = "")
    public List<Transfer> viewTransfers(int id) throws TransferNotFoundException {
        return transferDao.viewTransfers(id);
    }



    @GetMapping (path = "")
    public List<Transfer> viewPendingTransferRequests(int id) throws TransferNotFoundException {
        return transferDao.viewPendingTransfer(id);
    }



    public void approveOrRejectTransfer(Transfer transfer) {
        if (transfer.getTransferStatusId() == 2) {
            transferDao.updateBalance(transfer);
        }
        transferDao.updateTransfer(transfer);
    }

}
