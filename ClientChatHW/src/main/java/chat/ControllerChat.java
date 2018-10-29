package chat;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;


public class ControllerChat implements Initializable {

    @FXML private TextArea messageBox;
    @FXML private TextArea inputMessage;
    @FXML public Button buttonSend;
    @FXML private Button buttonLogout;
    @FXML private TextArea countBox;
    @FXML private TextArea usersBox;
    private Socket socket;
    private Listener listener;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        messageBox.setEditable(false);
        messageBox.setWrapText(true);
        countBox.setEditable(false);
        countBox.setWrapText(true);
        usersBox.setEditable(false);
        usersBox.setWrapText(true);
    }

    public void runClient(Socket socket) {
        listener = new Listener(this, socket);
        this.socket = socket;
        new Thread(listener).start();
    }

    public void setMessageBox(String message){
        messageBox.setText(messageBox.getText() + "\n" + message);
    }

    public void setCountBox(String count){
        countBox.clear();
        countBox.setText("Number of users:\n" + count);
    }

    public void setUsersBox(String[] users){
        usersBox.clear();
        StringBuilder stringBuilder = new StringBuilder("Users:\n");
        for (int i = 1; i < users.length; i++) {
            stringBuilder.append(users[i]).append("\n");
        }
        usersBox.setText(stringBuilder.toString());
    }

    public void listenButtonSend(final PrintWriter outputMessage){
        buttonSend.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (!inputMessage.getText().equals("")) {
                    outputMessage.println("Message/" + inputMessage.getText());
                    outputMessage.flush();
                    inputMessage.clear();
                }
            }
        });
    }

    public void listenButtonLogout(final PrintWriter outputMessage) {
        buttonLogout.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                outputMessage.println("Logout/");
                outputMessage.flush();

                Stage stage = (Stage) buttonLogout.getScene().getWindow();
                stage.close();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/signIn.fxml"));
                try {
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
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
