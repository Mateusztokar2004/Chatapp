package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** Uruchamia klienta JavaFX. */
public class Main extends Application {
    @Override public void start(Stage stage) throws Exception {
        stage.setTitle("Klient kolos 2022");
        stage.setScene(new Scene(
                FXMLLoader.load(getClass().getResource("/view.fxml"))));
        stage.show();
    }
    public static void main(String[] args) { launch(); }
}
