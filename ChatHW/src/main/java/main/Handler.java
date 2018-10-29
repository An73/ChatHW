package main;

import base.UserProfile;
import db.DBService;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Handler implements Runnable{

    private Scanner scanner;
    private Server server;
    private PrintWriter outputStream;
    private DBService dbService;
    private UserProfile userProfile;
    private ArrayList<Handler> clients;

    public Handler(Socket clientSocket, DBService dbService, ArrayList<Handler> clients, Server server) {
        try {
            this.clients = clients;
            this.server = server;
            this.dbService = dbService;
            this.scanner = new Scanner(clientSocket.getInputStream());
            outputStream = new PrintWriter(clientSocket.getOutputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (scanner.hasNext()) {
                    String message = scanner.nextLine();
                    checkMessage(message);
                }
                Thread.sleep(100);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            server.removeClient(this);
        }
    }

    public void checkMessage(String message){
        String[] arraySplit = message.split("/");
        try {
            if (arraySplit[0].equals("SignIn")) {

                userProfile = dbService.getUser(arraySplit[1]);
                if (userProfile != null && arraySplit[2].equals(userProfile.getPassword())) {
                    outputStream.println("signIn/true");
                    server.sendMessageAllClients("Message/" + "Server: " + userProfile.getNickname() + " has connected to the chat!");
                    clients.add(this);
                    System.out.println(userProfile.getNickname() + " has connected to the chat!");
                }
                else
                    outputStream.println("signIn/false");
                outputStream.flush();
            }

            else if (arraySplit[0].equals("Message"))
                server.sendMessageAllClients("Message/" + userProfile.getNickname() + ": " + arraySplit[1]);

            else if (arraySplit[0].equals("Logout")) {
                StringBuilder users;
                server.sendMessageAllClients("Message/" + "Server: " + userProfile.getNickname() + " has disconnected from the chat!");
                clients.remove(this);
                server.sendMessageAllClients("Count/" + clients.size());
                users = new StringBuilder("Users/");
                for (Handler u : clients) {
                    users.append(u.userProfile.getNickname()).append("/");
                }
                server.sendMessageAllClients(users.toString());
                System.out.println(userProfile.getNickname() + " has disconnected from the chat!");
            }

            else if (arraySplit[0].equals("SignUp")) {
                userProfile = dbService.getUser(arraySplit[1]);
                if (userProfile == null && !dbService.checkNick(arraySplit[3])){
                    dbService.addUser(new UserProfile(arraySplit[1], arraySplit[2], arraySplit[3]));
                    outputStream.println("SignUp/YES/YES");
                    System.out.println("SignUp: " + arraySplit[1] + ", " + arraySplit[2] + ", " + arraySplit[3]);
                }
                else if (userProfile != null)
                    outputStream.println("SignUp/NO/NO");
                else {
                    outputStream.println("SignUp/YES/NO");
                }
                outputStream.flush();
            }

            else if (arraySplit[0].equals("GetCount")) {
                StringBuilder users;
                server.sendMessageAllClients("Count/" + clients.size());
                users = new StringBuilder("Users/");
                for (Handler u : clients) {
                    users.append(u.userProfile.getNickname()).append("/");
                }
                server.sendMessageAllClients(users.toString());
            }

        } catch (Exception e) {
                e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        outputStream.println(message);
        outputStream.flush();
    }

}
