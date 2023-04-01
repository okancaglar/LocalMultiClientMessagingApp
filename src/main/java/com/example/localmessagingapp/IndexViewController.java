package com.example.localmessagingapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class IndexViewController implements Initializable {

    @FXML private TextField tfUserName;
    @FXML
    private Button btnLogin;
    @FXML private Text txtInfo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML
    public void loginBtnOnAction(ActionEvent e){
        FXMLLoader chatView = new FXMLLoader(getClass().getResource("chat-view.fxml"));
        chatView.setController(new ChatViewController(tfUserName.getText()));
        try {
            //before setting the new scene call the stage
            Stage stage = ((Stage)((Node)e.getSource()).getScene().getWindow());
            Parent chatParent = chatView.load();

            ((Stage)((Node)e.getSource()).getScene().getWindow()).setScene(new Scene(chatParent,625,400));
            ((ChatViewController)chatView.getController()).setStage(stage);

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
