package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.exceptions.TransferNotFoundException;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping (path = "users")
@PreAuthorize("isAuthenticated()")
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


    @GetMapping (path = "/{id}/balance")
    public BigDecimal getBalance(@PathVariable int id) {

        return accountDao.getBalance(id);
    }

    @PutMapping ( path = "/transfers")
    public void transfer(@RequestBody Transfer transfer) {
        transferDao.transferMoney(transfer);
    }

    @PostMapping ( path = "/transfers/requests")
    public Transfer requestMoney(@RequestBody Transfer transfer) {
        return transferDao.createTransfer(transfer);
    }


    //Fixed: Exception encountered during context initialization - cancelling refresh attempt: org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'requestMappingHandlerMapping' defined in class path resource [org/springframework/boot/autoconfigure/web/servlet/WebMvcAutoConfiguration$EnableWebMvcConfiguration.class]: Invocation of init method failed; nested exception is java.lang.IllegalStateException: Ambiguous mapping. Cannot map 'tenmoController' method
    //com.techelevator.tenmo.controller.TenmoController#viewPendin      gTransferRequests
    @GetMapping (path = "/transfers/{id}")
    public List<Transfer> viewTransfers(@PathVariable int userId) throws TransferNotFoundException {
        return transferDao.viewTransfers(userId);
    }



    @GetMapping (path = "/transfers/{id}/pending")
    public List<Transfer> viewPendingTransferRequests(@PathVariable int id) throws TransferNotFoundException {
        return transferDao.viewPendingTransfer(id);
    }

    @PutMapping (path = "/transfers/requests")
    public void approveOrRejectTransfer(@RequestBody Transfer transfer) {
        if (transfer.getTransferStatusId() == 2) {
            transferDao.updateBalance(transfer);
        }
        transferDao.updateTransfer(transfer);
    }


    @RequestMapping(path = "/whoami")
    public String whoAmI(Principal principal) {
        return principal.getName();

    }


}
