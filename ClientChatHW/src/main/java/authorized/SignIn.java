package authorized;

import chat.ControllerChat;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.*;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Created by dkotenko on 10/24/18.
 */
public class SignIn implements Initializable {

    @FXML private TextArea loginBox;
    @FXML private PasswordField passwordBox;
    @FXML private Button signInButton;
    @FXML private Button signUpButton;
    private Socket socket;
    private PrintWriter outputMessage;
    private Scanner inputMessage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            socket = new Socket("127.0.0.1", 7675);
            outputMessage = new PrintWriter(socket.getOutputStream());
            inputMessage = new Scanner(socket.getInputStream());
        } catch (IOException e) {
            System.out.println("Connection refused");
        }
        loginBox.setFont(Font.font(20));
        passwordBox.setFont(Font.font(20));
        signInButton.setFont(Font.font(20));
        signUpButton.setFont(Font.font(20));

        listenButtonSignIn();
        listenButtonSignUp();
    }

    private void listenButtonSignIn() {
        signInButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String input;

                if (loginBox.getText().equals("") || passwordBox.getText().equals("")) {
                    loginBox.clear();
                    passwordBox.clear();
                    return;
                }
                outputMessage.println("SignIn/" + loginBox.getText() + "/" + passwordBox.getText());
                outputMessage.flush();
                input = inputMessage.nextLine();
                if (input.split("/")[0].equals("signIn") &&
                        input.split("/")[1].equals("true")) {
                    chatStage();
                }
                else {
                    loginBox.clear();
                    passwordBox.clear();
                }
            }
        });
    }

    private void listenButtonSignUp(){
        signUpButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Stage stage = (Stage) signUpButton.getScene().getWindow();
                stage.close();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/signUp.fxml"));
                try {
                    AnchorPane root = (AnchorPane) fxmlLoader.load();
                    stage = new Stage();
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setTitle("SignUp");
                    stage.setScene(new Scene(root, 400, 400));

                    SignUp signUp = fxmlLoader.getController();
                    signUp.setIO(socket);
                    signUp.listenButtonSignUp();
                    signUp.listenButtonSignIn();

                    stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        public void handle(WindowEvent we) {
                            System.out.println("Stage is closing");
                            System.exit(0);
                        }
                    });
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void chatStage(){
        Stage stage = (Stage) signInButton.getScene().getWindow();
        stage.close();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/chat.fxml"));
        try {
            AnchorPane root = (AnchorPane) fxmlLoader.load();
            stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Chat HW");
            stage.setScene(new Scene(root, 800, 600));

            ControllerChat controllerChat = fxmlLoader.getController();
            controllerChat.runClient(socket);

            stage.show();
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    System.out.println("Stage is closing");
                    outputMessage.println("Logout/");
                    outputMessage.flush();
                    System.exit(0);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
