import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class AppInitializer extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/StartingPage.fxml"));

        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("Customer Support");
        stage.setScene(scene);
        stage.setX(10);
        stage.setY(10);
        stage.show();
    }
}
