package controller;

import Server.ClientHandler;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ChatServerController{
    @FXML
    private JFXButton btnsend;

    @FXML
    private JFXTextField txtField;

    @FXML
    private JFXTextArea txtArea;

    @FXML
    private JFXButton btnAddClient;
    @FXML
    private VBox vBox;
    @FXML
    private JFXButton btnRegisterClient;
    private static VBox staticVbox;

    public void initialize(){

        staticVbox = vBox;
    }
    @FXML
    void btnAddClientOnAction(ActionEvent event) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/addClient.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Add New Client");
        stage.setAlwaysOnTop(true);
        stage.showAndWait();
    }

    @FXML
    void btnsendOnAction(ActionEvent event) throws IOException {

         String text = txtField.getText();
        if (!Objects.equals(text, "")) {

            sendMessage(text);
        } else {
            ButtonType ok = new ButtonType("Ok");
            ButtonType cancel = new ButtonType("Cancel");
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Empty message. Is it ok?", ok, cancel);
            alert.showAndWait();
            ButtonType result = alert.getResult();
            if (result.equals(ok)) {
                sendMessage(text);
            }
        }

    }

    private void sendMessage(String message) throws IOException {

        ClientHandler.broadcastMessage(message);
        HBox hBox = new HBox();
        hBox.setStyle("-fx-alignment: center-right;-fx-fill-height: true;-fx-min-height: 50;-fx-pref-width: 520;-fx-max-width: 520;-fx-padding: 10");
        Label messageLbl = new Label(message);
       hBox.getChildren().add(messageLbl);
        vBox.getChildren().add(hBox);
        txtField.clear();
    }
    @FXML
    void txtFieldOnAction(ActionEvent event) throws IOException {
        btnsendOnAction(event);
    }

    public void btnRegisterClientOnAction(ActionEvent actionEvent) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/registerForm.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Register Client");
        stage.centerOnScreen();
        stage.show();

    }

    public static void receiveMessage(String message){

        if (message.endsWith("left")){
            HBox hBox = new HBox();
            hBox.setStyle("-fx-padding: 5px 0 0 10px; ");
            hBox.setMaxWidth(300);

            Label messageLbl = new Label(message);

            hBox.getChildren().add(messageLbl);

            Platform.runLater(() -> staticVbox.getChildren().add(hBox));
        }else{
            HBox hBox = new HBox();
            hBox.setStyle("-fx-padding: 5px 0 0 10px; ");
            hBox.setMaxWidth(300);

            Label messageLbl = new Label(message);
            hBox.getChildren().add(messageLbl);

            Platform.runLater(() -> staticVbox.getChildren().add(hBox));
        }

    }
}

