package com.mambujava1.server.repository.repositories;

import com.mambujava1.server.model.Account;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AccountRepository extends Repository {

    List<Account> getAccounts();

    Account insert(float amount, LocalDate startDate, LocalDate endDate, int userId, int accountTypeId);

    void delete(int id);

    void updateAmount(int id, float amount);

    Optional<Account> findById(int id);

    List<Account> findByUserId(int userId);

    List<Account> findAllMonthlyDue();

    List<Account> findAllExpired();
}
