package jar.auth;

import jar.App;
import jar.model.UserDAO;
import jar.model.client;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;

public class RegisterStep2Controller {
    @FXML private PasswordField txtPass, txtConfirm;
    
    private client clientPending; // Les données reçues de l'étape 1
    private UserDAO dao = new UserDAO();

    // Méthode pour recevoir le bagage
    public void setClientPending(client c) {
        this.clientPending = c;
    }

    @FXML
    private void finishRegister() {
        String p1 = txtPass.getText();
        String p2 = txtConfirm.getText();

        if (p1.isEmpty() || !p1.equals(p2)) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Les mots de passe ne correspondent pas !");
            alert.show();
            return;
        }

        // On complète l'objet client avec le mot de passe
        clientPending.setPassword(p1);

        // SAUVEGARDE BDD
        if (dao.inscrireClient(clientPending)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Compte créé avec succès ! Connectez-vous.");
            alert.showAndWait();
            try {
                App.setRoot("auth/login");
            } catch (IOException e) { e.printStackTrace(); }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erreur lors de l'inscription (Email ou User déjà pris ?)");
            alert.show();
        }
    }

    @FXML
    private void backToStep1() throws IOException {
        // Retour en arrière (on perd les données de l'étape 1 ici, c'est plus simple)
        // Ou tu peux renvoyer 'clientPending' au step 1 pour pré-remplir les champs
        App.setRoot("auth/register_step1");
    }
}