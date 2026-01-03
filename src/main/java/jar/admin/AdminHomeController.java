package jar.admin;

import jar.App;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import jar.model.UserDAO;
import jar.model.TableDAO;
import jar.model.ReservationDAO;
import jar.model.client;
import jar.user.AccueilUserController;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class AdminHomeController {
    @FXML private Label lblTotalClients, lblTablesLibres, lblTotalReservations;
    
    private UserDAO uDAO = new UserDAO();
    private TableDAO tDAO = new TableDAO();
    private ReservationDAO rDAO = new ReservationDAO();
    private client clientConnecte;
    public void setClient(client c){
        this.clientConnecte = c;
    }

    @FXML
    public void initialize() {
        // Ces méthodes supposent que tu les ajoutes dans tes DAO (ex: size() des listes)
        lblTotalClients.setText(String.valueOf(uDAO.getTousClients().size()));
        lblTablesLibres.setText(String.valueOf(tDAO.getToutesTables().stream().filter(t -> t.getStatut().toString().equals("LIBRE")).count()));
        // Pour les réservations, on ajoutera getAllReservations plus tard
        lblTotalReservations.setText(String.valueOf(rDAO.getAllReservation().size())); 
    }
    
    @FXML
    private void switchToUserMode() {
      try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/jar/user/acceuil_user.fxml"));
        Parent root = loader.load();

        // Récupérer le contrôleur de l'accueil pour lui repasser le client
        AccueilUserController controller = loader.getController();
        controller.setClient(this.clientConnecte); // Très important !

        App.getStage().getScene().setRoot(root);
    } catch (IOException e) {
        e.printStackTrace();
    }
    }
}