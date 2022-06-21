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
@PreAuthorize("isAuthenticated()")
public class TenmoController {
    @Autowired
    UserDao userDao;
    @Autowired
    AccountDao accountDao;
    @Autowired
    TransferDao transferDao;


    @GetMapping("/balance")
    public BigDecimal getAccountBalance(Principal principal){
        String username = principal.getName();
        long userId = userDao.findIdByUsername(username);
        BigDecimal balance = accountDao.getBalance(userId);
        return balance;
    }

    @GetMapping("/account/{id}")
    public Account getAccountByUserId(@PathVariable long id){
        return accountDao.getAnAccountByUserId(id);
    }

    @GetMapping("/user/{id}")
    public long getUserIdByAccountId(@PathVariable long id){
        return userDao.findIdByAccountID(id);
    }

    @GetMapping("/users")
    public List<User>getAllUsers(Principal principal){
        String username = principal.getName();
        long userID = userDao.findIdByUsername(username);
        return userDao.findAll(userID);
    }

    @GetMapping("/transfers")
    public List<Transfer> listTransfers(Principal principal){
        String username = principal.getName();
        long userID = userDao.findIdByUsername(username);
        Account account = accountDao.getAnAccountByUserId(userID);
        long accountId = account.getAccountId();
        List<Transfer> transferList = transferDao.getAllApprovedTransfers(accountId);
        return  transferList;

    }

    @GetMapping("/transfers/pending")
    public List<Transfer> listPendingTransfers(Principal principal){
        String username = principal.getName();
        long userID = userDao.findIdByUsername(username);
        Account account = accountDao.getAnAccountByUserId(userID);
        long accountId = account.getAccountId();
        List<Transfer> transferList = transferDao.getAllPendingTransfers(accountId);
        return  transferList;
    }

    @GetMapping("/transfers/{transferId}")
    public Transfer transferDetails (@PathVariable long transferId){
        return transferDao.getTransferById(transferId);
    }


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/transfers")
    public Transfer startTransfer (Principal principal,
                                   @Valid @RequestBody
                                   TransferDTO transferDTO) {
        String username = principal.getName();
        long userID = userDao.findIdByUsername(username);
        Transfer transfer = transferDao.newTransfer(userID,
                transferDTO.getUserId(),
                transferDTO.getAmount());
        return transfer;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/requests")
    public Transfer requestTransfer(Principal principal,
                                    @Valid @RequestBody
                                    TransferDTO transferDTO){
        String username = principal.getName();
        long userId = userDao.findIdByUsername(username);
        Transfer transfer = transferDao.newRequest(transferDTO.getUserId(),
                userId,
                transferDTO.getAmount());
        return transfer;
    }

    @PutMapping("/transfer/{transferId}/accept")
    public boolean acceptTransfer(Principal principal,
                                  @Valid @RequestBody
                                  TransferDTO transferDTO,
                                  @PathVariable long transferId) {
        String usernameFrom = principal.getName();
        long userFromId = userDao.findIdByUsername(usernameFrom);
        return transferDao.acceptRequest(userFromId,
                transferDTO.getUserId(),
                transferDTO.getAmount(),
                transferId);
    }

    @PutMapping("/transfer/{transferId}/reject")
    public boolean rejectTransfer(Principal principal,
                                  @Valid @RequestBody
                                  TransferDTO transferDTO,
                                  @PathVariable long transferId) {
        String usernameFrom = principal.getName();
        long userFromId = userDao.findIdByUsername(usernameFrom);
        return transferDao.rejectRequest(transferId);
    }

    @GetMapping("/username/{accountId}")
    public String username (@PathVariable long accountId){
        return userDao.findUserByAccountID(accountId);
    }

    @RequestMapping("/whoami")
    public String whoAmI(Principal principal) {
        return principal.getName();
    }

}
