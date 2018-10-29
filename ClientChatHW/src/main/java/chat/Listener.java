package chat;

import authorized.SignIn;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by dkotenko on 10/23/18.
 */
public class Listener implements Runnable {
    private Scanner inputMessage;
    private PrintWriter outputMessage;
    private ControllerChat controllerChat;

    public Listener(ControllerChat controllerChat, Socket socket){
        this.controllerChat = controllerChat;

        try {
            inputMessage = new Scanner(socket.getInputStream());
            outputMessage = new PrintWriter(socket.getOutputStream());

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        controllerChat.listenButtonSend(outputMessage);
        controllerChat.listenButtonLogout(outputMessage);
    }

    @Override
    public void run() {
        String[] inputArray;
        outputMessage.println("GetCount/");
        outputMessage.flush();
        try {
            while (true) {
                if (inputMessage.hasNext()) {
                    inputArray = inputMessage.nextLine().split("/");
                    if (inputArray[0].equals("Message"))
                        controllerChat.setMessageBox(inputArray[1]);
                    else if (inputArray[0].equals("Count"))
                        controllerChat.setCountBox(inputArray[1]);
                    else if (inputArray[0].equals("Users")) {
                        controllerChat.setUsersBox(inputArray);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
