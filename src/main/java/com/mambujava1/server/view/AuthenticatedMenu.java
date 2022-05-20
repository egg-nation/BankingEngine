package com.mambujava1.server.view;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mambujava1.server.ioc.IoCModuleService;
import com.mambujava1.server.model.Account;
import com.mambujava1.server.model.User;
import com.mambujava1.server.scheduler.TimeSimulation;
import com.mambujava1.server.service.AccountService;
import com.mambujava1.server.service.UserService;
import com.mambujava1.server.view.input.GetDirectInput;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

import static com.mambujava1.server.view.messages.Commands.AUTHENTICATED_MENU_COMMANDS;
import static com.mambujava1.server.view.messages.Errors.*;
import static com.mambujava1.server.view.messages.InputFields.*;
import static com.mambujava1.server.view.messages.Success.*;

public class AuthenticatedMenu implements Menu {

    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;
    private final GetDirectInput getDirectInput;

    private final User user;
    private final AccountService accountService;
    private final UserService userService;

    public AuthenticatedMenu(DataInputStream dataInputStream, DataOutputStream dataOutputStream, User user) {

        this.dataInputStream = dataInputStream;
        this.dataOutputStream = dataOutputStream;
        this.getDirectInput = new GetDirectInput(dataInputStream, dataOutputStream);

        this.user = user;

        Injector injector = Guice.createInjector(new IoCModuleService());
        this.accountService = injector.getInstance(AccountService.class);
        this.userService = injector.getInstance(UserService.class);
    }

    @Override
    public void start() throws IOException {

        do {
            dataOutputStream.writeUTF(AUTHENTICATED_MENU_COMMANDS);
            String command = dataInputStream.readUTF();

            switch (command) {
                case "create current account" -> {

                    float amount = getDirectInput.getPossibleAmount();
                    if (accountService.createCurrentAccount(amount, LocalDate.now(), null, user.getId())) {
                        dataOutputStream.writeUTF(CREATE_CURRENT_ACCOUNT_SUCCESS);
                        dataInputStream.readUTF();
                    } else {
                        dataOutputStream.writeUTF(CREATE_CURRENT_ACCOUNT_ERROR);
                        dataInputStream.readUTF();
                    }
                }
                case "view current accounts" -> {
                    dataOutputStream.writeUTF(CURRENT_ACCOUNTS_INPUT + accountService.findCurrentAccountsByUserId(user.getId()));
                    dataInputStream.readUTF();
                }
                case "view current account" -> {

                    int id = getDirectInput.getPossibleId();
                    Account currentAccount = accountService.findCurrentAccountById(id);
                    if (Objects.nonNull(currentAccount)) {
                        dataOutputStream.writeUTF(VIEW_CURRENT_ACCOUNT_SUCCESS);
                        dataInputStream.readUTF();
                        new AccountMenu(dataInputStream, dataOutputStream, currentAccount).start();
                    } else {
                        dataOutputStream.writeUTF(VIEW_CURRENT_ACCOUNT_ERROR);
                        dataInputStream.readUTF();
                    }
                }
                case "create deposit" -> {

                    float amount = getDirectInput.getPossibleAmount();
                    int timeframe = getDirectInput.getPossibleTimeframe();
                    if (accountService.createDeposit(amount, LocalDate.now(), LocalDate.now().plusMonths(timeframe), user.getId())) {
                        dataOutputStream.writeUTF(CREATE_DEPOSIT_SUCCESS);
                        dataInputStream.readUTF();
                    } else {
                        dataOutputStream.writeUTF(CREATE_DEPOSIT_ERROR);
                        dataInputStream.readUTF();
                    }
                }
                case "view deposits" -> {
                    dataOutputStream.writeUTF(DEPOSITS_INPUT + accountService.findDepositsByUserId(user.getId()));
                    dataInputStream.readUTF();
                }
                case "make loan" -> {

                    float amount = getDirectInput.getPossibleAmount();
                    int timeframe = getDirectInput.getPossibleTimeframe();
                    if (accountService.createLoan(amount, LocalDate.now(), LocalDate.now().plusMonths(timeframe), user.getId())) {
                        dataOutputStream.writeUTF(CREATE_LOAN_SUCCESS);
                        dataInputStream.readUTF();
                    } else {
                        dataOutputStream.writeUTF(CREATE_LOAN_ERROR);
                        dataInputStream.readUTF();
                    }
                }
                case "view loans" -> {
                    dataOutputStream.writeUTF(LOANS_INPUT + accountService.findLoansByUserId(user.getId()));
                    dataInputStream.readUTF();
                }
                case "view transactions" -> {

                    dataOutputStream.writeUTF(TRANSACTIONS_INPUT + userService.viewTransactions(user.getId()));
                    dataInputStream.readUTF();
                }
                case "add days" -> {

                    int days = getDirectInput.getPossibleDays();
                    TimeSimulation.addDays(days);
                    dataOutputStream.writeUTF(TRANSACTIONS_INPUT + userService.viewTransactions(user.getId()));
                    dataInputStream.readUTF();
                }
                case "delete user" -> {

                    userService.deleteUser(user.getId());
                    return;
                }
                case "logout" -> {

                    return;
                }
                default -> {

                    dataOutputStream.writeUTF(INVALID_COMMAND_ERROR);
                    dataInputStream.readUTF();
                }
            }
        } while (true);
    }
}
