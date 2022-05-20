package com.mambujava1.server.view.messages;

public class GeneralInfo {

    public static final String USERNAME_VALIDATION = """
            An account username must contain:
            - 6 to 30 characters
            - alphanumeric characters and underscores
            - the first character must be a letter
            """;

    public static final String PASSWORD_VALIDATION = """
            A password must contain:
            - at least 8 characters
            - an uppercase letter
            - a lowercase letter
            - a digit
            - a special character
            """;

    public static final String PIN_VALIDATION = """
            A pin must contain:
            - 4 digits
            """;

}
