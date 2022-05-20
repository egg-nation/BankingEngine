package com.mambujava1.server.view;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mambujava1.server.ioc.IoCModuleService;
import com.mambujava1.server.model.Card;
import com.mambujava1.server.service.AccountService;
import com.mambujava1.server.view.input.GetDirectInput;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static com.mambujava1.server.view.constants.CardPaymentLimits.CARD_WIRELESS_LIMIT;
import static com.mambujava1.server.view.messages.Commands.PAYMENT_COMMANDS;
import static com.mambujava1.server.view.messages.Errors.INVALID_COMMAND_ERROR;
import static com.mambujava1.server.view.messages.Errors.PAY_WITH_CARD_ERROR;
import static com.mambujava1.server.view.messages.Success.PAY_WITH_CARD_SUCCESS;

public class PaymentMenu implements Menu {

    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;
    private final GetDirectInput getDirectInput;

    private final AccountService accountService;
    private final Card card;

    public PaymentMenu(DataInputStream dataInputStream, DataOutputStream dataOutputStream, Card card) {

        this.dataInputStream = dataInputStream;
        this.dataOutputStream = dataOutputStream;
        this.getDirectInput = new GetDirectInput(dataInputStream, dataOutputStream);

        this.card = card;

        Injector injector = Guice.createInjector(new IoCModuleService());
        this.accountService = injector.getInstance(AccountService.class);
    }

    @Override
    public void start() throws IOException {

        do {
            dataOutputStream.writeUTF(PAYMENT_COMMANDS);
            String command = dataInputStream.readUTF();

            switch (command) {
                case "normal" -> {

                    float amount = getDirectInput.getPossibleAmount();
                    String pin = getDirectInput.getPin();
                    performPayment(card.getId(), pin, amount);
                }
                case "wireless" -> {

                    float amount = getDirectInput.getPossibleAmount();
                    String pin;
                    if (amount >= CARD_WIRELESS_LIMIT) {
                        pin = getDirectInput.getPin();
                    } else {
                        pin = null;
                    }
                    performPayment(card.getId(), pin, amount);
                }
                case "exit" -> {

                    return;
                }
                default -> {

                    dataOutputStream.writeUTF(INVALID_COMMAND_ERROR);
                    dataInputStream.readUTF();
                }
            }

        } while (true);
    }

    private void performPayment(int id, String pin, float amount) throws IOException {

        if (accountService.payWithCard(id, pin, amount)) {
            dataOutputStream.writeUTF(PAY_WITH_CARD_SUCCESS);
            dataInputStream.readUTF();
        } else {
            dataOutputStream.writeUTF(PAY_WITH_CARD_ERROR);
            dataInputStream.readUTF();
        }
    }
}
