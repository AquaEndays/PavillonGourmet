package jar.admin;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import jar.model.UserDAO;
import jar.model.client;
import java.util.List;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class AdminClientsController {
    @FXML private VBox vboxAdmins;
    @FXML private VBox vboxUsers;
    private UserDAO userDAO = new UserDAO();

    @FXML
    public void initialize() {
        refreshLists();
    }

    private void refreshLists() {
        vboxAdmins.getChildren().clear();
        vboxUsers.getChildren().clear();
        
     
        List<client> tousLesClients = userDAO.getTousClients(); 

        for (client c : tousLesClients) {
            VBox card = createClientCard(c);
            if ("admin".equals(c.getRole())) {
                vboxAdmins.getChildren().add(card);
            } else {
                vboxUsers.getChildren().add(card);
            }
        }
    }

    private VBox createClientCard(client c) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: #111111; -fx-padding: 20; -fx-background-radius: 15; -fx-border-color: #333333; -fx-border-radius: 15;");
        
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);
        
        VBox info = new VBox(5);
        Label name = new Label(c.getNom().toUpperCase());
        name.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16;");
        Label email = new Label(c.getEmail() + " | Solde: " + c.getSolde() + "€");
        email.setStyle("-fx-text-fill: #888888;");
        info.getChildren().addAll(name, email);
        
        // Bouton d'action selon le rôle
        Button btnAction = new Button();
        Button btn2 = new Button();
        btn2.setText("SUPPRIMER");
        btn2.setStyle("-fx-background-color: transparent; -fx-border-color: #fccd4d; -fx-text-fill: #fccd4d; -fx-border-radius: 20; -fx-cursor: hand;");
        btn2.setOnAction(e ->confirmerSuppression(c, "SUPPRIMER CLIENT", "ETES VOUS DE VOULOIR SUPPRIMER CE CLIENT ?", ()->{
            userDAO.supprimerClient(c);
        refreshLists();
        }));
        
        if ("admin".equals(c.getRole())) {
            btnAction.setText("RÉTROGRADER");
            btnAction.setStyle("-fx-background-color: transparent; -fx-border-color: #ef4444; -fx-text-fill: #ef4444; -fx-border-radius: 20; -fx-cursor: hand;");
            btnAction.setOnAction(e -> changerRole(c, "user"));
        } else {
            btnAction.setText("PROMOUVOIR");
            btnAction.setStyle("-fx-background-color: transparent; -fx-border-color: #fccd4d; -fx-text-fill: #fccd4d; -fx-border-radius: 20; -fx-cursor: hand;");
            btnAction.setOnAction(e -> changerRole(c, "admin"));
        }

        HBox.setHgrow(info, javafx.scene.layout.Priority.ALWAYS);
        header.getChildren().addAll(info, btnAction,btn2);
         
        card.getChildren().add(header);
        
        return card;
    }

    private void changerRole(client c, String nouveauRole) {

        userDAO.changerRole(c.getId(), nouveauRole);
        refreshLists();
    }
   
    
   
      private void confirmerSuppression(client c, String titre, String msg, Runnable action) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(msg);

        alert.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.OK){
                action.run();
                
            }
        });
    }

}