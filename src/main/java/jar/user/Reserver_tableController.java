package jar.user;

import jar.App;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import jar.model.ReservationDAO;
import jar.model.TableDAO;
import jar.model.table;
import jar.model.TableStatut;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jar.model.client;
public class Reserver_tableController {

    @FXML private FlowPane flowPaneTables;
    private ReservationDAO resDAO = new ReservationDAO();
    
 

private client clientActuel; 
private LocalDate date_choisi;
private String heure_choisi;
private int nbrePers;
private TableDAO tDAO = new TableDAO();
public void initData(client c, LocalDate date, String heure) {
    this.clientActuel = c;
    this.date_choisi = date;
    this.heure_choisi = heure;
    
    System.out.println("Client récupéré dans la réservation : " + c.getNom());
    
    rafraichirAffichage(date, heure, 2);
}
public void setPers(int nbrepers){
    this.nbrePers = nbrepers;
}

    @FXML
    public void initialize() {
        
        
    }

    @FXML
    private void gererRetour(ActionEvent event) {
        try{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/jar/user/dashboard_user.fxml"));
        Parent root = loader.load();
        Dashboard_userController controller = loader.getController();
        controller.setClientConnecte(clientActuel);
        App.getStage().getScene().setRoot(root);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void rafraichirAffichage(LocalDate date, String heure, int nbPers) {
        flowPaneTables.getChildren().clear();
        
            
            List<table> disponibles = tDAO.getTablesDisponiblesVisuel(date_choisi);

            

            for (table t : disponibles) {
                VBox card = new VBox(15);
                card.getStyleClass().add("table-card");
                card.setPrefWidth(250); 
                card.setAlignment(javafx.geometry.Pos.CENTER);

                Label titre = new Label("Table n°" + t.getId());
                titre.getStyleClass().add("card-title");

                Label desc = new Label("Places : " + t.getCapacite());
                desc.getStyleClass().add("card-description");

                Button btn = new Button("Reserver");
                btn.getStyleClass().add("btn-choisir");
                btn.setOnAction(e -> passerAuxPlats(t));

                card.getChildren().addAll(titre, desc, btn);
                flowPaneTables.getChildren().add(card);
            }
        
    }

    private void passerAuxPlats(table t) {
    try {
        // 1. Charger le fichier FXML de la page des plats
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/jar/user/choix_plat.fxml"));
        Parent root = loader.load();
// On récupère le contrôleur de la page suivante
ChoixPlatController controller = loader.getController();
// On lui donne le client actuel (que tu dois avoir en variable dans cette classe aussi)
controller.setClient(this.clientActuel, date_choisi, heure_choisi, nbrePers); 
controller.setTable(t);
Stage stage = (Stage) flowPaneTables.getScene().getWindow();
stage.setScene(new Scene(root));

        stage.show();
        
        System.out.println("Navigation vers la table n°" + t.getId());
    } catch (IOException e) {
        System.err.println("Erreur : Impossible de charger choix_plat.fxml");
        e.printStackTrace();
    }
}
    
    
}
