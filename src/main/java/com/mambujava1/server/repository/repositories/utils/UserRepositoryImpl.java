package com.mambujava1.server.repository.repositories.utils;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mambujava1.server.model.User;
import com.mambujava1.server.ioc.IoCModuleRepository;
import com.mambujava1.server.repository.repositories.AccountRepository;
import com.mambujava1.server.repository.repositories.UserRepository;
import com.mambujava1.server.repository.session.DatabaseConnection;
import org.jdbi.v3.core.Jdbi;

import java.util.List;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {

    private final Jdbi jdbi;
    private List<User> users;
    private final AccountRepository accountRepository;

    public UserRepositoryImpl() {

        this.jdbi = DatabaseConnection.getInstance().getConnection();

        Injector injector = Guice.createInjector(new IoCModuleRepository());
        this.accountRepository = injector.getInstance(AccountRepository.class);

        update();
    }

    @Override
    public void update() {

        users = jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM user")
                .mapToBean(User.class)
                .list());
    }

    @Override
    public User insert(String firstName, String lastName, String address, String username, String emailAddress, String passwordHash) {

        jdbi.withHandle(handle -> handle.createUpdate("INSERT INTO user (first_name, last_name, address, username, email_address, password_hash) VALUES (:first_name, :last_name, :address, :username, :email_address, :password_hash)")
                .bind("first_name", firstName)
                .bind("last_name", lastName)
                .bind("address", address)
                .bind("username", username)
                .bind("email_address", emailAddress)
                .bind("password_hash", passwordHash)
                .execute()
        );

        update();

        return users.stream()
                .reduce((first, second) -> second)
                .orElse(null);
    }

    @Override
    public void delete(int id) {

        accountRepository.findByUserId(id).forEach(account -> accountRepository.delete(account.getId()));

        jdbi.withHandle(handle -> handle.createUpdate("DELETE FROM user WHERE (id = :id)")
                .bind("id", id)
                .execute()
        );

        update();
    }

    @Override
    public List<User> getUsers() {

        return users;
    }

    @Override
    public Optional<User> findById(int id) {

        return jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM user WHERE (id = :id)")
                .bind("id", id)
                .mapToBean(User.class)
                .list()
                .stream()
                .findFirst()
        );
    }

    @Override
    public Optional<User> findByUsername(String username) {

        return jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM user WHERE (username = :username)")
                .bind("username", username)
                .mapToBean(User.class)
                .list()
                .stream()
                .findFirst()
        );
    }

    @Override
    public Optional<User> findByEmailAddress(String emailAddress) {

        return jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM user WHERE (email_address = :email_address)")
                .bind("email_address", emailAddress)
                .mapToBean(User.class)
                .list()
                .stream()
                .findFirst()
        );
    }
}
