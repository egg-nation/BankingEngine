package com.mambujava1.server.service.utils;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mambujava1.server.ioc.IoCModuleRepository;
import com.mambujava1.server.ioc.IoCModuleService;
import com.mambujava1.server.model.Account;
import com.mambujava1.server.model.AccountType;
import com.mambujava1.server.model.Card;
import com.mambujava1.server.model.Transaction;
import com.mambujava1.server.repository.repositories.AccountRepository;
import com.mambujava1.server.repository.repositories.AccountTypeRepository;
import com.mambujava1.server.service.AccountService;
import com.mambujava1.server.service.CardService;
import com.mambujava1.server.service.TransactionService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.mambujava1.server.view.constants.TransactionTypes.*;
import static com.mambujava1.server.view.messages.Transactions.*;

public class AccountServiceImpl implements AccountService {

    protected AccountRepository accountRepository;
    protected AccountTypeRepository accountTypeRepository;
    protected TransactionService transactionService;
    protected CardService cardService;

    public AccountServiceImpl() {

        Injector injectorForRepository = Guice.createInjector(new IoCModuleRepository());
        this.accountRepository = injectorForRepository.getInstance(AccountRepository.class);
        this.accountTypeRepository = injectorForRepository.getInstance(AccountTypeRepository.class);

        Injector injectorForService = Guice.createInjector(new IoCModuleService());
        this.transactionService = injectorForService.getInstance(TransactionService.class);
        this.cardService = injectorForService.getInstance(CardService.class);
    }

    @Override
    public boolean createCurrentAccount(float amount, LocalDate startDate, LocalDate endDate, int userId) {

        AccountType accountType = accountTypeRepository.findByName(CURRENT_ACCOUNT).orElse(null);

        if (Objects.nonNull(accountType)) {
            return Objects.nonNull(createAccount(amount, startDate, endDate, userId, accountType.getId()));
        }

        return false;
    }

    @Override
    public boolean createDeposit(float amount, LocalDate startDate, LocalDate endDate, int userId) {

        AccountType accountType = accountTypeRepository.findByName(DEPOSIT).orElse(null);

        if (Objects.nonNull(accountType)) {
            return Objects.nonNull(createAccount(amount, startDate, endDate, userId, accountType.getId()));
        }

        return false;
    }

    @Override
    public boolean createLoan(float amount, LocalDate startDate, LocalDate endDate, int userId) {

        AccountType accountType = accountTypeRepository.findByName(LOAN).orElse(null);

        if (Objects.nonNull(accountType)) {
            return Objects.nonNull(createAccount(amount, startDate, endDate, userId, accountType.getId()));
        }

        return false;
    }

    @Override
    public void deleteAccount(int id) {

        accountRepository.delete(id);
    }

    @Override
    public List<Card> viewCards(int id) {

        return cardService.findCardsByAccountId(id);
    }

    @Override
    public List<Transaction> viewTransactions(int id) {

        return transactionService.viewTransactionsForAccount(id);
    }

    @Override
    public boolean withdrawCash(int id, float amount) {

        Optional<Account> foundAccount = accountRepository.findById(id);

        if (foundAccount.isPresent()) {
            float newAmount = foundAccount.get().getAmount() - amount;
            if (newAmount >= 0) {
                accountRepository.updateAmount(id, newAmount);
                transactionService.addTransaction(TRANSACTION_WITHDRAW_CASH_TITLE, TRANSACTION_WITHDRAW_CASH_DESC, amount, LocalDate.now(), id);

                return true;
            }
        }

        return false;
    }

    @Override
    public boolean depositCash(int id, float amount) {

        Optional<Account> foundAccount = accountRepository.findById(id);

        if (foundAccount.isPresent()) {
            float newAmount = foundAccount.get().getAmount() + amount;
            accountRepository.updateAmount(id, newAmount);
            transactionService.addTransaction(TRANSACTION_DEPOSIT_CASH_TITLE, TRANSACTION_DEPOSIT_CASH_DESC, amount, LocalDate.now(), id);

            return true;
        }

        return false;
    }

    @Override
    public boolean transferMoney(int fromAccountId, int toAccountId, float amount) {

        Account fromAccount = accountRepository.findById(fromAccountId).orElse(null);
        Account toAccount = accountRepository.findById(toAccountId).orElse(null);

        if (Objects.nonNull(fromAccount) && Objects.nonNull(toAccount)) {
            float fromAccountAmountToBeUpdated = fromAccount.getAmount() - amount;

            if (fromAccountAmountToBeUpdated >= 0) {
                accountRepository.updateAmount(fromAccountId, fromAccountAmountToBeUpdated);
                transactionService.addTransaction(TRANSACTION_TRANSFER_SENT_TITLE, TRANSACTION_TRANSFER_SENT_DESC + toAccountId, amount, LocalDate.now(), fromAccountId);

                accountRepository.updateAmount(toAccountId, toAccount.getAmount() + amount);
                transactionService.addTransaction(TRANSACTION_TRANSFER_RECEIVED_TITLE, TRANSACTION_TRANSFER_RECEIVED_DESC + fromAccountId, amount, LocalDate.now(), toAccountId);

                return true;
            }
        }

        return false;
    }

    @Override
    public boolean payWithCard(int cardId, String pin, float amount) {

        Card foundCard;
        if (Objects.isNull(pin)) {
            foundCard = cardService.findById(cardId);
        } else {
            foundCard = cardService.findCardByIdAndPin(cardId, pin);
        }

        if (Objects.nonNull(foundCard)) {
            Account foundAccount = accountRepository.findById(foundCard.getAccountId()).orElse(null);
            if (foundCard.getActive() == 1 && Objects.nonNull(foundAccount)) {
                float newAmount = foundAccount.getAmount() - amount;
                if (newAmount >= 0) {
                    accountRepository.updateAmount(foundAccount.getId(), newAmount);
                    transactionService.addTransaction(TRANSACTION_PAY_WITH_CARD_TITLE, TRANSACTION_PAY_WITH_CARD_DESC + foundCard.getId(), amount, LocalDate.now(), foundAccount.getId());

                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public Account findCurrentAccountById (int id) {

        AccountType accountType = accountTypeRepository.findByName(CURRENT_ACCOUNT).orElse(null);
        Account currentAccount = findById(id);

        if (Objects.nonNull(accountType) && Objects.nonNull(currentAccount)) {
            return currentAccount.getAccountTypeId() == accountType.getId() ? currentAccount : null;
        } else {
            return null;
        }
    }

    @Override
    public List<Account> findCurrentAccountsByUserId(int userId) {

        return findByUserIdAndType(userId, CURRENT_ACCOUNT);
    }

    @Override
    public List<Account> findDepositsByUserId(int userId) {

        return findByUserIdAndType(userId, DEPOSIT);
    }

    @Override
    public List<Account> findLoansByUserId(int userId) {

        return findByUserIdAndType(userId, LOAN);
    }

    @Override
    public List<Account> findByUserId(int userId) {

        return accountRepository.findByUserId(userId);
    }

    private Account createAccount(float amount, LocalDate startDate, LocalDate endDate, int userId, int accountTypeId) {

        return accountRepository.insert(amount, startDate, endDate, userId, accountTypeId);
    }

    private Account findById(int id) {

        return accountRepository.findById(id).orElse(null);
    }

    private List<Account> findByUserIdAndType(int userId, String accountTypeName) {

        AccountType accountType = accountTypeRepository.findByName(accountTypeName).orElse(null);

        if (Objects.nonNull(accountType)) {
            return findByUserId(userId).stream()
                    .filter(account -> account.getAccountTypeId() == accountType.getId())
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }
}
