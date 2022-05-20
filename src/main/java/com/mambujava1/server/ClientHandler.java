package com.mambujava1.server;


import com.mambujava1.server.view.MainMenu;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler extends Thread {

    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;
    private final Socket client;

    public ClientHandler(Socket client, DataInputStream dataInputStream, DataOutputStream dataOutputStream) {

        this.client = client;
        this.dataInputStream = dataInputStream;
        this.dataOutputStream = dataOutputStream;
    }

    @Override
    public void run() {

        try {
            new MainMenu(client, dataInputStream, dataOutputStream).start();
        } catch (IOException throwables) {
            throwables.printStackTrace();
        }

        try {
            this.dataInputStream.close();
            this.dataOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}