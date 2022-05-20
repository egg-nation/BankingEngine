package com.mambujava1.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {

    private static final int PORT = 5056;

    private final int port;
    private final ServerSocket serverSocket;

    public Server(int port) throws IOException {

        this.port = port;
        this.serverSocket = new ServerSocket(port);
    }

    public int getPort() {

        return port;
    }

    public void getClient() throws IOException {

        Socket client = serverSocket.accept();

        try {
            System.out.println("A new client is connected: " + client);

            DataInputStream dataInputStream = new DataInputStream(client.getInputStream());
            DataOutputStream dataOutputStream = new DataOutputStream(client.getOutputStream());

            startClientThread(client, dataInputStream, dataOutputStream);
        } catch (Exception e) {

            assert client != null;
            client.close();
            e.printStackTrace();
        }
    }

    private void startClientThread(Socket client, DataInputStream dataInputStream, DataOutputStream dataOutputStream) {

        System.out.println("Assigning new thread for this client");

        Thread clientThread = new ClientHandler(client, dataInputStream, dataOutputStream);
        clientThread.start();
    }

    public static void main(String[] args) throws IOException {

        Server newServer = new Server(PORT);

        do {
            try {
                newServer.getClient();
            } catch (Exception e) {
                e.printStackTrace();
            }
            newServer.getClient();
        } while (true);
    }
}