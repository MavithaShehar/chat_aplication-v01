package controller;

import Server.Server;
import com.jfoenix.controls.JFXButton;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StartingPageController implements Initializable {

    @FXML
    private ImageView image;
    @FXML
    private JFXButton btnGetStarted;
    @FXML
    private AnchorPane Pane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

       setAnimation();
    }

    void setAnimation() {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(1), image);
        scaleTransition.setFromX(1.0);
        scaleTransition.setFromY(1.0);
        scaleTransition.setToX(1.5);
        scaleTransition.setToY(1.5);
        scaleTransition.play();

        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(1), btnGetStarted);
        translateTransition.setFromY(100);
        translateTransition.setToY(0);
        translateTransition.play();
    }

    public void btnGetStartedOnAction(ActionEvent actionEvent) throws IOException {
        initServer();

        Pane.getChildren().clear();
        Stage stage = (Stage) Pane.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/ChatServer.fxml"))));
        stage.show();

         }

    void initServer() throws IOException {
      Server server = Server.getServerSocket();

        Thread thread = new Thread(server);
        thread.start();
    }
}
