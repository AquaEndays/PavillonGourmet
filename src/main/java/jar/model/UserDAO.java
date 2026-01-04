/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jar.model;

/**
 *
 * @author JEndays
 */





import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
// On demande à Java de lire les variables système de Railway
private final String HOST = System.getenv("MYSQLHOST");
private final String PORT = System.getenv("MYSQLPORT");
private final String DB_NAME = System.getenv("MYSQLDATABASE");
private final String USER = System.getenv("MYSQLUSER");
private final String PASS = System.getenv("MYSQLPASSWORD");

// L'URL se construit toute seule maintenant !
private final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME + "?allowPublicKeyRetrieval=true&useSSL=false";;
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }


// Dans UserDAO.java
public client getClientByLogin(String loginSaisi, String password) {
    // On cherche par email OU par username
    String sql = "SELECT * FROM client WHERE (email = ? OR username = ?) AND password = ?"; 
    
    try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setString(1, loginSaisi);
        pstmt.setString(2, loginSaisi);
        pstmt.setString(3, password);
        
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                client c = new client();
                c.setId(rs.getInt("id"));
                c.setNom(rs.getString("nom"));
                c.setSolde(rs.getDouble("solde"));
                c.setEmail(rs.getString("email"));
                c.setUsername(rs.getString("username"));
                c.setPassword(rs.getString("password"));// On remplit le nouveau champ
                c.setRole(rs.getString("role"));
                return c; 
            }
        }
    } catch (SQLException e) {
        // Alerte pour toi dans la console si le nom de colonne est mauvais
        System.err.println("Erreur SQL : " + e.getMessage());
    }
    return null; 
}


    public void maj_solde(client c, double montant_achat){
        String sql = "UPDATE client SET solde = ? WHERE id = ?";
        try(Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setDouble(1, c.getSolde()-montant_achat);
            ps.setInt(2, c.getId());
            int lignemodifie = ps.executeUpdate();
            if (lignemodifie >0){
                System.out.println("CLIENT MODIFIEE AVEC SUCCES : NOUVEAU SOLDE : "+(c.getSolde()-montant_achat));
                c.setSolde(c.getSolde()-montant_achat);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
     public void depot_solde(client c, double solde_total){
        String sql = "UPDATE client SET solde = ? WHERE id = ?";
        try(Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setDouble(1, solde_total);
            ps.setInt(2, c.getId());
            int lignemodifie = ps.executeUpdate();
            if (lignemodifie >0){
                System.out.println("CLIENT MODIFIEE AVEC SUCCES : NOUVEAU SOLDE : "+solde_total);
                c.setSolde(solde_total);
                
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
     // Dans UserDAO.java
public void updateProfil(client c) {
    String sql = "UPDATE client SET nom = ?, email = ?, username = ? WHERE id = ?";
    try (Connection conn = getConnection(); 
         PreparedStatement ps = conn.prepareStatement(sql)) {
        
        ps.setString(1, c.getNom());
        ps.setString(2, c.getEmail());
        ps.setString(3, c.getUsername());
        ps.setInt(4, c.getId());
        
        ps.executeUpdate();
        System.out.println("Profil mis à jour !");
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
public List<client> getTousClients() {
    List<client> liste = new ArrayList<>();
    String sql = "SELECT * FROM client";

    try (Connection conn = getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            client c = new client();
            c.setId(rs.getInt("id"));
            c.setNom(rs.getString("nom"));
            c.setUsername(rs.getString("username"));
            c.setPassword(rs.getString("password"));
            c.setEmail(rs.getString("email"));
            c.setSolde(rs.getDouble("solde"));
            c.setRole(rs.getString("role"));
            c.setTelephone(rs.getString("telephone"));
            liste.add(c);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return liste;
}

public void changerRole(int id, String role) {
    String sql = "UPDATE client SET role=? WHERE id=?";
    try (Connection conn = getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, role);
        ps.setInt(2, id);
        ps.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

public void supprimerClient(client c) {
    String sql = "DELETE FROM client WHERE id=?";
    try (Connection conn = getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, c.getId());
        ps.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
public boolean inscrireClient(client c) {
    String sql = "INSERT INTO client (nom, username, password, email, telephone, solde, role) VALUES (?, ?, ?, ?, ?, 0, 'user')";
    
    try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setString(1, c.getNom());
        pstmt.setString(2, c.getUsername());
        pstmt.setString(3, c.getPassword());
        pstmt.setString(4, c.getEmail());
        pstmt.setString(5, c.getTelephone());
        
        return pstmt.executeUpdate() > 0;
    } catch (SQLException e) {
        System.err.println("Erreur Inscription : " + e.getMessage());
        return false;
    }
}

public client getClient(int id) {
    // 1. On sélectionne TOUTES les colonnes nécessaires
    String sql = "SELECT * FROM client WHERE id = ?";
    client c = new client();

    try (Connection conn = getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        
        ps.setInt(1, id);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) { // Utilise 'if' au lieu de 'while' pour un seul client
                c.setId(rs.getInt("id"));
                c.setNom(rs.getString("nom"));
                c.setUsername(rs.getString("username"));
                c.setSolde(rs.getDouble("solde"));
                c.setPassword(rs.getString("password"));
                c.setEmail(rs.getString("email"));
                c.setRole(rs.getString("role"));
                c.setTelephone(rs.getString("telephone"));
                // Ajoute les autres set si nécessaire...
            } else {
                // Si pas trouvé, on met des valeurs par défaut pour éviter le "null"
                c.setNom("Inconnu");
                c.setUsername("inconnu");
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return c;
}

}


