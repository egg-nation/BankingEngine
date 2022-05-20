package com.mambujava1.server.service.utils;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mambujava1.server.ioc.IoCModuleRepository;
import com.mambujava1.server.model.Transaction;
import com.mambujava1.server.repository.repositories.TransactionRepository;
import com.mambujava1.server.service.TransactionService;

import java.time.LocalDate;
import java.util.List;

public class TransactionServiceImpl implements TransactionService {

    protected TransactionRepository transactionRepository;

    public TransactionServiceImpl() {

        Injector injector = Guice.createInjector(new IoCModuleRepository());
        this.transactionRepository = injector.getInstance(TransactionRepository.class);
    }

    @Override
    public Transaction addTransaction(String name, String description, float amount, LocalDate date, int accountId) {

        return transactionRepository.insert(name, description, amount, date, accountId);
    }

    @Override
    public void deleteTransaction(int id) {

        transactionRepository.delete(id);
    }

    @Override
    public List<Transaction> viewAllTransactions() {

        return transactionRepository.getTransactions();
    }

    @Override
    public Transaction viewTransactionForId(int id) {

        return transactionRepository.findById(id).orElse(null);
    }

    @Override
    public List<Transaction> viewTransactionsForAccount(int accountId) {

        return transactionRepository.findByAccountId(accountId);
    }
}
