package jar.admin;

import jar.model.TableDAO;
import jar.model.TableStatut;
import jar.model.table;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

public class AdminTablesController {

    @FXML private TextField txtCapacite;
    @FXML private ComboBox<String> comboStatut;
    @FXML private Button btnValider;

    @FXML private TableView<table> tabTables;
    @FXML private TableColumn<table, Integer> colId;
    @FXML private TableColumn<table, Integer> colCapacite;
    @FXML private TableColumn<table, String> colStatut;
    @FXML private TableColumn<table, HBox> colAction;

    private table tableSelectionnee = null;
    TableDAO dao = new TableDAO();

    @FXML
    public void initialize() {
        // Initialisation des colonnes
        colId.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getId()));
        colCapacite.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getCapacite()));
        colStatut.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getStatut().name()));

        colAction.setCellValueFactory(cell -> {
            table t = cell.getValue();
            Button btnEdit = new Button("✏ Modifier");
            Button btnDelete = new Button("🗑 Supprimer");

            btnEdit.getStyleClass().add("btn-action-gold");
            btnDelete.getStyleClass().add("btn-action-red");

            btnEdit.setOnAction(e -> {
                tableSelectionnee = t;
                txtCapacite.setText(String.valueOf(t.getCapacite()));
                comboStatut.setValue(t.getStatut().name());
                btnValider.setText("MODIFIER");
            });

            btnDelete.setOnAction(e -> confirmerSuppression("Supprimer ?", "Table #" + t.getId(), () -> {
                dao.supprimerTable(t.getId());
                charger();
            }));

            return new SimpleObjectProperty<>(new HBox(10, btnEdit, btnDelete));
        });

        charger();
    }

    @FXML
    private void ajouterTable() {
        try {
            int cap = Integer.parseInt(txtCapacite.getText());
            String statutSaisi = comboStatut.getValue();

            if (tableSelectionnee == null) {
                dao.ajouterTable(cap); // Par défaut Libre en BDD
                afficherMessage("Succès", "Table ajoutée");
            } else {
                // Ici on utilise la méthode modifierTable du DAO
                // IL FAUT AJOUTER LE PARAMÈTRE STATUT DANS TON DAO
                dao.modifierTable(tableSelectionnee.getId(), cap, statutSaisi);
                tableSelectionnee = null;
                btnValider.setText("ENREGISTRER");
                afficherMessage("Succès", "Table modifiée");
            }

            reinitialiser();
            charger();

        } catch (Exception e) {
            afficherErreur("Erreur", "Données invalides");
        }
    }

    private void reinitialiser() {
        txtCapacite.clear();
        comboStatut.setValue(null);
        tableSelectionnee = null;
    }

    private void charger() {
        tabTables.getItems().setAll(dao.getToutesTables());
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