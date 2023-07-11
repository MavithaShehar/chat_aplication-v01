package controller;

import client.Client;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class AddClientController implements Initializable {

    @FXML
    private ImageView image;

    @FXML
    private JFXTextField txtField;

    @FXML
    private JFXButton btnJoin;
    @FXML
    private AnchorPane Pane;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(1), image);
        scaleTransition.setFromX(1.0);
        scaleTransition.setFromY(1.0);
        scaleTransition.setToX(1.5);
        scaleTransition.setToY(1.5);
        scaleTransition.play();
    }
    @FXML
    void btnJoinOnAction(ActionEvent event) throws IOException {

        if (Pattern.matches("^[a-zA-Z\\s]+", txtField.getText())) {

            Client client = new Client(txtField.getText());

            Thread thread = new Thread(client);

            thread.start();
            Stage stage = (Stage) Pane.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    void txtFieldOnAction(ActionEvent event) throws IOException {
    btnJoinOnAction(event);
    }


}
