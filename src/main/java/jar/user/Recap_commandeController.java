/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package jar.user;

import jar.App;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import jar.model.client;
import jar.model.table;
import jar.model.plat;
import jar.model.platDAO;
import jar.model.ReservationDAO;
import jar.model.TableDAO;
import jar.model.UserDAO;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
/**
 * FXML Controller class
 *
 * @author JEndays
 */
public class Recap_commandeController implements Initializable {

    /**
     * Initializes the controller class.
     */
     @FXML private Button Annuler;
     @FXML private ListView listViewCommande;
     @FXML private Label lblTotalFinal;
     @FXML private Label lblNouveauSolde;
     
     private double totalCommande;
     private platDAO platDAO = new platDAO();
     private UserDAO userDAO = new UserDAO();
     private ReservationDAO resDAO = new ReservationDAO();
     private TableDAO tDAO = new TableDAO();
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
    }
    private client clientConnectee;
    private List<plat> plats_commande = new ArrayList<>();
    private table table_choisi;
    private LocalDate date_choisi;
    private String heure_choisi;
    private int nbrePers;
    
    
   
    public void setData(client c, List<plat> panier, double total, LocalDate date, String heure, int nbrepers){
        this.clientConnectee = c;
        this.plats_commande = panier;
        this.totalCommande = total;
        this.date_choisi = date;
        this.heure_choisi = heure;
        this.nbrePers = nbrepers;
        listViewCommande.getItems().addAll(panier);
        lblTotalFinal.setText(String.valueOf(total)+" £");
        double mtotal = c.getSolde()-total;
        lblNouveauSolde.setText(String.valueOf(mtotal));
    }


public void setTable(table t){
    if(t == null){
        System.err.println("ERREUR: la table recu est NULL");
        return;
    }
    this.table_choisi = t;
}

@FXML
public void confirmerPaiement(){
    // 1. DÉBIT UNIQUE : On laisse le DAO faire TOUT le calcul
    // On lui passe juste le montant à retirer. 
    // Le DAO va faire (Solde actuel - totalCommande) tout seul.
    userDAO.maj_solde(clientConnectee, totalCommande);

    // 2. ENREGISTREMENT DE LA RÉSERVATION
    int id_reservation = resDAO.AjouterReservation(clientConnectee, table_choisi, date_choisi, heure_choisi, totalCommande);

    // 3. REGROUPEMENT DES PLATS (Pour éviter les doublons dans la BDD)
    Map<Integer, Integer> compteur = new HashMap<>();
    Map<Integer, plat> objetsPlat = new HashMap<>();
    
    for(plat p : plats_commande){
        int id = p.getId();
        compteur.put(id, compteur.getOrDefault(id, 0) + 1);
        objetsPlat.put(id, p);
    }

    // 4. INSERTION DES DÉTAILS
    for(Integer idPlat : compteur.keySet()){
        resDAO.ajouterDetailReservation(id_reservation, objetsPlat.get(idPlat), compteur.get(idPlat));
    }

    AfficherAlerte("RÉSERVATION RÉUSSIE", "Paiement effectué. Nouveau solde : " + clientConnectee.getSolde() + "€");
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/jar/user/acceuil_user.fxml"));
        Parent root = loader.load();

        // Récupérer le contrôleur de l'accueil pour lui repasser le client
        AccueilUserController controller = loader.getController();
        controller.setClient(this.clientConnectee); 

        App.getStage().getScene().setRoot(root);
    } catch (IOException e) {
        e.printStackTrace();
    }
    
    // ... redirection vers accueil ...
}

@FXML
public void annuler(){
    try{
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/jar/user/choix_plat.fxml"));
    Parent root = loader.load();
    ChoixPlatController controller = loader.getController();
    controller.setClient(clientConnectee, date_choisi, heure_choisi, nbrePers);
    controller.setTable(table_choisi);
    App.getStage().getScene().setRoot(root);
    }catch(IOException e){
        e.printStackTrace();
    }
}
private void AfficherAlerte(String titre, String message){
    Alert alet = new Alert(Alert.AlertType.INFORMATION);
    alet.setTitle("INFO");
    alet.setHeaderText(titre);
    alet.setContentText(message);
    alet.showAndWait();
}


}
