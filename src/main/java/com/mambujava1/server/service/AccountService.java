package com.mambujava1.server.service;

import com.mambujava1.server.model.Account;
import com.mambujava1.server.model.Card;
import com.mambujava1.server.model.Transaction;

import java.time.LocalDate;
import java.util.List;

public interface AccountService {

    boolean createCurrentAccount(float amount, LocalDate startDate, LocalDate endDate, int userId);

    boolean createDeposit(float amount, LocalDate startDate, LocalDate endDate, int userId);

    boolean createLoan(float amount, LocalDate startDate, LocalDate endDate, int userId);

    void deleteAccount(int id);

    List<Card> viewCards(int id);

    List<Transaction> viewTransactions(int id);

    boolean withdrawCash(int id, float amount);

    boolean depositCash(int id, float amount);

    boolean transferMoney(int fromAccountId, int toAccountId, float amount);

    boolean payWithCard(int cardId, String pin, float amount);

    Account findCurrentAccountById(int id);

    List<Account> findCurrentAccountsByUserId(int userId);

    List<Account> findDepositsByUserId(int userId);

    List<Account> findLoansByUserId(int userId);

    List<Account> findByUserId(int userId);
}
