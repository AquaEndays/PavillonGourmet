package jar.auth;

import jar.App;
import jar.model.client;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;

public class RegisterStep1Controller {
    @FXML private TextField txtNom, txtUsername, txtEmail, txtTel;

    @FXML
    private void goToStep2() {
        if(txtNom.getText().isEmpty() || txtUsername.getText().isEmpty() || txtEmail.getText().isEmpty()) {
            showAlert("Champs manquants", "Veuillez remplir les informations.");
            return;
        }

        try {
            // 1. Création de l'objet temporaire (le bagage)
            client c = new client();
            c.setNom(txtNom.getText());
            c.setUsername(txtUsername.getText());
            c.setEmail(txtEmail.getText());
            c.setTelephone(txtTel.getText());

            // 2. Chargement de l'étape 2
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/jar/auth/register_step2.fxml"));
            Parent root = loader.load();

            // 3. Transfert du bagage
            RegisterStep2Controller controller = loader.getController();
            controller.setClientPending(c); // On passe les données !

            App.getStage().getScene().setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void backToLogin() throws IOException {
        App.setRoot("auth/login");
    }
    
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }
}