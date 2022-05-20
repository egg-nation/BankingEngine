package com.mambujava1.server.service;

import com.mambujava1.server.model.Transaction;

import java.time.LocalDate;
import java.util.List;

public interface TransactionService {

    Transaction addTransaction(String name, String description, float amount, LocalDate date, int accountId);

    void deleteTransaction(int id);

    List<Transaction> viewAllTransactions();

    Transaction viewTransactionForId(int id);

    List<Transaction> viewTransactionsForAccount(int accountId);
}
