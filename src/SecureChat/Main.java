package SecureChat;

import SecureChat.network.Receiver;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("App.fxml"));
        stage.setTitle("Secure chat");
        stage.setScene(new Scene(root));
        stage.setOnCloseRequest(event -> {
            if (Receiver.instantiated) Receiver.stop();
            stage.close();
        });
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
