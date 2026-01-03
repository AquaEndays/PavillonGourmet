package jar.model;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class ReservationDAO {
    

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

    public void enregistrerReservation(int tableId, LocalDate date, String heure, int nbPers) {
        String sql = "INSERT INTO reservations (id_table, date_res, heure_res, nb_personnes) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, tableId);
            pstmt.setDate(2, Date.valueOf(date));
            pstmt.setString(3, heure);
            pstmt.setInt(4, nbPers);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
    public int AjouterReservation(client c, table t, LocalDate date, String heure, double total){
        String sql = "INSERT INTO reservations (id_client, id_table, date_res, heure_res, total_paye) VALUES(?,?,?,?,?)";
        int idGeneree = -1;
        try(Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setInt(1, c.getId());
            ps.setInt(2, t.getId());
            ps.setDate(3, Date.valueOf(date));
            ps.setString(4, heure);
            ps.setDouble(5, total);
            int lignemodifiee = ps.executeUpdate();
            if(lignemodifiee >0){
                System.out.println("RESERVATION AJOUTEE AVEC SUCCES");
            }
            try(ResultSet rs = ps.getGeneratedKeys()){
                if(rs.next()){
                    idGeneree = rs.getInt(1);
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return idGeneree;
    }
    public void ajouterDetailReservation(int id_res, plat p, int quantite){
        String sql = "INSERT INTO reservation_details (id_res, id_plat,nom_plat,quantite) VALUES(?,?,?,?)";
        try(Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, id_res);
            ps.setInt(2, p.getId());
            ps.setString(3, p.getNom());
            ps.setDouble(4, quantite);
            int lignemodifiee = ps.executeUpdate();
            if(lignemodifiee >0){
                System.out.println("DETAILS RESERVATION AJOUTEE AVEC SUCCES");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    public List<reservation_details> getReservationDetails(Reservation res){
        String sql = "SELECT nom_plat, quantite FROM reservation_details WHERE id_res = ?";
        List<reservation_details> liste = new ArrayList();
        try(Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, res.getId_res());
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                liste.add(new reservation_details(rs.getInt("id_detail"), rs.getInt("id_res"), rs.getInt("id_table"), rs.getString("nom_table"), rs.getInt("quantite")));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return liste;
        
    }
     public List<Reservation> getReservation(client c){
        String sql = "SELECT * FROM reservations WHERE id_client = ?";
        List<Reservation> liste = new ArrayList();
        
        try(Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, c.getId());
            ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    liste.add(new Reservation(rs.getInt("id_res"),rs.getInt("id_client"), rs.getInt("id_table"),rs.getDate("date_res"), rs.getString("heure_res"), rs.getDouble("total_paye") ));
                }
            
        }catch(SQLException e){
            e.printStackTrace();
        }
        return liste;
    }
     // AJOUTE CA DANS ReservationDAO.java
public String getNomsPlatsString(int idReservation) {
    StringBuilder sb = new StringBuilder();
    // Plus besoin de JOIN complexe, on prend direct dans reservation_details
    String sql = "SELECT nom_plat, quantite FROM reservation_details WHERE id_res = ?";
    
    try (Connection conn = getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        
        ps.setInt(1, idReservation);
        ResultSet rs = ps.executeQuery();
        
        while (rs.next()) {
            int qte = rs.getInt("quantite");
            String nom = rs.getString("nom_plat");
            
            // Construit la phrase : "2x Burger, 1x Soda, "
            sb.append(qte).append("x ").append(nom).append(", ");
        }
    } catch (Exception e) {
        e.printStackTrace();
        return "Détails indisponibles";
    }
    
    // On enlève la dernière virgule si la chaine n'est pas vide
    if (sb.length() > 2) {
        sb.setLength(sb.length() - 2); 
    }
    return sb.toString();
}

public void supprimerReservation(int idRes) {
    String sql = "DELETE FROM reservations WHERE id_res = ?";
    try (Connection conn = getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, idRes);
        ps.executeUpdate();
    } catch (Exception e) { e.printStackTrace(); }
}

public List<Reservation> getAllReservation(){
        String sql = "SELECT * FROM reservations";
        List<Reservation> liste = new ArrayList();
        
        try(Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)){
            ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    liste.add(new Reservation(rs.getInt("id_res"),rs.getInt("id_client"), rs.getInt("id_table"),rs.getDate("date_res"), rs.getString("heure_res"), rs.getDouble("total_paye") ));
                }
            
        }catch(SQLException e){
            e.printStackTrace();
        }
        return liste;
    }
}