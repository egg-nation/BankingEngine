package com.mambujava1.server.view.input;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static com.mambujava1.server.view.input.DirectInputHashing.getSha256;
import static com.mambujava1.server.view.input.DirectInputValidator.*;
import static com.mambujava1.server.view.messages.Errors.*;
import static com.mambujava1.server.view.messages.GeneralInfo.*;
import static com.mambujava1.server.view.messages.InputFields.*;

public class GetDirectInput {

    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;

    public GetDirectInput(DataInputStream dataInputStream, DataOutputStream dataOutputStream) {

        this.dataInputStream = dataInputStream;
        this.dataOutputStream = dataOutputStream;
    }

    public String getFirstName() throws IOException {

        dataOutputStream.writeUTF(FIRST_NAME_INPUT);
        return dataInputStream.readUTF();
    }

    public String getLastName() throws IOException {

        dataOutputStream.writeUTF(LAST_NAME_INPUT);
        return dataInputStream.readUTF();
    }

    public String getAddress() throws IOException {

        dataOutputStream.writeUTF(ADDRESS_INPUT);
        return dataInputStream.readUTF();
    }

    public String getUsername() throws IOException {

        dataOutputStream.writeUTF(USERNAME_INPUT);
        return dataInputStream.readUTF();
    }

    public String getEmailAddress() throws IOException {

        dataOutputStream.writeUTF(EMAIL_ADDRESS_INPUT);
        return dataInputStream.readUTF();
    }

    public String getPasswordHash() throws IOException {

        dataOutputStream.writeUTF(PASSWORD_INPUT);
        return getSha256(dataInputStream.readUTF().trim());
    }

    private String getPassword() throws IOException {

        dataOutputStream.writeUTF(PASSWORD_INPUT);
        return dataInputStream.readUTF().trim();
    }

    private String getAmount() throws IOException {

        dataOutputStream.writeUTF(AMOUNT_INPUT);
        return dataInputStream.readUTF();
    }

    private String getId() throws IOException {

        dataOutputStream.writeUTF(ID_INPUT);
        return dataInputStream.readUTF();
    }

    private String getDays() throws IOException {

        dataOutputStream.writeUTF(DAYS_INPUT);
        return dataInputStream.readUTF();
    }

    public String getPin() throws IOException {

        dataOutputStream.writeUTF(PIN_INPUT);
        return dataInputStream.readUTF();
    }

    private String getTimeframe() throws IOException {

        dataOutputStream.writeUTF(TIMEFRAME_INPUT);
        return dataInputStream.readUTF();
    }

    public String getPossibleUsername() throws IOException {

        dataOutputStream.writeUTF(USERNAME_VALIDATION);
        dataInputStream.readUTF();

        String username = getUsername();

        while (!checkUsername(username)) {
            dataOutputStream.writeUTF(USERNAME_ERROR);
            dataInputStream.readUTF();

            username = getUsername();
        }

        return username;
    }

    public String getPossibleEmailAddress() throws IOException {

        String emailAddress = getEmailAddress();

        while (!checkEmailAddress(emailAddress)) {
            dataOutputStream.writeUTF(EMAIL_ADDRESS_ERROR);
            dataInputStream.readUTF();

            emailAddress = getEmailAddress();
        }

        return emailAddress;
    }

    public String getPossiblePasswordHash() throws IOException {

        dataOutputStream.writeUTF(PASSWORD_VALIDATION);
        dataInputStream.readUTF();

        String password = getPassword();

        while (!checkPassword(password)) {
            dataOutputStream.writeUTF(PASSWORD_ERROR);
            dataInputStream.readUTF();

            password = getPassword();
        }

        return getSha256(password);
    }

    public float getPossibleAmount() throws IOException {

        String amountInput = getAmount();
        Float amount;

        while ((amount = checkAmount(amountInput)) == null) {
            dataOutputStream.writeUTF(WRONG_FORMAT_ERROR);
            dataInputStream.readUTF();

            amountInput = getAmount();
        }

        return amount;
    }

    public int getPossibleId() throws IOException {

        String idInput = getId();
        Integer id;

        while ((id = checkId(idInput)) == null) {
            dataOutputStream.writeUTF(WRONG_FORMAT_ERROR);
            dataInputStream.readUTF();

            idInput = getId();
        }

        return id;
    }

    public int getPossibleDays() throws IOException {

        String daysInput = getDays();
        Integer days;

        while ((days = checkDays(daysInput)) == null) {
            dataOutputStream.writeUTF(WRONG_FORMAT_ERROR);
            dataInputStream.readUTF();

            daysInput = getDays();
        }

        return days;
    }

    public String getPossiblePin() throws IOException {

        dataOutputStream.writeUTF(PIN_VALIDATION);
        dataInputStream.readUTF();

        String pin = getPin();

        while (!checkPin(pin)) {
            dataOutputStream.writeUTF(WRONG_FORMAT_ERROR);
            dataInputStream.readUTF();

            pin = getPin();
        }

        return pin;
    }

    public int getPossibleTimeframe() throws IOException {

        String timeframeInput = getTimeframe();
        Integer timeframe;

        while ((timeframe = checkTimeframe(timeframeInput)) == null) {
            dataOutputStream.writeUTF(WRONG_FORMAT_ERROR);
            dataInputStream.readUTF();

            timeframeInput = getAmount();
        }

        return timeframe;
    }
}