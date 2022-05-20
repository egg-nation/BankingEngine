package com.mambujava1.server.repository.repositories.utils;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mambujava1.server.ioc.IoCModuleRepository;
import com.mambujava1.server.model.Account;
import com.mambujava1.server.model.AccountType;
import com.mambujava1.server.repository.repositories.AccountRepository;
import com.mambujava1.server.repository.repositories.AccountTypeRepository;
import com.mambujava1.server.repository.session.DatabaseConnection;
import com.mambujava1.server.scheduler.TimeSimulation;
import org.jdbi.v3.core.Jdbi;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.mambujava1.server.view.constants.TransactionTypes.DEPOSIT;
import static com.mambujava1.server.view.constants.TransactionTypes.LOAN;

public class AccountRepositoryImpl implements AccountRepository {

    private final Jdbi jdbi;
    private List<Account> accounts;
    private final AccountTypeRepository accountTypeRepository;

    public AccountRepositoryImpl() {

        this.jdbi = DatabaseConnection.getInstance().getConnection();

        Injector injectorForRepository = Guice.createInjector(new IoCModuleRepository());
        this.accountTypeRepository = injectorForRepository.getInstance(AccountTypeRepository.class);
        update();
    }

    @Override
    public void update() {

        accounts = jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM account")
                .mapToBean(Account.class)
                .list());
    }

    @Override
    public List<Account> getAccounts() {

        return accounts;
    }

    @Override
    public Account insert(float amount, LocalDate startDate, LocalDate endDate, int userId, int accountTypeId) {

        Date sqlStartDate = Date.valueOf(startDate);
        Date sqlEndDate;
        if (endDate != null) {
            sqlEndDate = Date.valueOf(endDate);
        } else {
            sqlEndDate = null;
        }

        jdbi.withHandle(handle -> handle.createUpdate("INSERT INTO account (amount, start_date, end_date, user_id, account_type_id) VALUES (:amount, :start_date, :end_date, :user_id, :account_type_id)")
                .bind("amount", amount)
                .bind("start_date", sqlStartDate)
                .bind("end_date", sqlEndDate)
                .bind("user_id", userId)
                .bind("account_type_id", accountTypeId)
                .execute()
        );

        update();

        return accounts.stream()
                .reduce((first, second) -> second)
                .orElse(null);
    }

    @Override
    public void delete(int id) {

        jdbi.withHandle(handle -> handle.createUpdate("DELETE FROM transaction WHERE (account_id = :account_id)")
                .bind("account_id", id)
                .execute()
        );
        jdbi.withHandle(handle -> handle.createUpdate("DELETE FROM card WHERE (account_id = :account_id)")
                .bind("account_id", id)
                .execute()
        );
        jdbi.withHandle(handle -> handle.createUpdate("DELETE FROM account WHERE (id = :id)")
                .bind("id", id)
                .execute()
        );

        update();
    }

    @Override
    public void updateAmount(int id, float amount) {

        jdbi.withHandle(handle -> handle.createUpdate("UPDATE account SET amount = :amount WHERE (id = :id)")
                .bind("amount", amount)
                .bind("id", id)
                .execute()
        );

        update();
    }

    @Override
    public Optional<Account> findById(int id) {

        return jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM account WHERE (id = :id)")
                .bind("id", id)
                .mapToBean(Account.class)
                .list()
                .stream()
                .findFirst()
        );
    }

    @Override
    public List<Account> findByUserId(int userId) {

        return jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM account WHERE (user_id = :user_id)")
                .bind("user_id", userId)
                .mapToBean(Account.class)
                .list()
        );
    }

    @Override
    public List<Account> findAllMonthlyDue() {

        AccountType depositType = accountTypeRepository.findByName(DEPOSIT).orElse(null);
        AccountType loanType = accountTypeRepository.findByName(LOAN).orElse(null);
        if (Objects.nonNull(depositType) && Objects.nonNull(loanType)) {
            return accounts.stream()
                    .filter(account -> account.getAccountTypeId() == depositType.getId() || account.getAccountTypeId() == loanType.getId())
                    .filter(this::checkMonthlyDue)
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Account> findAllExpired() {

        Predicate<Account> expiredAccountsPredicate = account -> TimeSimulation.currentDate.equals(account.getEndDate());

        AccountType depositType = accountTypeRepository.findByName(DEPOSIT).orElse(null);
        AccountType loanType = accountTypeRepository.findByName(LOAN).orElse(null);
        if (Objects.nonNull(depositType) && Objects.nonNull(loanType)) {
            return accounts.stream()
                    .filter(account -> account.getAccountTypeId() == depositType.getId() || account.getAccountTypeId() == loanType.getId())
                    .filter(expiredAccountsPredicate)
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    private boolean checkMonthlyDue(Account account) {

        LocalDate startDate = account.getStartDate();
        if (startDate.isAfter(TimeSimulation.currentDate)) {
            return false;
        }

        int accountStartYear = startDate.getYear();
        int accountStartMonth = startDate.getMonthValue();
        int accountStartDay = startDate.getDayOfMonth();

        int currentYear = TimeSimulation.currentDate.getYear();
        int currentMonth = TimeSimulation.currentDate.getMonthValue();
        int currentDay = TimeSimulation.currentDate.getDayOfMonth();

        int nextDayOfCurrentDateDay = TimeSimulation.currentDate.plusDays(1).getDayOfMonth();

        return ((currentMonth != accountStartMonth) || (currentYear > accountStartYear))
                && ((currentDay == accountStartDay) || ((currentDay < accountStartDay) && (nextDayOfCurrentDateDay == 1)));
    }
}
