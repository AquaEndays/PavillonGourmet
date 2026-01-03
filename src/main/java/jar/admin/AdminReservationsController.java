package jar.admin;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import jar.model.ReservationDAO;
import jar.model.Reservation;
import jar.model.TableDAO;
import jar.model.UserDAO;
import jar.model.client;
import java.util.List;

public class AdminReservationsController {

    @FXML private VBox vboxReservations;
    @FXML private Label lblCompteur;

    private ReservationDAO resDAO = new ReservationDAO();
    private UserDAO uDAO = new UserDAO();
    private TableDAO tableDAO = new TableDAO(); // Pour libérer la table
    
    private client clientConnecte;
    public void setClient(client c){
        this.clientConnecte = c;
    }

    @FXML
    public void initialize() {
        chargerReservations();
    }

    private void chargerReservations() {
        vboxReservations.getChildren().clear();
        
        // ATTENTION : Ajoute cette méthode dans ton DAO (voir plus bas)
        List<Reservation> liste = resDAO.getAllReservation(); 
        
        lblCompteur.setText(liste.size() + " réservations actives");

        for (Reservation r : liste) {
            vboxReservations.getChildren().add(creerCarteAdmin(r));
        }
    }

    private VBox creerCarteAdmin(Reservation r) {
        // 1. Structure de la carte
        VBox card = new VBox(10);
        card.getStyleClass().add("card-admin-reservation");

        // 2. Ligne du haut (ID + Date)
        HBox topRow = new HBox(10);
        topRow.setAlignment(Pos.CENTER_LEFT);
        
        Label lblId = new Label("CMD #" + r.getId_res());
        lblId.getStyleClass().add("lbl-admin-id");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Label lblDate = new Label(r.getDate_res().toString() + " à " + r.getHeure_res());
        lblDate.setStyle("-fx-text-fill: white; -fx-opacity: 0.7;");
        
        topRow.getChildren().addAll(lblId, spacer, lblDate);

        // 3. Ligne du milieu (Infos Table + Client + Total)
        HBox midRow = new HBox(20);
        midRow.setAlignment(Pos.CENTER_LEFT);
        
        // On récupère les plats en texte
        String platsTexte = resDAO.getNomsPlatsString(r.getId_res());
        client client_reserv = uDAO.getClient(r.getId_client());
        
        VBox detailsBox = new VBox(5);
        Label lblTable = new Label("📍 Table n°" + r.getId_table());
        lblTable.getStyleClass().add("lbl-admin-info");
        
        Label lblClient = new Label("📍 Nom client : "+ client_reserv.getNom()+" Username : "+client_reserv.getUsername());
        lblClient.getStyleClass().add("lbl-admin-info");
        
        
        Label lblPlats = new Label(platsTexte);
        lblPlats.setStyle("-fx-text-fill: #94a3b8; -fx-font-style: italic;");
        lblPlats.setWrapText(true);
        
        detailsBox.getChildren().addAll(lblTable, lblPlats, lblClient);
        
        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);
        
        Label lblTotal = new Label(String.format("%.2f €", r.getTotal_payee()));
        lblTotal.getStyleClass().add("lbl-admin-total");
        
        midRow.getChildren().addAll(detailsBox, spacer2, lblTotal);

        // 4. Bouton d'action (Annuler)
        HBox actionRow = new HBox();
        actionRow.setAlignment(Pos.CENTER_RIGHT);
        
        Button btnAnnuler = new Button("ANNULER");
        btnAnnuler.getStyleClass().add("btn-admin-cancel");
        btnAnnuler.setOnAction(e -> {
            annulerReservationAdmin(r);
        });
        
        actionRow.getChildren().add(btnAnnuler);

        // Assemblage
        card.getChildren().addAll(topRow, new Separator(), midRow, actionRow);
        
        return card;
    }

    private void annulerReservationAdmin(Reservation r) {
        // 1. Libérer la table
        tableDAO.setStatutLibre(r.getId_table()); // Assure-toi d'avoir setStatutLibre ou setStatut dans TableDAO
        
        // 2. Rembourser le client (Optionnel, nécessite UserDAO)
        // userDAO.depot_solde(client, montant); 
        
        // 3. Supprimer la réservation
        resDAO.supprimerReservation(r.getId_res());
        
        // 4. Rafraîchir
        chargerReservations();
    }
}