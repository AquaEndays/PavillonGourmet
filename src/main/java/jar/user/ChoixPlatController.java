package jar.user;

import jar.App;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import jar.model.plat;
import jar.model.client;
import jar.model.table;
import java.util.List;
import javafx.scene.layout.VBox;
import jar.model.platDAO;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class ChoixPlatController implements Initializable {

    @FXML private Button btnRetour;
    @FXML private FlowPane flowPaneEntrees;
    @FXML private FlowPane flowPanePlats;
    @FXML private FlowPane flowPaneBoissons;
    @FXML private Label lblTotal;
    @FXML private Button btnValiderCommande;
    @FXML private Label lblSolde;
    private List<plat> plats_choisi = new ArrayList<>();
    private table table_choisi;
    private LocalDate date_choisi;
    private String heure_choisi;
    private int nbrePers;

    private client clientActuel; // Sera rempli par la page précédente
    
    public void setTable(table t){
        if(t== null){
            System.err.println("ERREUR: La table recu est null");
            return;
        }
        this.table_choisi = t;
    }

    public void setClient(client c, LocalDate date, String heure, int nbrepers) {
    if (c == null) {
        System.err.println("ERREUR : Le client reçu est NULL !");
        return;
    }
    this.date_choisi = date;
    this.heure_choisi = heure;
    this.clientActuel = c;
    this.nbrePers = nbrepers;
    if (lblSolde != null) {
        lblSolde.setText("Mon Solde : " + c.getSolde() + " €");
    }
}
       

    @FXML
    private void gererRetour(ActionEvent event) {
           try {

FXMLLoader loader = new FXMLLoader(getClass().getResource("/jar/user/reserver_table.fxml"));
Parent root = loader.load();

Reserver_tableController controller = loader.getController();
controller.initData(this.clientActuel, date_choisi, heure_choisi);
controller.setPers(0);
        
       App.getStage().getScene().setRoot(root);
        
        


    } catch (IOException e) {
        e.printStackTrace();
    }
    }

    @FXML
    private void validerCommande(ActionEvent event) {
        System.out.println("Commande validée !");
        if(totalCommande == 0){
            AfficherAlerte("ERREUR COMMANDE", "Vous ne pouvez pas reserver une table vide ?");
        }
        else if(clientActuel.getSolde()>= totalCommande){
            
            
            try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/jar/user/recap_commande.fxml"));
            Parent root = loader.load();
            Recap_commandeController controller = loader.getController();
            
            controller.setTable(table_choisi);
            controller.setData(clientActuel, plats_choisi, totalCommande, date_choisi, heure_choisi, nbrePers);
            
            App.getStage().getScene().setRoot(root);
            
            }catch(IOException e){
                e.printStackTrace();
            }
            
              
        }else{
            AfficherAlerte("ERREUR VALIDATION", "DESOLEE VOUS NE POUVEZ EFFECTUER CETTE TRANSACTION VERIFIEZ VOTRE SOLDE");
        }
        // Ici on ajoutera la redirection vers la page finale
    }
private platDAO platDAO = new platDAO();
private double totalCommande = 0.0;

@Override
public void initialize(URL url, ResourceBundle rb) {
    chargerCategorie("ENTREE", flowPaneEntrees);
    chargerCategorie("PLAT", flowPanePlats);
    chargerCategorie("BOISSON", flowPaneBoissons);
}

// Assure-toi d'avoir ces imports exacts en haut du fichier


// Dans ta méthode chargerCategorie
private void chargerCategorie(String cat, FlowPane pane) {
    // Vérifie bien que getPlatsParCategorie renvoie List<plat> et non pas juste List
    List<plat> plats = platDAO.getPlatsParCategorie(cat);
    
    if (plats != null) {
        for (plat p : plats) {
            VBox card = creerCartePlat(p);
            pane.getChildren().add(card);
        }
    }
}

private VBox creerCartePlat(plat p) {
    VBox card = new VBox(10);
    card.getStyleClass().add("plat-card"); // Utilise ton style CSS doré/noir
    card.setPrefWidth(220);
    
    Label nom = new Label(p.getNom());
    nom.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;");
    
    Label prix = new Label(p.getPrix() + " €");
    prix.setStyle("-fx-text-fill: #fccd4d; -fx-font-size: 18px;");
    
    Button btn = new Button("Ajouter");
    btn.getStyleClass().add("btn-choisir");
    btn.setOnAction(e -> ajouterAuPanier(p)); // On gérera le panier après
    
    card.getChildren().addAll(nom, prix, btn);
    return card;
}
private void ajouterAuPanier(plat p) {
    
    int plat_restant = platDAO.RestePlat(p);
    
    if(plat_restant>0){
    
    totalCommande += p.getPrix();
    // Met à jour le label en bas
    lblTotal.setText(String.format("Total : %.2f €", totalCommande));
    System.out.println("Ajouté : " + p.getNom());
    platDAO.RetirerPlat(p);
    plats_choisi.add(p);
    
}else{
        AfficherAlerte("PLAT FINI", "LE PLAT QUE VOUS ESSAYEZ DE COMMANDER EST FINI");
    }
}
private void AfficherAlerte(String titre, String message){
    Alert alet = new Alert(Alert.AlertType.INFORMATION);
    alet.setTitle("INFO");
    alet.setHeaderText(titre);
    alet.setContentText(message);
    alet.showAndWait();
}
/*
private void AlerteQuestion(String titre, String message){
        Alert alet = new Alert(Alert.AlertType.CONFIRMATION);
        alet.setTitle("VERIFICATION");
        alet.setHeaderText(titre);
        alet.setContentText(message);
        Optional<ButtonType> result = alet.showAndWait();
        if((result.isPresent() && result.get()= ButtonType.OK)){
            
        } else {
        }
}
*/
}