package com.mambujava1.server.repository.repositories;

import com.mambujava1.server.model.Transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends Repository {

    Transaction insert(String name, String description, float amount, LocalDate date, int accountId);

    void delete(int id);

    List<Transaction> getTransactions();

    Optional<Transaction> findById(int id);

    List<Transaction> findByAccountId(int accountId);
}
