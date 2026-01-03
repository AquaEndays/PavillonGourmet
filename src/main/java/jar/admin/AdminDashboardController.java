package jar.admin;

import jar.App;
import jar.model.client;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import java.io.IOException;

public class AdminDashboardController {

    @FXML private StackPane contentArea;
    
    private client clientConnecte;
    public void setClient(client c){
        this.clientConnecte = c;
    }

    @FXML
    public void initialize() {
        showHome(); // Charge la vue par défaut
    }

 private void load(String fxml) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/jar/admin/" + fxml));
        Node view = loader.load();

        // --- MAGIE ICI ---
        // On récupère le contrôleur de la page qu'on vient de charger
        Object controller = loader.getController();

        // Si la page chargée a besoin du client (ex: AdminHomeController)
        // On lui donne l'objet qu'on a stocké au login
        if (controller instanceof AdminHomeController) {
            ((AdminHomeController) controller).setClient(this.clientConnecte);
        } else if (controller instanceof AdminClientsController) {
             // Pareil pour les autres si besoin
        }
        // ------------------

        contentArea.getChildren().setAll(view);
        // ... tes animations ...
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    @FXML private void showHome() { load("admin_home.fxml"); }
    @FXML private void showTables() { load("admin_tables.fxml"); }
    @FXML private void showPlats() { load("admin_plats.fxml"); }
    @FXML private void showClients() { load("admin_clients.fxml"); }
    @FXML private void showReservations() { load("admin_reservations.fxml"); }

    @FXML
    private void logout() {
        try {
            App.getStage().getScene().setRoot(FXMLLoader.load(getClass().getResource("/jar/auth/login.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}