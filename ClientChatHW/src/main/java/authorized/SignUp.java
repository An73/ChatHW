package authorized;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

/**
 * Created by dkotenko on 10/26/18.
 */
public class SignUp implements Initializable {
    @FXML private TextArea loginBox;
    @FXML private PasswordField passwordBox1;
    @FXML private PasswordField passwordBox2;
    @FXML private TextArea nicknameBox;
    @FXML private Button signUpButton;
    @FXML private Button signInButton;
    private PrintWriter outputMessage;
    private Scanner inputMessage;
    private Socket socket;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loginBox.setFont(Font.font(20));
        passwordBox1.setFont(Font.font(20));
        passwordBox2.setFont(Font.font(20));
        nicknameBox.setFont(Font.font(20));
        signInButton.setFont(Font.font(20));
        signUpButton.setFont(Font.font(20));
    }

    public void listenButtonSignUp() {
        signUpButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                String input;
                String[] arraySplit;
                if (!passwordBox1.getText().equals(passwordBox2.getText())) {
                    passwordBox1.clear();
                    passwordBox2.clear();
                    return;
                }
                outputMessage.println("SignUp/" + loginBox.getText() + "/" + passwordBox1.getText() +
                "/" + nicknameBox.getText());
                outputMessage.flush();
                input = inputMessage.nextLine();
                arraySplit = input.split("/");
                if (arraySplit[0].equals("SignUp")) {
                    if (arraySplit[1].equals("NO"))
                        loginBox.clear();
                    else if (arraySplit[2].equals("NO"))
                        nicknameBox.clear();
                    else {
                        setSignInStage();
                    }
                }
            }
        });
    }

    public void listenButtonSignIn(){
        signInButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                setSignInStage();
            }
        });
    }

    public void setIO(Socket socket) {
        try {
            this.socket = socket;
            outputMessage = new PrintWriter(socket.getOutputStream());
            inputMessage = new Scanner(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setSignInStage(){
        Stage stage = (Stage) signUpButton.getScene().getWindow();
        stage.close();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/signIn.fxml"));
        try {
            socket.close();
            AnchorPane root1 = (AnchorPane) fxmlLoader.load();
            stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("SignIn");
            stage.setScene(new Scene(root1, 400, 400));

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
}
