package com.mambujava1.server.repository.repositories.utils;

import com.mambujava1.server.model.Transaction;
import com.mambujava1.server.repository.repositories.TransactionRepository;
import com.mambujava1.server.repository.session.DatabaseConnection;
import org.jdbi.v3.core.Jdbi;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class TransactionRepositoryImpl implements TransactionRepository {

    private final Jdbi jdbi;
    private List<Transaction> transactions;

    public TransactionRepositoryImpl() {

        this.jdbi = DatabaseConnection.getInstance().getConnection();

        update();
    }

    @Override
    public void update() {

        transactions = jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM transaction")
                .mapToBean(Transaction.class)
                .list());
    }

    @Override
    public Transaction insert(String name, String description, float amount, LocalDate date, int accountId) {

        Date sqlDate = Date.valueOf(date);

        jdbi.withHandle(handle -> handle.createUpdate("INSERT INTO transaction (name, description, amount, date, account_id) VALUES (:name, :description, :amount, :date, :account_id)")
                .bind("name", name)
                .bind("description", description)
                .bind("amount", amount)
                .bind("date", sqlDate)
                .bind("account_id", accountId)
                .execute()
        );

        update();

        return transactions.stream()
                .reduce((first, second) -> second)
                .orElse(null);
    }

    @Override
    public void delete(int id) {

        jdbi.withHandle(handle -> handle.createUpdate("DELETE FROM transaction WHERE (id = :id)")
                .bind("id", id)
                .execute()
        );

        update();
    }

    @Override
    public List<Transaction> getTransactions() {

        return transactions;
    }

    @Override
    public Optional<Transaction> findById(int id) {

        return jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM transaction WHERE (id = :id)")
                .bind("id", id)
                .mapToBean(Transaction.class)
                .list()
                .stream()
                .findFirst()
        );
    }

    @Override
    public List<Transaction> findByAccountId(int accountId) {

        return jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM transaction WHERE (account_id = :account_id)")
                .bind("account_id", accountId)
                .mapToBean(Transaction.class)
                .list()
        );
    }
}
