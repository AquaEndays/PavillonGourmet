package jar;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class App extends Application {

    // 1. On crée une variable statique pour garder la fenêtre en mémoire
    private static Stage stage; 

    @Override
    public void start(Stage s) throws IOException {
        stage = s; // 2. TRÈS IMPORTANT : On assigne le stage reçu au démarrage
        
        Scene scene = new Scene(loadFXML("auth/login"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    // 3. On crée la méthode que le LoginController appelle
    public static Stage getStage() {
        return stage;
    }

    // Le reste de tes méthodes (setRoot, loadFXML, main...)
    public static void setRoot(String fxml) throws IOException {
        stage.getScene().setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}