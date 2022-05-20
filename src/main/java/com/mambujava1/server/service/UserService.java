package com.mambujava1.server.service;

import com.mambujava1.server.model.Transaction;
import com.mambujava1.server.model.User;

import java.util.List;

public interface UserService {

    User createUser(String firstName, String lastName, String address, String username, String emailAddress, String passwordHash);

    User login(String usernameOrEmailAddress, String passwordHash);

    void deleteUser(int id);

    User findById(int id);

    List<Transaction> viewTransactions(int id);
}
