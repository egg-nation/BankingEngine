package com.mambujava1.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static final String IP_ADDRESS = "localhost";
    private static final int PORT = 5056;

    public final int port;
    public final InetAddress ip;

    protected Socket clientSocket;

    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;

    public Client(int port) throws IOException {

        this.port = port;
        this.ip = InetAddress.getByName(IP_ADDRESS);
        this.clientSocket = new Socket(this.ip, this.port);

        this.dataInputStream = new DataInputStream(clientSocket.getInputStream());
        this.dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
    }

    public void startServerClientCommunication() throws IOException {

        Scanner scanCommand = new Scanner(System.in);
        String command;
        do {
            System.out.println(dataInputStream.readUTF());
            command = scanCommand.nextLine();
            dataOutputStream.writeUTF(command);
        } while (!command.equals("exit"));

        scanCommand.close();
        dataInputStream.close();
        dataOutputStream.close();
    }

    public static void main(String[] args) throws IOException {

        Client client = new Client(PORT);
        client.startServerClientCommunication();
    }
}