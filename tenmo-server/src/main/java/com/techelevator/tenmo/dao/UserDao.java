package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.User;

import java.util.List;

public interface UserDao {

   List<User> findAll(long id);

    User findByUsername(String username);

    long findIdByUsername(String username);

    boolean create(String username, String password);

    public long findIdByAccountID(long accountId);

    String findUserByAccountID(long accountId);



}
