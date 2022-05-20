package com.mambujava1.server.view.messages;

public class Commands {

    public static final String MAIN_MENU_COMMANDS = """
            To login into user account type <login>
            To create a new user account type <register>
            To make a payment type <pay>
            To exit menu type <exit>
            """;

    public static final String PAYMENT_COMMANDS = """
            To introduce your card type <normal>
            To pay wireless type <wireless>
            To return to main menu type <exit>
            """;

    public static final String AUTHENTICATED_MENU_COMMANDS = """
            To open a new current account type <create current account>
            To check your current accounts type <view current accounts>
            To check a current account in detail type <view current account> 
            To create a new deposit - amount and timeframe - type <create deposit>
            To check your deposits - amount and timeframe - type <view deposits>
            To make a new loan - amount and timeframe - type <make loan>
            To check your loans - amount and timeframe - type <view loans>
            To check all the transactions - type <view transactions>
            To add days - type <add days>
            To delete the user type <delete user>
            To return to the previous menu type <logout>
            """;

    public static final String ACCOUNT_COMMANDS = """
            To check the current balance of the account type <view current balance>
            To view the transactions made with this account type <view transactions>
            To deposit cash to your account type <deposit cash>
            To withdraw cash from your account type <withdraw cash>
            To transfer money to another account type <transfer money>
            To create a new card type <create card>
            To see your current cards type <view cards>
            To freeze a card type <freeze card>
            To unfreeze a card type <unfreeze card>
            To remove a card type <remove card>
            To delete the current account type <delete account>
            To return to the previous menu type <back>
            """;
}
