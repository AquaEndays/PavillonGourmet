package jar.user;

import jar.App;
import jar.model.client;
import jar.model.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class ProfilUserController {
    @FXML private TextField txtNom, txtEmail;
    @FXML private Label lblSolde;
    
    private client clientConnecte;
    private UserDAO uDAO = new UserDAO();

    public void setClient(client c) {
        this.clientConnecte = c;
        txtNom.setText(c.getNom());
        txtEmail.setText(c.getEmail());
        lblSolde.setText(c.getSolde() + " €");
    }

    @FXML
    private void handleUpdate() {
        clientConnecte.setNom(txtNom.getText());
        clientConnecte.setEmail(txtEmail.getText());
        
        // On utilise ta logique de mise à jour (similaire à maj_solde mais pour le nom/email)
        uDAO.updateProfil(clientConnecte); 
        
        Alert a = new Alert(Alert.AlertType.INFORMATION, "Profil mis à jour avec succès !");
        a.show();
    }

    @FXML
    private void retour() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/jar/user/acceuil_user.fxml"));
        Parent root = loader.load();
        AccueilUserController controller = loader.getController();
        controller.setClient(clientConnecte);
        App.getStage().getScene().setRoot(root);
    }
}