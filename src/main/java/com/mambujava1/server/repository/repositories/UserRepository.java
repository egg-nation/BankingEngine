package com.mambujava1.server.repository.repositories;

import com.mambujava1.server.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends Repository {

    User insert(String firstName, String lastName, String address, String username, String emailAddress, String passwordHash);

    void delete(int id);

    List<User> getUsers();

    Optional<User> findById(int id);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmailAddress(String emailAddress);
}
