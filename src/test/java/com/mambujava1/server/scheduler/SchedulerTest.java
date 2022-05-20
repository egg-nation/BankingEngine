package com.mambujava1.server.scheduler;

import com.mambujava1.server.model.Account;
import com.mambujava1.server.model.AccountType;
import com.mambujava1.server.model.Transaction;
import com.mambujava1.server.repository.repositories.AccountRepository;
import com.mambujava1.server.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SchedulerTest {
    @Mock
    AccountRepository accountRepository;
    @Mock
    TransactionService transactionService;

    @InjectMocks
    Scheduler scheduler = Scheduler.getInstance();

    @Captor
    ArgumentCaptor<Integer> accountIdCaptor;
    @Captor
    ArgumentCaptor<Float> amountCaptor;

    @BeforeEach
    public void init() {

        MockitoAnnotations.openMocks(this);
    }

    SchedulerTest() {

    }

    @Test
    void performDueDateAccountsUpdate() {

        // given
        AccountType accountType = new AccountType(1, "deposit", 1.2f);
        List<Account> monthlyDueAccounts = List.of(
                new Account(1, 12, LocalDate.now().minusMonths(2), LocalDate.now().plusMonths(1), 1, accountType.getId())
        );
        List<Account> expiredAccounts = List.of(
                new Account(2, 12, LocalDate.now().minusMonths(1), LocalDate.now(), 1, accountType.getId())
        );
        when(accountRepository.findAllMonthlyDue()).thenReturn(monthlyDueAccounts);
        when(accountRepository.findAllExpired()).thenReturn(expiredAccounts);
        when(transactionService.addTransaction(any(), any(), anyFloat(), isNull(), anyInt())).thenReturn(new Transaction(1, "test", "test desc", 0.0f, null, 2));

        // when
        scheduler.performDueDateAccountsUpdate();
        Transaction transactionForExpiration = transactionService.addTransaction("test for expired account", "there was a transaction added", 0.0f, null, 2);

        // then
        verify(accountRepository).updateAmount(accountIdCaptor.capture(), amountCaptor.capture());
        assertEquals(1, accountIdCaptor.getValue());
        assertEquals(14.88f, amountCaptor.getValue());
        assertNotNull(transactionForExpiration);
    }
}