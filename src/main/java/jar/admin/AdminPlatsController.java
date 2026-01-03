package jar.admin;

import jar.model.platDAO;
import jar.model.plat;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.control.cell.PropertyValueFactory;

public class AdminPlatsController {

    @FXML private TextField txtNom, txtPrix, txtQte;
    @FXML private ComboBox<String> comboCat;

    @FXML private TableView<plat> tabPlats;
    @FXML private TableColumn<plat, String> colNom;
    @FXML private TableColumn<plat, Double> colPrix;
    @FXML private TableColumn<plat, Integer> colQte;
    @FXML private TableColumn<plat, HBox> colAction;

    private plat platSelectionne = null;
    platDAO dao = new platDAO();

    @FXML
    public void initialize() {

        // 🔹 LIAISON DES COLONNES (OBLIGATOIRE)
        colNom.setCellValueFactory(cell -> new SimpleObjectProperty(cell.getValue().getNom()));
        colPrix.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getPrix()));
        colQte.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getQuantite()));

        // 🔹 ACTIONS
        colAction.setCellValueFactory(cell -> {
            plat p = cell.getValue();

            Button btnEdit = new Button("✏ Modifier");
            Button btnDelete = new Button("🗑 Supprimer");

            btnEdit.getStyleClass().add("btn-action-gold");
            btnDelete.getStyleClass().add("btn-action-red");

            btnEdit.setOnAction(e -> {
                txtNom.setText(p.getNom());
                txtPrix.setText(String.valueOf(p.getPrix()));
                txtQte.setText(String.valueOf(p.getQuantite()));
                comboCat.setValue(p.getCategorie());
                platSelectionne = p;
            });

            btnDelete.setOnAction(e -> confirmerSuppression(
                "Supprimer le plat",
                "Voulez-vous vraiment supprimer : " + p.getNom(),
                () -> {
                    dao.supprimerPlat(p);
                    charger();
                }
            ));

            HBox box = new HBox(10, btnEdit, btnDelete);
            box.setStyle("-fx-alignment: CENTER;");
            return new SimpleObjectProperty<>(box);
        });

        comboCat.setItems(FXCollections.observableArrayList(
            "Entrée", "Plat", "Dessert", "Boisson"
        ));

        charger();
    }

    @FXML
    private void ajouterPlat() {
        try {
            if (platSelectionne == null) {
                dao.ajouterPlat(txtNom.getText(), Double.parseDouble(txtPrix.getText()), Integer.parseInt(txtQte.getText()), comboCat.getValue());
                afficherMessage("Succès", "Plat ajouté avec succès");
            } else {
                dao.modifierPlat(platSelectionne);
                platSelectionne = null;
                afficherMessage("Succès", "Plat modifié avec succès");
            }
            txtNom.clear(); txtPrix.clear(); txtQte.clear();
            charger();
        } catch (Exception e) {
            afficherErreur("Erreur", "Vérifie les champs");
        }
    }

    private void charger() {
        tabPlats.getItems().setAll(dao.getToutLesPlats());
    }

    // 🔔 CONFIRMATION
    private void confirmerSuppression(String titre, String msg, Runnable action) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(msg);

        alert.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.OK) action.run();
        });
    }

    // ✅ MESSAGES
    private void afficherMessage(String titre, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(titre);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.show();
    }

    private void afficherErreur(String titre, String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(titre);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.show();
    }
}
