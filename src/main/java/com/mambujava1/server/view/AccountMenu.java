package com.mambujava1.server.view;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mambujava1.server.ioc.IoCModuleService;
import com.mambujava1.server.model.Account;
import com.mambujava1.server.service.AccountService;
import com.mambujava1.server.service.CardService;
import com.mambujava1.server.view.input.GetDirectInput;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.LocalDate;

import static com.mambujava1.server.view.messages.Commands.ACCOUNT_COMMANDS;
import static com.mambujava1.server.view.messages.Errors.*;
import static com.mambujava1.server.view.messages.InputFields.*;
import static com.mambujava1.server.view.messages.Success.*;

public class AccountMenu implements Menu {

    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;
    private final GetDirectInput getDirectInput;

    private final Account account;
    private final AccountService accountService;
    private final CardService cardService;

    public AccountMenu(DataInputStream dataInputStream, DataOutputStream dataOutputStream, Account account) {

        this.dataInputStream = dataInputStream;
        this.dataOutputStream = dataOutputStream;
        this.getDirectInput = new GetDirectInput(dataInputStream, dataOutputStream);

        this.account = account;

        Injector injector = Guice.createInjector(new IoCModuleService());
        this.accountService = injector.getInstance(AccountService.class);
        this.cardService = injector.getInstance(CardService.class);
    }

    @Override
    public void start() throws IOException {

        do {
            dataOutputStream.writeUTF(ACCOUNT_COMMANDS);
            String command = dataInputStream.readUTF();

            switch (command) {
                case "view current balance" -> {

                    dataOutputStream.writeUTF(CURRENT_BALANCE_INPUT + accountService.findCurrentAccountById(account.getId()).getAmount());
                    dataInputStream.readUTF();
                }
                case "view transactions" -> {

                    dataOutputStream.writeUTF(TRANSACTIONS_INPUT + accountService.viewTransactions(account.getId()));
                    dataInputStream.readUTF();
                }
                case "deposit cash" -> {

                    float amount = getDirectInput.getPossibleAmount();
                    if (accountService.depositCash(account.getId(), amount)) {
                        dataOutputStream.writeUTF(DEPOSIT_CASH_SUCCESS);
                        dataInputStream.readUTF();
                    } else {
                        dataOutputStream.writeUTF(DEPOSIT_CASH_ERROR);
                        dataInputStream.readUTF();
                    }
                }
                case "withdraw cash" -> {

                    float amount = getDirectInput.getPossibleAmount();
                    if (accountService.withdrawCash(account.getId(), amount)) {
                        dataOutputStream.writeUTF(WITHDRAW_CASH_SUCCESS);
                        dataInputStream.readUTF();
                    } else {
                        dataOutputStream.writeUTF(WITHDRAW_CASH_ERROR);
                        dataInputStream.readUTF();
                    }
                }
                case "transfer money" -> {

                    float amount = getDirectInput.getPossibleAmount();
                    int toAccountId = getDirectInput.getPossibleId();

                    if (accountService.transferMoney(account.getId(), toAccountId, amount)) {
                        dataOutputStream.writeUTF(TRANSFER_MONEY_SUCCESS);
                        dataInputStream.readUTF();
                    } else {
                        dataOutputStream.writeUTF(TRANSFER_MONEY_ERROR);
                        dataInputStream.readUTF();
                    }
                }
                case "create card" -> {

                    String pin = getDirectInput.getPossiblePin();

                    if (cardService.createCard(pin, LocalDate.now(), LocalDate.now().plusYears(2), 1, account.getId())) {
                        dataOutputStream.writeUTF(CREATE_CARD_SUCCESS);
                        dataInputStream.readUTF();
                    } else {
                        dataOutputStream.writeUTF(CREATE_CARD_ERROR);
                        dataInputStream.readUTF();
                    }
                }
                case "view cards" -> {

                    dataOutputStream.writeUTF(CARDS_INPUT + accountService.viewCards(account.getId()));
                    dataInputStream.readUTF();
                }
                case "freeze card" -> {

                    int id = getDirectInput.getPossibleId();
                    String pin = getDirectInput.getPin();

                    if (cardService.freezeCard(id, pin)) {
                        dataOutputStream.writeUTF(FREEZE_CARD_SUCCESS);
                        dataInputStream.readUTF();
                    } else {
                        dataOutputStream.writeUTF(FREEZE_CARD_ERROR);
                        dataInputStream.readUTF();
                    }
                }
                case "unfreeze card" -> {

                    int id = getDirectInput.getPossibleId();
                    String pin = getDirectInput.getPin();

                    if (cardService.unfreezeCard(id, pin)) {
                        dataOutputStream.writeUTF(UNFREEZE_CARD_SUCCESS);
                        dataInputStream.readUTF();
                    } else {
                        dataOutputStream.writeUTF(UNFREEZE_CARD_ERROR);
                        dataInputStream.readUTF();
                    }
                }
                case "remove card" -> {

                    int id = getDirectInput.getPossibleId();
                    String pin = getDirectInput.getPin();

                    if (cardService.removeCard(id, pin)) {
                        dataOutputStream.writeUTF(REMOVE_CARD_SUCCESS);
                        dataInputStream.readUTF();
                    } else {
                        dataOutputStream.writeUTF(REMOVE_CARD_ERROR);
                        dataInputStream.readUTF();
                    }
                }
                case "delete account" -> {

                    accountService.deleteAccount(account.getId());
                    return;
                }
                case "back" -> {

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
