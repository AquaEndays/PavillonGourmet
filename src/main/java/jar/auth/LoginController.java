package jar.auth;

import java.io.IOException;
import jar.model.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import jar.App;
import jar.user.Dashboard_userController;
import jar.admin.AdminDashboardController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import jar.model.client;
import javafx.scene.control.Alert; // Import indispensable pour les alertes
import javafx.stage.Stage;
import javafx.scene.Scene; // N'oublie pas cet import
import jar.user.AccueilUserController;

public class LoginController {

    @FXML private TextField txtUser;
    @FXML private PasswordField txtPass;

    private UserDAO userDAO = new UserDAO();

    @FXML
    private void handleLogin() {
        String saisieUser = txtUser.getText();
        String pass = txtPass.getText();

        if (saisieUser.isEmpty() || pass.isEmpty()) {
            afficherAlerte("Champs vides", "Veuillez entrer votre email et votre mot de passe.");
            return;
        }

        // Appel au DAO
        client c = userDAO.getClientByLogin(saisieUser, pass);
        
        String role1 = "admin";

        if (c != null) {
            try {
                System.out.println("ROLE RECUPERE :"+c.getRole());
                if(c.getRole().equals(role1)){
                // Chargement du Dashboard
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/jar/admin/admin_dashboard.fxml"));
                Parent root = loader.load();

                // Transmission du client
                AdminDashboardController controller = loader.getController();
                controller.setClient(c);
                

                // Changement de scène
                App.getStage().getScene().setRoot(root);
                }else{
                      // Chargement du Dashboard
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/jar/user/acceuil_user.fxml"));
                Parent root = loader.load();

                // Transmission du client
                AccueilUserController controller = loader.getController();
                controller.setClient(c);

                // Changement de scène
                App.getStage().getScene().setRoot(root);
                }

            } catch (IOException e) {
                e.printStackTrace();
                afficherAlerte("Erreur technique", "Impossible de charger le tableau de bord.");
            }
        } else {
            // C'est ICI que l'alerte remplace le System.out.println
            afficherAlerte("Échec de connexion", "Email ou mot de passe incorrect.\nVérifiez que vous utilisez bien l'email enregistré dans la base de données.");
        }
        
    }

    // Méthode utilitaire pour afficher les alertes proprement
    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(titre);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void creer_compte() throws IOException{
        App.setRoot("/jar/auth/register_step1");
    }
}