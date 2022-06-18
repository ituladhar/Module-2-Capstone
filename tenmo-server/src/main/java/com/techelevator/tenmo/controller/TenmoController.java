package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.exceptions.TransferNotFoundException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDTO;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
//@RequestMapping ("/users")
@RequestMapping ()
@PreAuthorize("isAuthenticated()")
public class TenmoController {
    @Autowired
    UserDao userDao;
    @Autowired
    AccountDao accountDao;
    @Autowired
    TransferDao transferDao;

   /* @GetMapping()
    public List<User> listUsers() {
        return userDao.findAll();
    }*/

    @GetMapping(path="balance")
    public BigDecimal getAccountBalance(Principal principal){
        String username = principal.getName();
        long userId = userDao.findIdByUsername(username);
        BigDecimal balance = accountDao.getBalance(userId);
        return balance;
    }

    @GetMapping(path="account/{id}")
    public Account getAccountByUserId(@PathVariable long id){
        return accountDao.getAnAccountByUserId(id);
    }

    @GetMapping(path="user/{id}")
    public long getUserIdByAccountId(@PathVariable long id){
        return userDao.findIdByAccountID(id);
    }

    @GetMapping("users")
    public List<User>getAllUsers(Principal principal){
        String username = principal.getName();
        long userID = userDao.findIdByUsername(username);
        return userDao.findAll(userID);
    }

    @GetMapping(path="transfers")
    public List<Transfer> listTransfers(Principal principal){
        String username = principal.getName();
        long userID = userDao.findIdByUsername(username);
        Account account = accountDao.getAnAccountByUserId(userID);
        long accountId = account.getAccountId();
        List<Transfer> transferList = transferDao.getAllApprovedTransfers(accountId);
        return  transferList;

    }

    @GetMapping(path="transfers/pending")
    public List<Transfer> listPendingTransfers(Principal principal){
        String username = principal.getName();
        long userID = userDao.findIdByUsername(username);
        Account account = accountDao.getAnAccountByUserId(userID);
        long accountId = account.getAccountId();
        List<Transfer> transferList = transferDao.getAllPendingTransfers(accountId);
        return  transferList;
    }

    @GetMapping(path="transfers/{transferId}")
    public Transfer transferDetails (@PathVariable long transferId){
        return transferDao.getTransferById(transferId);
    }


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path="transfers")
    public Transfer startTransfer (Principal principal,
                                   @Valid @RequestBody
                                   TransferDTO transferDTO) {
        String username = principal.getName();
        long userID = userDao.findIdByUsername(username);
        Transfer transfer = transferDao.newTransfer(userID, transferDTO.getUserId(),transferDTO.getAmount());
        return transfer;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path="requests")
    public Transfer requestTransfer(Principal principal,
                                    @Valid @RequestBody
                                    TransferDTO transferDTO){
        String username = principal.getName();
        long userId = userDao.findIdByUsername(username);
        Transfer transfer = transferDao.newRequest(transferDTO.getUserId(), userId, transferDTO.getAmount());
        return transfer;
    }

    @PutMapping(path="transfer/{transferId}/accept")
    public boolean acceptTransfer(Principal principal,
                                  @Valid @RequestBody
                                  TransferDTO transferDTO,
                                  @PathVariable long transferId) {
        String usernameFrom = principal.getName();
        long userFromId = userDao.findIdByUsername(usernameFrom);
        return transferDao.acceptRequest(userFromId, transferDTO.getUserId(), transferDTO.getAmount(), transferId);
    }

    @PutMapping(path="transfer/{transferId}/reject")
    public boolean rejectTransfer(Principal principal,
                                  @Valid @RequestBody
                                  TransferDTO transferDTO,
                                  @PathVariable long transferId) {
        String usernameFrom = principal.getName();
        long userFromId = userDao.findIdByUsername(usernameFrom);
        return transferDao.rejectRequest(transferId);
    }

    @GetMapping(path="username/{accountId}")
    public String username (@PathVariable long accountId){
        return userDao.findUserByAccountID(accountId);
    }

    @RequestMapping(path = "/whoami")
    public String whoAmI(Principal principal) {
        return principal.getName();

    }


}
