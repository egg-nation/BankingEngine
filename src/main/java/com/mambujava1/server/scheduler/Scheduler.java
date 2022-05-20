package com.mambujava1.server.scheduler;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mambujava1.server.ioc.IoCModuleRepository;
import com.mambujava1.server.ioc.IoCModuleService;
import com.mambujava1.server.model.Account;
import com.mambujava1.server.model.AccountType;
import com.mambujava1.server.repository.repositories.AccountRepository;
import com.mambujava1.server.repository.repositories.AccountTypeRepository;
import com.mambujava1.server.service.TransactionService;

import java.util.List;
import java.util.Objects;

import static com.mambujava1.server.view.messages.Transactions.*;

public class Scheduler {

    private static Scheduler INSTANCE;

    protected AccountRepository accountRepository;
    protected AccountTypeRepository accountTypeRepository;

    protected TransactionService transactionService;

    private final List<AccountType> accountTypes;

    private Scheduler() {

        Injector injectorForRepository = Guice.createInjector(new IoCModuleRepository());
        this.accountRepository = injectorForRepository.getInstance(AccountRepository.class);
        this.accountTypeRepository = injectorForRepository.getInstance(AccountTypeRepository.class);

        Injector injectorForService = Guice.createInjector(new IoCModuleService());
        this.transactionService = injectorForService.getInstance(TransactionService.class);

        this.accountTypes = accountTypeRepository.getAccountTypes();
    }

    public static Scheduler getInstance() {

        if (Objects.isNull(INSTANCE)) {
            INSTANCE = new Scheduler();
        }

        return INSTANCE;
    }

    public void performDueDateAccountsUpdate() {

        List<Account> monthlyDueAccounts = accountRepository.findAllMonthlyDue();
        performMonthlyDueDateAccountsUpdate(monthlyDueAccounts);

        List<Account> expiredAccounts = accountRepository.findAllExpired();
        performExpiredAccountsUpdate(expiredAccounts);
    }

    private void performMonthlyDueDateAccountsUpdate(List<Account> monthlyDueAccounts) {

        monthlyDueAccounts.forEach(this::payInterest);
    }

    private void performExpiredAccountsUpdate(List<Account> expiredAccounts) {

        expiredAccounts.forEach(account -> transactionService.addTransaction(TRANSACTION_EXPIRED_TITLE, TRANSACTION_EXPIRED_DESC, account.getAmount(), TimeSimulation.currentDate, account.getId()));
    }

    private void payInterest(Account account) {

        float collectedAmount = computeCollectedAmount(account);
        float newAmount = account.getAmount() + collectedAmount;
        accountRepository.updateAmount(account.getId(), newAmount);
        transactionService.addTransaction(TRANSACTION_MONTHLY_DUE_TITLE, TRANSACTION_MONTHLY_DUE_DESC_1 + collectedAmount + TRANSACTION_MONTHLY_DUE_DESC_2 + newAmount, collectedAmount, TimeSimulation.currentDate, account.getId());
    }

    private float computeCollectedAmount(Account account) {

        float interestRate = Objects.requireNonNull(accountTypes.stream()
                .filter(accountType -> accountType.getId() == account.getAccountTypeId())
                .findFirst()
                .orElse(null))
                .getInterestRate();
        return account.getAmount() * interestRate;
    }
}
