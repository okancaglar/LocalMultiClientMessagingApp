package com.example.localmessagingapp;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.net.Socket;
import java.net.URL;;
import java.util.ResourceBundle;

public class ChatViewController implements Initializable {

    private String userName;
    private ClientModel model;
    @FXML private Label lblUserName;
    @FXML private ScrollPane spChatMain;
    @FXML private VBox vbChat;

    @FXML private TextField tfMessageToSend;
    @FXML private Button btnSend;
    private Stage stage;


    public ChatViewController(String userName){this.userName = userName;}
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblUserName.setText(this.userName);
        model = new ClientModel(this.userName);
        model.getMessageFromServer(this.vbChat);

        btnSend.setOnAction(e->{
            if (!tfMessageToSend.getText().isEmpty()){
                this.model.sendDataToServer(this.userName + ": " + tfMessageToSend.getText());
                Platform.runLater(()->{
                    vbChat.getChildren().add(createMessageBoxToServer());
                    tfMessageToSend.clear();
                });
            }
        });
        vbChat.heightProperty().addListener(new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                    spChatMain.setVvalue((Double) t1);
            }
        });
    }

    public static HBox createMessageBoxFromServer(String message){
        HBox messageBox = new HBox();
        messageBox.setPadding(new Insets(7,7,7,7));
        messageBox.setAlignment(Pos.CENTER_LEFT);
        Text txtMessage = new Text(message);
        TextFlow txtfMessage = new TextFlow(txtMessage);
        messageBox.getChildren().add(txtfMessage);
        txtfMessage.setStyle("-fx-background-color: #53d769;"  +
                "-fx-background-radius: 25px");
        txtfMessage.setPadding(new Insets(7,7,7,7));
        return messageBox;
    }
    private HBox createMessageBoxToServer(){
        HBox messageBox = new HBox();
        messageBox.setPadding(new Insets(7,7,7,7));
        messageBox.setAlignment(Pos.CENTER_RIGHT);
        Text txtMessage = new Text(tfMessageToSend.getText());
        TextFlow txtfMessage = new TextFlow(txtMessage);
        messageBox.getChildren().add(txtfMessage);
        txtfMessage.setStyle("-fx-background-color: #53d769;"  +
                "-fx-background-radius: 25px");
        txtfMessage.setPadding(new Insets(7,7,7,7));
        return messageBox;
    }
    public static void addMessageToChatBox(HBox message, VBox vbox){
        Platform.runLater(()->{
            vbox.getChildren().add(message);
        });
    }
    public void setStage(Stage stage){
        this.stage = stage;
        this.stage.setOnCloseRequest(e->{
            this.model.sendDataToServer(this.userName + " has left the chat");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            ClientModel.terminateConnection(this.model);
            Platform.exit();
            System.exit(0);
        });
    }
}
