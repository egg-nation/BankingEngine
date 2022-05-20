package com.mambujava1.server.view.input;

import org.junit.jupiter.api.Test;

import static com.mambujava1.server.view.input.DirectInputValidator.*;
import static org.junit.jupiter.api.Assertions.*;

class DirectInputValidatorTest {

    @Test
    void checkUsernameIsValid() {

        // given
        String username = "lukas123";
        boolean expected = true;

        // when
        boolean actual = checkUsername(username);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void checkUsernameIsInvalid() {

        // then
        assertAll(
                () -> assertFalse(checkUsername("lukas")),
                () -> assertFalse(checkUsername("someexampleofusernamewithmorethan30thirtycharactersheresoitshouldbefalseifallsgood")),
                () -> assertFalse(checkUsername("1lukas")),
                () -> assertFalse(checkUsername("_lukas")),
                () -> assertFalse(checkUsername(""))
        );
    }

    @Test
    void checkEmailAddressIsValid() {

        // given
        String email = "lukas.devenas@mambu.com";
        boolean expected = true;

        // when
        boolean actual = checkEmailAddress(email);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void checkEmailAddressIsInvalid() {

        // then
        assertAll(
                () -> assertFalse(checkEmailAddress("lukas.devenasmambu.com")),
                () -> assertFalse(checkEmailAddress("lukas@.com")),
                () -> assertFalse(checkEmailAddress("@mambu.com")),
                () -> assertFalse(checkEmailAddress(".lukas@mambu.com")),
                () -> assertFalse(checkEmailAddress("lukas.@mambu.com")),
                () -> assertFalse(checkEmailAddress(""))
        );
    }

    @Test
    void checkPasswordIsValid() {

        // given
        String password = "Password123!";
        boolean expected = true;

        // when
        boolean actual = checkPassword(password);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void checkPasswordIsInvalid() {

        // given
        assertAll(
                () -> assertFalse(checkPassword("Paswo1!")),
                () -> assertFalse(checkPassword("Password1")),
                () -> assertFalse(checkPassword("Password!")),
                () -> assertFalse(checkPassword("password1!")),
                () -> assertFalse(checkPassword(""))
        );
    }

    @Test
    void checkPinIsValid() {

        // given
        String pin = "1234";
        boolean expected = true;

        // when
        boolean actual = checkPin(pin);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void checkPinIsInvalid() {

        // given
        assertAll(
                () -> assertFalse(checkPin("123a")),
                () -> assertFalse(checkPin("123")),
                () -> assertFalse(checkPin("12345")),
                () -> assertFalse(checkPin("1234a")),
                () -> assertFalse(checkPin(""))
        );
    }

    @Test
    void checkAmountIsValid() {

        // given
        String amount = "12.34";
        Float expected = 12.34f;

        // when
        Float actual = checkAmount(amount);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void checkAmountIsInvalid() {

        // given
        assertAll(
                () -> assertNull(checkAmount("12.3a")),
                () -> assertNull(checkAmount("0.0f")),
                () -> assertNull(checkAmount("-2.0f")),
                () -> assertNull(checkAmount("amount")),
                () -> assertNull(checkAmount(""))
        );
    }

    @Test
    void checkTimeframeIsValid() {

        // given
        String timeframe = "2";
        Integer expected = 2;

        // when
        Integer actual = checkTimeframe(timeframe);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void checkTimeframeIsInvalid() {

        // then
        assertAll(
                () -> assertNull(checkTimeframe("a")),
                () -> assertNull(checkTimeframe("0")),
                () -> assertNull(checkTimeframe("-1")),
                () -> assertNull(checkTimeframe("2a")),
                () -> assertNull(checkTimeframe(""))
        );
    }

    @Test
    void checkIdIsValid() {

        // given
        String id = "1";
        Integer expected = 1;

        // when
        Integer actual = checkId(id);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void checkIdIsInvalid() {

        // then
        assertAll(
                () -> assertNull(checkId("a")),
                () -> assertNull(checkId("0")),
                () -> assertNull(checkId("-1")),
                () -> assertNull(checkId("2a")),
                () -> assertNull(checkId(""))
        );
    }

    @Test
    void checkDaysIsValid() {

        // given
        String days = "3";
        Integer expected = 3;

        // when
        Integer actual = checkDays(days);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void checkDaysIsInvalid() {

        // then
        assertAll(
                () -> assertNull(checkDays("a")),
                () -> assertNull(checkDays("-1")),
                () -> assertNull(checkDays("2a")),
                () -> assertNull(checkDays(""))
        );
    }
}