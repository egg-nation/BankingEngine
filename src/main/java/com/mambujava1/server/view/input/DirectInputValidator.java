package com.mambujava1.server.view.input;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DirectInputValidator {

    public static boolean checkUsername(String username) {

        Pattern usernameFormat = Pattern.compile("^[A-Za-z]\\w{5,29}$");
        Matcher checkUsernameFormat = usernameFormat.matcher(username);

        return checkUsernameFormat.matches();
    }

    public static boolean checkEmailAddress(String emailAddress) {

        Pattern emailAddressFormat = java.util.regex.Pattern.compile("^[a-zA-Z][a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-][a-zA-Z0-9]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$");
        Matcher checkEmailAddressFormat = emailAddressFormat.matcher(emailAddress);

        return checkEmailAddressFormat.matches();
    }

    public static boolean checkPassword(String password) {

        if (password.length() >= 8) {
            Pattern uppercaseLetter = Pattern.compile("[A-Z]");
            Pattern lowercaseLetter = Pattern.compile("[a-z]");
            Pattern numericValue = Pattern.compile("[0-9]");
            Pattern specialCharacter = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]");

            Matcher checkUppercaseLetter = uppercaseLetter.matcher(password);
            Matcher checkLowercaseLetter = lowercaseLetter.matcher(password);
            Matcher checkNumericValue = numericValue.matcher(password);
            Matcher checkSpecialCharacter = specialCharacter.matcher(password);

            return (checkUppercaseLetter.find() && checkLowercaseLetter.find() && checkNumericValue.find() && checkSpecialCharacter.find());
        }

        return false;
    }

    public static boolean checkPin(String pin) {

        Pattern pinFormat = Pattern.compile("[0-9]{4}");
        Matcher checkPinFormat = pinFormat.matcher(pin);

        return checkPinFormat.matches();
    }

    public static Float checkAmount(String amountInput) {

        float amount;
        try {
            amount = Float.parseFloat(amountInput);
        } catch (NumberFormatException | NullPointerException e) {
            return null;
        }
        if (amount <= 0.0f) {
            return null;
        }
        return amount;
    }

    public static Integer checkTimeframe(String timeframeInput) {

        int timeframe;
        try {
            timeframe = Integer.parseInt(timeframeInput);
        } catch (NumberFormatException | NullPointerException e) {
            return null;
        }
        if (timeframe <= 0) {
            return null;
        }
        return timeframe;
    }

    public static Integer checkId(String idInput) {

        int id;
        try {
            id = Integer.parseInt(idInput);
        } catch (NumberFormatException | NullPointerException e) {
            return null;
        }
        if (id <= 0) {
            return null;
        }
        return id;
    }

    public static Integer checkDays(String daysInput) {

        int days;
        try {
            days = Integer.parseInt(daysInput);
        } catch (NumberFormatException | NullPointerException e) {
            return null;
        }
        if (days < 0) {
            return null;
        }
        return days;
    }
}
