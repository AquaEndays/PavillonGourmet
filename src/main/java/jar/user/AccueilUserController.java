package jar.user;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import jar.model.client;
import jar.App;

public class AccueilUserController implements Initializable {

    @FXML private Label lblBienvenue;
    @FXML private Label lblSolde;

    private client clientConnecte;
    /*
    private void setData(client c){
        this.clientConnecte = c;
        lblSolde.setText(String.valueOf(c.getSolde()));
        lblBienvenue.setText(String.valueOf(c.getUsername()));
    }
    */

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialisation si nécessaire
    }

    /**
     * Reçoit les données du client depuis une autre page
     */
    public void setClient(client c) {
        this.clientConnecte = c;
        if (c != null && lblBienvenue != null) {
            lblBienvenue.setText("BIENVENUE, " + c.getUsername().toUpperCase());
            lblSolde.setText(String.format("%.2f €", c.getSolde()));
        }
    }

    @FXML
    private void allerVersReservation(ActionEvent event) {
        try{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/jar/user/dashboard_user.fxml"));
        Parent root = loader.load();
        Dashboard_userController controller = loader.getController();
        controller.setClientConnecte(clientConnecte);
        App.getStage().getScene().setRoot(root);
        }catch(IOException e){
            e.printStackTrace();
        }
        
        
    }

    @FXML
    private void seDeconnecter(ActionEvent event) {
        try {
            App.setRoot("/jar/auth/login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void allerVersHistorique(ActionEvent event) {
         try{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/jar/user/mes_reservations.fxml"));
        Parent root = loader.load();
        MesReservationsController controller = loader.getController();
        controller.setClient(clientConnecte);
        App.getStage().getScene().setRoot(root);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void allerVersPortefeuille(ActionEvent event) {
        try{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/jar/user/portefeuille.fxml"));
        Parent root = loader.load();
        PortefeuilleController controller = loader.getController();
        controller.setClient(clientConnecte);
        App.getStage().getScene().setRoot(root);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    @FXML
private void ouvrirProfil(ActionEvent event) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/jar/user/profil_user.fxml"));
        Parent root = loader.load();
        
        ProfilUserController controller = loader.getController();
        controller.setClient(this.clientConnecte); // Transfert des données
        
        App.getStage().getScene().setRoot(root);
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    /**
     * Méthode utilitaire pour naviguer en passant le client
     */
    /*
    private void changerPage(ActionEvent event, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            // On récupère le contrôleur de la page suivante pour lui donner le client
            Object controller = loader.getController();
            
            
            
            // Si la page cible est le calendrier (Dashboard)
            if (controller instanceof Dashboard_userController) {
                ((Dashboard_userController) controller).setClientConnecte(this.clientConnecte);
            }

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Erreur de navigation : " + e.getMessage());
            e.printStackTrace();
        }
    }
*/
}