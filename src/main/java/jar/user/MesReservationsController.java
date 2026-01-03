package jar.user;

import jar.App;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import jar.model.Reservation;
import jar.model.client;
import jar.model.ReservationDAO;
import java.io.IOException;

public class MesReservationsController implements Initializable {

    @FXML private VBox vboxHistorique;
    
    private client clientConnecte;
    private ReservationDAO resDAO = new ReservationDAO();

    /**
     * C'est ici que tout commence. Quand on reçoit le client, on charge l'historique.
     */
    public void setClient(client c) {
        this.clientConnecte = c;
        if (c != null) {
            afficherHistorique();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Rien ici, on attend le setClient
    }

    private void afficherHistorique() {
        vboxHistorique.getChildren().clear();
        vboxHistorique.setSpacing(20); // Espace entre les cartes
        
        // Récupération des données
        List<Reservation> list = resDAO.getReservation(clientConnecte); // Assure-toi que cette méthode existe dans ton DAO

        if (list.isEmpty()) {
            Label vide = new Label("Aucune réservation trouvée.");
            vide.setStyle("-fx-text-fill: white; -fx-font-size: 16;");
            vboxHistorique.getChildren().add(vide);
            return;
        }

        for (Reservation res : list) {
            
            // 1. LA CARTE PRINCIPALE
            HBox card = new HBox(20);
            card.getStyleClass().add("card-luxe");
            card.setAlignment(Pos.CENTER_LEFT);
            card.setPrefHeight(100);

            // 2. BLOC DATE (Gauche)
            VBox boxDate = new VBox(5);
            boxDate.setAlignment(Pos.CENTER);
            boxDate.getStyleClass().add("box-date");
            boxDate.setMinWidth(100);
            
            // J'utilise tes getters (getDate_res)
            Label lblDate = new Label(res.getDate_res().toString());
            lblDate.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
            Label lblHeure = new Label(res.getHeure_res());
            lblHeure.setStyle("-fx-text-fill: #888; -fx-font-size: 12;");
            
            boxDate.getChildren().addAll(lblDate, lblHeure);

            // 3. BLOC INFO (Centre)
            VBox boxInfo = new VBox(5);
            HBox.setHgrow(boxInfo, Priority.ALWAYS);
            
            Label lblTable = new Label("Table N°" + res.getId_table());
            lblTable.setStyle("-fx-text-fill: white; -fx-font-size: 18; -fx-font-weight: bold;");
            
            // Appel de la méthode DAO qu'on vient de créer
            String nomsPlats = resDAO.getNomsPlatsString(res.getId_res()); // J'ai supposé getId_res(), vérifie si c'est getId()
            Label lblPlats = new Label(nomsPlats);
            lblPlats.getStyleClass().add("lbl-plats");
            lblPlats.setWrapText(true);
            
            boxInfo.getChildren().addAll(lblTable, lblPlats);

            // 4. BLOC PRIX & ACTION (Droite)
            VBox boxAction = new VBox(10);
            boxAction.setAlignment(Pos.CENTER_RIGHT);
            
            Label lblPrix = new Label(String.format("%.2f €", res.getTotal_payee()));
            lblPrix.getStyleClass().add("lbl-prix-gold");
            
            Button btnDelete = new Button("🗑 Annuler");
            btnDelete.getStyleClass().add("btn-trash");
            
            btnDelete.setOnAction(e -> 
                // Appel méthode DAO suppression
                /*
                resDAO.supprimerReservation(res.getId_res()); // Vérifie getId() ou getId_res()
                afficherHistorique(); // Rafraichir
*/
                confirmerSuppression("ANNULER RESERVATION", "ETES VOUS DE VOULOIR ANNULER CETTE RESERVATION ? VOUS NE SEREZ PAS REMBOURSEE", ()->{
                    resDAO.supprimerReservation(res.getId_res()); // Vérifie getId() ou getId_res()
                afficherHistorique();
                })
            );
            
            boxAction.getChildren().addAll(lblPrix, btnDelete);

            // ASSEMBLAGE
            card.getChildren().addAll(boxDate, boxInfo, boxAction);
            vboxHistorique.getChildren().add(card);
        }
    }
    
    @FXML
    private void retourAccueil(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/jar/user/acceuil_user.fxml"));
            Parent root = loader.load();
            
            AccueilUserController controller = loader.getController();
            controller.setClient(this.clientConnecte); // On n'oublie pas les bagages !
            
           App.getStage().getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void confirmerSuppression(String titre, String msg, Runnable action) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(msg);

        alert.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.OK) action.run();
        });
    }
    
}
