package com.mambujava1.server.repository.session;

import org.jdbi.v3.core.Jdbi;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class DatabaseConnection {

    private static DatabaseConnection INSTANCE;
    private Jdbi jdbi;

    private DatabaseConnection() {

        try {
            Properties properties = new Properties();
            FileInputStream fileInputStream = new FileInputStream("database.properties");
            properties.load(fileInputStream);
            fileInputStream.close();

            String url = properties.getProperty("jdbc.url");
            String username = properties.getProperty("jdbc.username");
            String password = properties.getProperty("jdbc.password");
            this.jdbi = Jdbi.create(url, username, password);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static DatabaseConnection getInstance() {

        if (INSTANCE == null) {
            synchronized (DatabaseConnection.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DatabaseConnection();
                }
            }
        }

        return INSTANCE;
    }

    public Jdbi getConnection() {

        return jdbi;
    }
}
