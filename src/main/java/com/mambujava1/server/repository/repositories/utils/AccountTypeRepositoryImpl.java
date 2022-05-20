package com.mambujava1.server.repository.repositories.utils;

import com.mambujava1.server.model.AccountType;
import com.mambujava1.server.repository.repositories.AccountTypeRepository;
import com.mambujava1.server.repository.session.DatabaseConnection;
import org.jdbi.v3.core.Jdbi;

import java.util.List;
import java.util.Optional;

public class AccountTypeRepositoryImpl implements AccountTypeRepository {

    private final Jdbi jdbi;
    private List<AccountType> accountTypes;

    public AccountTypeRepositoryImpl() {

        this.jdbi = DatabaseConnection.getInstance().getConnection();

        update();
    }

    @Override
    public void update() {

        accountTypes = jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM account_type")
                .mapToBean(AccountType.class)
                .list()
        );
    }

    @Override
    public AccountType insert(String name, Float interestRate) {

        jdbi.withHandle(handle -> handle.createUpdate("INSERT INTO account_type (name, interest_rate) VALUES (:name, :interest_rate)")
                .bind("name", name)
                .bind("interest_rate", interestRate)
                .execute()
        );

        update();

        return accountTypes.stream()
                .reduce((first, second) -> second)
                .orElse(null);
    }

    @Override
    public void delete(int id) {

        jdbi.withHandle(handle -> handle.createUpdate("DELETE FROM account_type WHERE (id = :id)")
                .bind("id", id)
                .execute()
        );

        update();
    }

    @Override
    public List<AccountType> getAccountTypes() {

        return accountTypes;
    }

    @Override
    public Optional<AccountType> findById(int id) {

        return jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM account_type WHERE (id = :id)")
                .bind("id", id)
                .mapToBean(AccountType.class)
                .list()
                .stream()
                .findFirst()
        );
    }

    @Override
    public Optional<AccountType> findByName(String name) {

        return jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM account_type WHERE (name = :name)")
                .bind("name", name)
                .mapToBean(AccountType.class)
                .list()
                .stream()
                .findFirst()
        );
    }
}
