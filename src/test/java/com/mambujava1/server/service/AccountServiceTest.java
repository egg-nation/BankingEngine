package com.mambujava1.server.service;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mambujava1.server.ioc.IoCModuleService;
import com.mambujava1.server.model.Account;
import com.mambujava1.server.model.AccountType;
import com.mambujava1.server.model.Card;
import com.mambujava1.server.model.Transaction;
import com.mambujava1.server.repository.repositories.AccountRepository;
import com.mambujava1.server.repository.repositories.AccountTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.mambujava1.server.view.constants.TransactionTypes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AccountServiceTest {

    protected Injector injector = Guice.createInjector(new IoCModuleService());

    @Mock
    AccountRepository accountRepository;
    @Mock
    AccountTypeRepository accountTypeRepository;
    @Mock
    TransactionService transactionService;
    @Mock
    CardService cardService;

    @InjectMocks
    AccountService accountService = injector.getInstance(AccountService.class);

    @BeforeEach
    public void init() {

        MockitoAnnotations.openMocks(this);
    }

    AccountServiceTest() {

    }

    @Test
    void whenCreateCurrentAccount() {

        // given
        AccountType accountType = new AccountType(1, CURRENT_ACCOUNT, null);
        Account account = new Account(1, 12, LocalDate.of(2022, 5, 11), null, 1, 1);
        when(accountTypeRepository.findByName(CURRENT_ACCOUNT)).thenReturn(Optional.of(accountType));
        when(accountRepository.insert(12, LocalDate.of(2022, 5, 11), null, 1, 1)).thenReturn(account);

        // when
        boolean result = accountService.createCurrentAccount(12, LocalDate.of(2022, 5, 11), null, 1);

        // then
        assertTrue(result);
    }

    @Test
    void whenCreateDeposit() {

        //given
        AccountType accountType = new AccountType(2, DEPOSIT, 3.5f);
        Account account = new Account(2, 20, LocalDate.of(2022, 5, 11), LocalDate.of(2023, 5, 11), 1, 2);
        when(accountTypeRepository.findByName(DEPOSIT)).thenReturn(Optional.of(accountType));
        when(accountRepository.insert(20, LocalDate.of(2022, 5, 11), LocalDate.of(2023, 5, 11), 1, 2)).thenReturn(account);

        //when
        boolean result = accountService.createDeposit(20, LocalDate.of(2022, 5, 11), LocalDate.of(2023, 5, 11), 1);

        //then
        assertTrue(result);
    }

    @Test
    void whenCreateLoan() {

        //given
        AccountType accountType = new AccountType(3, LOAN, 5.5f);
        Account account = new Account(3, 200, LocalDate.of(2022, 5, 11), LocalDate.of(2026, 5, 11), 1, 3);
        when(accountTypeRepository.findByName(LOAN)).thenReturn(Optional.of(accountType));
        when(accountRepository.insert(200, LocalDate.of(2022, 5, 11), LocalDate.of(2026, 5, 11), 1, 3)).thenReturn(account);

        //when
        boolean result = accountService.createLoan(200, LocalDate.of(2022, 5, 11), LocalDate.of(2026, 5, 11), 1);

        //then
        assertTrue(result);
    }

    @Test
    void whenDeleteAccount() {

        //given
        accountRepository.insert(200, LocalDate.of(2022, 5, 11), null, 1, 1);

        //when
        accountService.deleteAccount(1);

        //then
        assertNull(accountRepository.findById(1).orElse(null));
    }

    @Test
    void whenViewCards() {

        //given
        Card card1 = new Card(1, "1234", LocalDate.of(2022, 5, 11), LocalDate.of(2026, 5, 31), 1, 1);
        Card card2 = new Card(2, "4253", LocalDate.of(2022, 5, 11), LocalDate.of(2024, 5, 31), 1, 1);
        List<Card> expected = List.of(card1, card2);
        when(cardService.findCardsByAccountId(1)).thenReturn(List.of(card1, card2));

        //when
        List<Card> cards = accountService.viewCards(1);

        //then
        assertEquals(expected, cards);
    }

    @Test
    void whenViewTransactions() {

        //given
        Transaction transaction1 = new Transaction(1, "transfer1", "first test transfer", 200, LocalDate.of(2022, 5, 11), 1);
        Transaction transaction2 = new Transaction(1, "transfer2", "second test transfer", 500, LocalDate.of(2022, 5, 11), 1);
        List<Transaction> expected = List.of(transaction1, transaction2);
        when(transactionService.viewTransactionsForAccount(1)).thenReturn(List.of(transaction1, transaction2));

        //when
        List<Transaction> transactions = accountService.viewTransactions(1);

        //then
        assertEquals(expected, transactions);
    }

    @Test
    void whenWithdrawCashWithEnoughBalance() {

        //given
        Account account = new Account(1, 500.34f, LocalDate.of(2022, 5, 11), null, 1, 1);
        when(accountRepository.findById(1)).thenReturn(Optional.of(account));


        //when
        boolean result = accountService.withdrawCash(1, 300);

        //then
        assertTrue(result);
    }

    @Test
    void whenWithdrawCashWithExactBalance() {

        //given
        Account account = new Account(1, 345.87f, LocalDate.of(2022, 5, 11), null, 1, 1);
        when(accountRepository.findById(1)).thenReturn(Optional.of(account));


        //when
        boolean result = accountService.withdrawCash(1, 345.87f);

        //then
        assertTrue(result);
    }

    @Test
    void whenWithdrawCashWithNotEnoughBalance() {

        //given
        Account account = new Account(1, 233.5f, LocalDate.of(2022, 5, 11), null, 1, 1);
        when(accountRepository.findById(1)).thenReturn(Optional.of(account));


        //when
        boolean result = accountService.withdrawCash(1, 345.87f);

        //then
        assertFalse(result);
    }


    @Test
    void whenDepositCash() {

        //given
        Account account = new Account(1, 445f, LocalDate.of(2022, 5, 11), null, 1, 1);
        when(accountRepository.findById(1)).thenReturn(Optional.of(account));


        //when
        boolean result = accountService.depositCash(1, 50f);

        //then
        assertTrue(result);
    }

    @Test
    void whenTransferMoneyToValidAccount() {

        //given
        Account account1 = new Account(1, 300, LocalDate.of(2022, 5, 11), null, 1, 1);
        Account account2 = new Account(2, 700, LocalDate.of(2021, 4, 21), null, 1, 1);
        when(accountRepository.findById(1)).thenReturn(Optional.of(account1));
        when(accountRepository.findById(2)).thenReturn(Optional.of(account2));

        //when
        boolean result = accountService.transferMoney(1, 2, 70);

        //then
        assertTrue(result);
    }

    @Test
    void whenTransferMoneyToInvalidAccount() {

        //given
        Account account1 = new Account(1, 300, LocalDate.of(2022, 5, 11), null, 1, 1);
        when(accountRepository.findById(1)).thenReturn(Optional.of(account1));
        when(accountRepository.findById(2)).thenReturn(Optional.empty());

        //when
        boolean result = accountService.transferMoney(1, 2, 70);

        //then
        assertFalse(result);
    }

    @Test
    void whenPayWithCardWithCorrectPin() {

        //given
        Card card = new Card(1, "1234", LocalDate.of(2020, 3, 30), LocalDate.of(2024, 3, 31), 1, 1);
        Account account = new Account(1, 300, LocalDate.of(2019, 5, 11), null, 1, 1);
        when(cardService.findCardByIdAndPin(1, "1234")).thenReturn(card);
        when(accountRepository.findById(1)).thenReturn(Optional.of(account));

        //when
        boolean result = accountService.payWithCard(1, "1234", 30);

        //then
        assertTrue(result);
    }

    @Test
    void whenPayWithCardWithCorrectPinAndNotEnoughBalance() {

        //given
        Card card = new Card(1, "1234", LocalDate.of(2020, 3, 30), LocalDate.of(2024, 3, 31), 1, 1);
        Account account = new Account(1, 300, LocalDate.of(2019, 5, 11), null, 1, 1);
        when(cardService.findCardByIdAndPin(1, "1234")).thenReturn(card);
        when(accountRepository.findById(1)).thenReturn(Optional.of(account));

        //when
        boolean result = accountService.payWithCard(1, "1234", 301);

        //then
        assertFalse(result);
    }

    @Test
    void whenPayWithCardWithoutPin() {

        //given
        Card card = new Card(1, "1234", LocalDate.of(2020, 3, 30), LocalDate.of(2024, 3, 31), 1, 1);
        Account account = new Account(1, 300, LocalDate.of(2019, 5, 11), null, 1, 1);
        when(cardService.findById(1)).thenReturn(card);
        when(accountRepository.findById(1)).thenReturn(Optional.of(account));

        //when
        boolean result = accountService.payWithCard(1, null, 30);

        //then
        assertTrue(result);
    }

    @Test
    void whenPayWithCardWithWrongPin() {

        //given
        Card card = new Card(1, "1234", LocalDate.of(2020, 3, 30), LocalDate.of(2024, 3, 31), 1, 1);
        Account account = new Account(1, 300, LocalDate.of(2019, 5, 11), null, 1, 1);
        when(cardService.findCardByIdAndPin(1, "1234")).thenReturn(card);
        when(accountRepository.findById(1)).thenReturn(Optional.of(account));

        //when
        boolean result = accountService.payWithCard(1, "4632", 30);

        //then
        assertFalse(result);
    }

    @Test
    void whenFindCurrentAccountByCorrectId() {

        //given
        AccountType accountType = new AccountType(1,  CURRENT_ACCOUNT, null);
        Account account = new Account(1, 300, LocalDate.of(2019, 5, 12), null, 1, 1);
        when(accountTypeRepository.findByName(CURRENT_ACCOUNT)).thenReturn(Optional.of(accountType));
        when(accountRepository.findById(1)).thenReturn(Optional.of(account));

        //when
        Account returned = accountService.findCurrentAccountById(1);

        //then
        assertEquals(account, returned);
    }

    @Test
    void whenFindCurrentAccountByWrongId() {

        //given
        AccountType accountType = new AccountType(1,  CURRENT_ACCOUNT, null);
        Account account = new Account(1, 300, LocalDate.of(2019, 5, 12), null, 1, 1);
        when(accountTypeRepository.findByName(CURRENT_ACCOUNT)).thenReturn(Optional.of(accountType));
        when(accountRepository.findById(1)).thenReturn(Optional.of(account));

        //when
        Account returned = accountService.findCurrentAccountById(4);

        //then
        assertNull(returned);
    }

    @Test
    void whenFindCurrentAccountsByCorrectUserId() {

        //given
        AccountType accountType = new AccountType(1,  CURRENT_ACCOUNT, null);
        Account account1 = new Account(1, 300, LocalDate.of(2019, 5, 12), null, 2, 1);
        Account account2 = new Account(2, 700, LocalDate.of(2020, 7, 2), null, 2, 1);
        List<Account> expected = List.of(account1, account2);
        when(accountTypeRepository.findByName(CURRENT_ACCOUNT)).thenReturn(Optional.of(accountType));
        when(accountRepository.findByUserId(2)).thenReturn(List.of(account1, account2));

        //when
        List<Account> returned = accountService.findCurrentAccountsByUserId(2);

        //then
        assertEquals(expected, returned);
    }

    @Test
    void whenFindCurrentAccountsByWrongUserId() {

        //given
        AccountType accountType = new AccountType(1,  CURRENT_ACCOUNT, null);
        Account account1 = new Account(1, 300, LocalDate.of(2019, 5, 12), null, 2, 1);
        Account account2 = new Account(2, 700, LocalDate.of(2020, 7, 2), null, 2, 1);
        when(accountTypeRepository.findByName(CURRENT_ACCOUNT)).thenReturn(Optional.of(accountType));
        when(accountRepository.findByUserId(2)).thenReturn(List.of(account1, account2));

        //when
        boolean returned = accountService.findCurrentAccountsByUserId(1).isEmpty();

        //then
        assertTrue(returned);
    }

    @Test
    void whenFindDepositsByCorrectUserId() {

        //given
        AccountType accountType = new AccountType(2,  DEPOSIT, 3.5f);
        Account account = new Account(1, 300, LocalDate.of(2019, 5, 12), LocalDate.of(2026, 5, 30), 2, 2);
        List<Account> expected = List.of(account);
        when(accountTypeRepository.findByName(DEPOSIT)).thenReturn(Optional.of(accountType));
        when(accountRepository.findByUserId(2)).thenReturn(List.of(account));

        //when
        List<Account> returned = accountService.findDepositsByUserId(2);

        //then
        assertEquals(expected, returned);
    }

    @Test
    void whenFindDepositsByWrongUserId() {

        //given
        AccountType accountType = new AccountType(2,  DEPOSIT, 3.5f);
        Account account = new Account(1, 300, LocalDate.of(2019, 5, 12), LocalDate.of(2026, 5, 30), 2, 2);
        when(accountTypeRepository.findByName(DEPOSIT)).thenReturn(Optional.of(accountType));
        when(accountRepository.findByUserId(2)).thenReturn(List.of(account));

        //when
        boolean returned = accountService.findDepositsByUserId(1).isEmpty();

        //then
        assertTrue(returned);
    }

    @Test
    void whenFindLoansByCorrectUserId() {

        //given
        AccountType accountType = new AccountType(3,  LOAN, 5.5f);
        Account account = new Account(1, 50000, LocalDate.of(2022, 5, 12), LocalDate.of(2030, 7, 30), 2, 3);
        List<Account> expected = List.of(account);
        when(accountTypeRepository.findByName(LOAN)).thenReturn(Optional.of(accountType));
        when(accountRepository.findByUserId(2)).thenReturn(List.of(account));

        //when
        List<Account> returned = accountService.findLoansByUserId(2);

        //then
        assertEquals(expected, returned);
    }

    @Test
    void whenFindLoansByWrongUserId() {

        //given
        AccountType accountType = new AccountType(3,  LOAN, 5.5f);
        Account account = new Account(1, 50000, LocalDate.of(2022, 5, 12), LocalDate.of(2030, 7, 30), 2, 3);
        when(accountTypeRepository.findByName(LOAN)).thenReturn(Optional.of(accountType));
        when(accountRepository.findByUserId(2)).thenReturn(List.of(account));

        //when
        boolean returned = accountService.findLoansByUserId(1).isEmpty();

        //then
        assertTrue(returned);
    }

    @Test
    void whenFindByCorrectUserId() {

        //given
        Account account1 = new Account(1, 50000, LocalDate.of(2022, 5, 12), LocalDate.of(2030, 7, 30), 1, 3);
        Account account2 = new Account(2, 700, LocalDate.of(2020, 7, 2), null, 1, 1);
        Account account3 = new Account(2, 700, LocalDate.of(2021, 4, 21), null, 1, 1);
        List<Account> expected = List.of(account1, account2, account3);
        when(accountRepository.findByUserId(1)).thenReturn(List.of(account1, account2, account3));

        //when
        List<Account> returned = accountService.findByUserId(1);

        //then
        assertEquals(expected, returned);
    }

    @Test
    void whenFindByWrongUserId() {

        //given
        Account account1 = new Account(1, 50000, LocalDate.of(2022, 5, 12), LocalDate.of(2030, 7, 30), 1, 3);
        Account account2 = new Account(2, 700, LocalDate.of(2020, 7, 2), null, 1, 1);
        Account account3 = new Account(2, 700, LocalDate.of(2021, 4, 21), null, 1, 1);
        when(accountRepository.findByUserId(1)).thenReturn(List.of(account1, account2, account3));

        //when
        boolean returned = accountService.findByUserId(5).isEmpty();

        //then
        assertTrue(returned);
    }
}