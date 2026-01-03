package jar.user;

import jar.App;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import java.time.LocalDate;
import jar.model.ReservationDAO;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jar.model.client;
import javafx.scene.Node;

public class Dashboard_userController {
    private client clientConnecte;
    // 1. Déclarer la variable pour stocker le client

// 2. Créer la méthode publique que le LoginController appelle
public void setClientConnecte(client c) {
    this.clientConnecte = c;
    System.out.println("Dashboard : Client " + c.getNom() + " bien reçu !");
}

    // Assure-toi que les fx:id dans SceneBuilder correspondent exactement à ceux-ci
    @FXML private DatePicker datePicker;
    @FXML private Label lblNbPersonnes;
    
    // Variables de données
    private int nbPersonnes = 1;
    private final int MAX_PLACES = 12; // Capacité max du restaurant
    private String heureSelectionnee = "";
    
    // Pour gérer le style des boutons
    private Button dernierBoutonHeure;
    
    // Lien vers la base de données
    private ReservationDAO resDAO = new ReservationDAO();

    @FXML
    public void initialize() {
        // --- 1. CONFIGURATION DU CALENDRIER ---
        // Cette partie grise les dates avant "aujourd'hui"
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });
        
        // Sélectionne la date d'aujourd'hui par défaut
        datePicker.setValue(LocalDate.now());
    }

    // --- 2. GESTION DU COMPTEUR PERSONNES ---
    @FXML
    private void decrementerPersonnes() {
        if (nbPersonnes > 1) {
            nbPersonnes--;
            lblNbPersonnes.setText(String.valueOf(nbPersonnes));
        }
    }

    @FXML
    private void incrementerPersonnes() {
        if (nbPersonnes < MAX_PLACES) {
            nbPersonnes++;
            lblNbPersonnes.setText(String.valueOf(nbPersonnes));
        }
    }

    // --- 3. GESTION DES BOUTONS HEURE ---
    @FXML
    private void selectionnerHeure(ActionEvent event) {
        Button btnClique = (Button) event.getSource();

        // A. On remet le bouton précédent à son état normal (transparent)
        if (dernierBoutonHeure != null) {
            dernierBoutonHeure.setStyle("-fx-background-color: rgba(255, 255, 255, 0.05); -fx-text-fill: #fccd4d; -fx-border-color: rgba(252, 212, 77, 0.3);");
        }

        // B. On met le nouveau bouton en "Or" FORCE (!important)
        // Cela empêche le DatePicker de lui voler son style quand on clique ailleurs
        btnClique.setStyle("-fx-background-color: #fccd4d !important; -fx-text-fill: #1a1a1a !important; -fx-border-color: white !important; -fx-opacity: 1;");
        
        // C. On sauvegarde le choix
        dernierBoutonHeure = btnClique;
        heureSelectionnee = btnClique.getText();
        
        System.out.println("Heure choisie : " + heureSelectionnee);
    }

    // --- 4. VALIDATION FINALE ---
    
    @FXML
private void validerReservation() {
    LocalDate date = datePicker.getValue();
    int nbrePerson = Integer.parseInt(lblNbPersonnes.getText());

    // 1. GESTION DES ERREURS (MsgBox JavaFX)
    if (date == null || heureSelectionnee.isEmpty()) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Données manquantes");
        alert.setHeaderText("Action requise");
        alert.setContentText("Veuillez sélectionner une date et une heure avant de continuer.");
        alert.showAndWait();
        return;
    }

    // 2. TRANSITION VERS LA PAGE SUIVANTE
    try {

FXMLLoader loader = new FXMLLoader(getClass().getResource("/jar/user/reserver_table.fxml"));
Parent root = loader.load();

Reserver_tableController controller = loader.getController();
controller.initData(this.clientConnecte, date, heureSelectionnee);
controller.setPers(nbrePerson);// On transmet le client !
        
       App.getStage().getScene().setRoot(root);
        
        


    } catch (IOException e) {
        e.printStackTrace();
    }
}
    @FXML
private void retourAcceuil(ActionEvent event) {
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
