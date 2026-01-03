package jar.user;

import jar.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import jar.model.client;
import jar.model.UserDAO;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;

public class PortefeuilleController {

    @FXML private Label lblSoldePortefeuille, lblNomClient;
    @FXML private TextField txtMontant;
    private client clientConnecte;
    UserDAO dao = new UserDAO();

    public void setClient(client c) {
        this.clientConnecte = c;
        if (c != null) {
            lblSoldePortefeuille.setText(String.format("%.2f €", c.getSolde()));
            lblNomClient.setText(c.getNom().toUpperCase());
        }
    }

    @FXML
    private void effectuerDepot(ActionEvent event) {
        gererTransaction(true);
    }

    @FXML
    private void effectuerRetrait(ActionEvent event) {
        gererTransaction(false);
    }

 private void gererTransaction(boolean estDepot) {
    try {
        if (txtMontant.getText().isEmpty()) return;
        double montant = Double.parseDouble(txtMontant.getText());
        if (montant <= 0) return;

        if (estDepot) {
            // Le DAO s'occupe de faire c.setSolde() à l'intérieur !
            double nouveauSolde = clientConnecte.getSolde() + montant;
            dao.depot_solde(clientConnecte, nouveauSolde);
        } else {
            if (montant > clientConnecte.getSolde()) {
                System.out.println("Fonds insuffisants !");
                return;
            }
            // maj_solde retire déjà le montant dans ton code DAO
            dao.maj_solde(clientConnecte, montant);
        }

        // MISE À JOUR VISUELLE (Après l'opération BDD)
        lblSoldePortefeuille.setText(String.format("%.2f €", clientConnecte.getSolde()));
        txtMontant.clear();

    } catch (NumberFormatException e) {
        System.err.println("Entrez un nombre valide.");
    }
}

    @FXML
    private void retourAccueil(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/jar/user/acceuil_user.fxml"));
            Parent root = loader.load();
            AccueilUserController controller = loader.getController();
            controller.setClient(clientConnecte);
            App.getStage().getScene().setRoot(root);
        } catch (Exception e) { e.printStackTrace(); }
    }
}
