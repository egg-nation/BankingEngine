package com.mambujava1.server.service.utils;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mambujava1.server.ioc.IoCModuleRepository;
import com.mambujava1.server.ioc.IoCModuleService;
import com.mambujava1.server.model.Account;
import com.mambujava1.server.model.Transaction;
import com.mambujava1.server.model.User;
import com.mambujava1.server.repository.repositories.UserRepository;
import com.mambujava1.server.service.AccountService;
import com.mambujava1.server.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserServiceImpl implements UserService {

    protected UserRepository userRepository;
    protected AccountService accountService;

    public UserServiceImpl() {

        Injector injectorForRepository = Guice.createInjector(new IoCModuleRepository());
        this.userRepository = injectorForRepository.getInstance(UserRepository.class);

        Injector injectorForService = Guice.createInjector(new IoCModuleService());
        this.accountService = injectorForService.getInstance(AccountService.class);
    }

    @Override
    public User createUser(String firstName, String lastName, String address, String username, String emailAddress, String passwordHash) {

        if (userRepository.findByUsername(username).isEmpty() && userRepository.findByEmailAddress(emailAddress).isEmpty()) {
            return userRepository.insert(firstName, lastName, address, username, emailAddress, passwordHash);
        } else {
            return null;
        }
    }

    @Override
    public User login(String usernameOrEmailAddress, String passwordHash) {

        User foundUser = userRepository.findByUsername(usernameOrEmailAddress).orElse(null);

        if (Objects.isNull(foundUser)) {
            foundUser = userRepository.findByEmailAddress(usernameOrEmailAddress).orElse(null);
            if (Objects.isNull(foundUser)) {
                return null;
            }
        }

        return (foundUser.getPasswordHash().trim().equals(passwordHash.trim()) ? foundUser : null);
    }

    @Override
    public void deleteUser(int id) {

        userRepository.delete(id);
    }

    @Override
    public User findById(int id) {

        return userRepository.findById(id).orElse(null);
    }

    @Override
    public List<Transaction> viewTransactions(int id) {

        List<Account> depositsAndLoans = accountService.findDepositsByUserId(id);
        depositsAndLoans.addAll(accountService.findLoansByUserId(id));

        List<Transaction> transactionsForDepositsAndLoans = new ArrayList<>();

        depositsAndLoans.forEach(account -> transactionsForDepositsAndLoans.addAll(accountService.viewTransactions(account.getId())));

        return transactionsForDepositsAndLoans;
    }
}
