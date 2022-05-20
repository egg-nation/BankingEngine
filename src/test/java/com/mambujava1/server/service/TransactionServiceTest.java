package com.mambujava1.server.service;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mambujava1.server.ioc.IoCModuleService;
import com.mambujava1.server.model.Transaction;
import com.mambujava1.server.repository.repositories.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class TransactionServiceTest {

    protected Injector injector = Guice.createInjector(new IoCModuleService());

    @Mock
    TransactionRepository transactionRepository;

    @InjectMocks
    TransactionService transactionService = injector.getInstance(TransactionService.class);

    @BeforeEach
    public void init() {

        MockitoAnnotations.openMocks(this);
    }

    TransactionServiceTest() {

    }

    @Test
    void whenAddTransaction() {

        //given
        Transaction transaction = new Transaction(1, "transfer", "test transfer", 900, LocalDate.of(2022, 5, 12), 1);
        when(transactionRepository.insert("transfer", "test transfer", 900, LocalDate.of(2022, 5, 12), 1)).thenReturn(transaction);

        //when
        Transaction returnedTransaction = transactionService.addTransaction("transfer", "test transfer", 900, LocalDate.of(2022, 5, 12), 1);

        //then
        assertSame(transaction, returnedTransaction);
    }

    @Test
    void whenDeleteTransaction() {

        //given
        transactionRepository.insert("transfer", "test transfer", 900, LocalDate.of(2022, 5, 12), 1);

        //when
        transactionService.deleteTransaction(1);

        //then
        assertNull(transactionRepository.findById(1).orElse(null));
    }

    @Test
    void whenViewAllTransactions() {

        //given
        Transaction transaction1 = new Transaction(1, "transfer1", "first test transfer", 200, LocalDate.of(2022, 5, 11), 1);
        Transaction transaction2 = new Transaction(1, "transfer2", "second test transfer", 500, LocalDate.of(2022, 5, 11), 1);
        List<Transaction> expected = List.of(transaction1, transaction2);
        when(transactionRepository.getTransactions()).thenReturn(List.of(transaction1, transaction2));

        //when
        List<Transaction> returned = transactionService.viewAllTransactions();

        //then
        assertEquals(expected, returned);
    }

    @Test
    void whenViewTransactionForCorrectId() {

        //given
        Transaction transaction = new Transaction(1, "transfer1", "first test transfer", 200, LocalDate.of(2022, 5, 11), 1);
        when(transactionRepository.findById(1)).thenReturn(Optional.of(transaction));

        //when
        Transaction returned = transactionService.viewTransactionForId(1);

        //then
        assertEquals(transaction, returned);
    }

    @Test
    void whenViewTransactionForWrongId() {

        //given
        Transaction transaction = new Transaction(1, "transfer1", "first test transfer", 200, LocalDate.of(2022, 5, 11), 1);
        when(transactionRepository.findById(1)).thenReturn(Optional.of(transaction));

        //when
        Transaction returned = transactionService.viewTransactionForId(2);

        //then
        assertNull(returned);
    }

    @Test
    void whenViewTransactionsForAccount() {

        //given
        Transaction transaction1 = new Transaction(1, "transfer1", "first test transfer", 200, LocalDate.of(2022, 5, 11), 5);
        Transaction transaction2 = new Transaction(1, "transfer2", "second test transfer", 500, LocalDate.of(2022, 5, 11), 5);
        List<Transaction> expected = List.of(transaction1, transaction2);
        when(transactionRepository.findByAccountId(5)).thenReturn(List.of(transaction1, transaction2));

        //when
        List<Transaction> returned = transactionService.viewTransactionsForAccount(5);

        //then
        assertEquals(expected, returned);
    }
}