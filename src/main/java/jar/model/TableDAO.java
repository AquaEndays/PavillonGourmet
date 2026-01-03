/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jar.model;
import java.sql.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author JEndays
 */
public class TableDAO {
    
// Identifiants PUBLIC (Networking sur ta capture image_69784c.png)
private final String HOST = "centerbeam.proxy.rlwy.net";
private final String PORT = "56002"; 
private final String DB_NAME = "railway";
private final String USER = "root";
private final String PASS = "yiIgkCQkFEnwoaXdwdsqMNvrfInSmtII";

// URL JDBC de combat (incluant les correctifs de sécurité pour MySQL 8)
private final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME + 
                           "?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    // READ
    public List<table> getToutesTables() {
        List<table> liste = new ArrayList<>();
        String sql = "SELECT * FROM tables_restaurant";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                liste.add(new table(
                        rs.getInt("id"),
                        rs.getInt("capacite"),
                        TableStatut.valueOf(rs.getString("statut").toUpperCase())
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste;
    }

    // CREATE
    public void ajouterTable(int capacite) {
        String sql = "INSERT INTO tables_restaurant (capacite) VALUES (?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, capacite);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // UPDATE
    public void modifierTable(int id, int capacite, String statut) {
        String sql = "UPDATE tables_restaurant SET capacite=? statut = ? WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, capacite);
            ps.setString(2, statut);
            ps.setInt(3, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE
    public void supprimerTable(int id) {
        String sql = "DELETE FROM tables_restaurant WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void setStatut(table t){
        String sql = "UPDATE tables_restaurant SET statut='OCCUPEE' WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, t.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

   public List<table> getTablesDisponiblesVisuel(LocalDate date) {
    List<table> liste = new ArrayList<>();
    // Correction de la requête : on ajoute le filtre de capacité
    String sql = "SELECT * FROM tables_restaurant " +
                 "WHERE statut = 'LIBRE'" + 
                 "AND id NOT IN (" +
                 "    SELECT id_table FROM reservations " +
                 "    WHERE date_res = ? " +
                 ")";

    try (Connection conn = getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {       
        pstmt.setDate(1, Date.valueOf(date));
    
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            liste.add(new table(
                rs.getInt("id"), 
                rs.getInt("capacite"), 
                TableStatut.valueOf(rs.getString("statut").toUpperCase())
            ));
        }
    } catch (SQLException e) { e.printStackTrace(); }
    return liste;
}
   public void setStatutLibre(int id){
        String sql = "UPDATE tables_restaurant SET statut='LIBRE' WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
   

}
