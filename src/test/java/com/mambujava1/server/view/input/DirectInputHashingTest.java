package com.mambujava1.server.view.input;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static com.mambujava1.server.view.input.DirectInputHashing.getSha256;

class DirectInputHashingTest {

    @Test
    void getSha256IsCorrect() {

        // given
        String expected = "ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f";

        // when
        String actual = getSha256("password123");

        // then
        assertEquals(expected, actual);
    }
}