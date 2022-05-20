package com.mambujava1.server.view;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mambujava1.server.ioc.IoCModuleService;
import com.mambujava1.server.model.Card;
import com.mambujava1.server.model.User;
import com.mambujava1.server.service.CardService;
import com.mambujava1.server.service.UserService;
import com.mambujava1.server.view.input.GetDirectInput;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Objects;

import static com.mambujava1.server.view.messages.Commands.MAIN_MENU_COMMANDS;
import static com.mambujava1.server.view.messages.Errors.*;
import static com.mambujava1.server.view.messages.Success.*;

public class MainMenu implements Menu {

    private final Socket client;
    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;

    private final GetDirectInput getDirectInput;

    private final UserService userService;
    private final CardService cardService;

    public MainMenu(Socket client, DataInputStream dataInputStream, DataOutputStream dataOutputStream) throws IOException {

        this.client = client;
        this.dataInputStream = dataInputStream;
        this.dataOutputStream = dataOutputStream;

        this.getDirectInput = new GetDirectInput(dataInputStream, dataOutputStream);

        Injector injector = Guice.createInjector(new IoCModuleService());
        this.userService = injector.getInstance(UserService.class);
        this.cardService = injector.getInstance(CardService.class);
    }

    @Override
    public void start() throws IOException {

        do {
            dataOutputStream.writeUTF(MAIN_MENU_COMMANDS);
            String command = dataInputStream.readUTF();

            switch (command) {
                case "login" -> {

                    String username = getDirectInput.getUsername();
                    String password = getDirectInput.getPasswordHash();

                    User user = userService.login(username, password);
                    if (Objects.nonNull(user)) {
                        dataOutputStream.writeUTF(LOGIN_SUCCESS);
                        dataInputStream.readUTF();
                        new AuthenticatedMenu(dataInputStream, dataOutputStream, user).start();
                    } else {
                        dataOutputStream.writeUTF(LOGIN_ERROR);
                        dataInputStream.readUTF();
                    }
                }
                case "register" -> {

                    String firstName = getDirectInput.getFirstName();
                    String lastName = getDirectInput.getLastName();
                    String address = getDirectInput.getAddress();
                    String username = getDirectInput.getPossibleUsername();
                    String emailAddress = getDirectInput.getPossibleEmailAddress();
                    String passwordHash = getDirectInput.getPossiblePasswordHash();

                    User user = userService.createUser(firstName, lastName, address, username, emailAddress, passwordHash);
                    if (Objects.nonNull(user)) {
                        dataOutputStream.writeUTF(REGISTER_SUCCESS);
                        dataInputStream.readUTF();
                        new AuthenticatedMenu(dataInputStream, dataOutputStream, user).start();
                    } else {
                        dataOutputStream.writeUTF(REGISTER_ERROR);
                        dataInputStream.readUTF();
                    }
                }
                case "pay" -> {

                    int cardId = getDirectInput.getPossibleId();
                    Card card = cardService.findById(cardId);

                    if (Objects.nonNull(card)) {
                        dataOutputStream.writeUTF(CARD_ID_SUCCESS);
                        dataInputStream.readUTF();
                        new PaymentMenu(dataInputStream, dataOutputStream, card).start();
                    } else {
                        dataOutputStream.writeUTF(CARD_ID_ERROR);
                        dataInputStream.readUTF();
                    }
                }
                case "exit" -> {

                    System.out.println("Client " + client + " sent exit command...");
                    System.out.println("Closing this connection.");
                    this.client.close();

                    System.out.println("Connection closed");
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
