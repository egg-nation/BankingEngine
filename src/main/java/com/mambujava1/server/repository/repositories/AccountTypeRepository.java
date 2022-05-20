package com.mambujava1.server.repository.repositories;

import com.mambujava1.server.model.AccountType;

import java.util.List;
import java.util.Optional;

public interface AccountTypeRepository extends Repository {

    AccountType insert(String name, Float interestRate);

    void delete(int id);

    List<AccountType> getAccountTypes();

    Optional<AccountType> findById(int id);

    Optional<AccountType> findByName(String name);
}
