module jar {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql; // Pour ta base de données
    requires java.base;

    // Autorise le moteur FXML à "voir" tes contrôleurs dans chaque dossier
    opens jar.auth to javafx.fxml;
    opens jar.user to javafx.fxml;
    opens jar.admin to javafx.fxml;

    // Permet à l'application de démarrer
    exports jar;
}