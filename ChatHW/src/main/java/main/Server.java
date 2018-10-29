package main;

import db.DBService;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    static final int PORT = 7675;
    private ArrayList<Handler> clients = new ArrayList<Handler>();

    public Server (DBService dbService) throws Exception {

        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("server start");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                Handler client = new Handler(clientSocket, dbService, clients, this);
                new Thread(client).start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void removeClient(Handler client) {
        clients.remove(client);
    }

    public void sendMessageAllClients(String message) {
        for (Handler handler : clients)
            handler.sendMessage(message);
    }


}

