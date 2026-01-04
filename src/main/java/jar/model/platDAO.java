package jar.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class platDAO {
// On demande à Java de lire les variables système de Railway
private final String HOST = System.getenv("MYSQLHOST");
private final String PORT = System.getenv("MYSQLPORT");
private final String DB_NAME = System.getenv("MYSQLDATABASE");
private final String USER = System.getenv("MYSQLUSER");
private final String PASS = System.getenv("MYSQLPASSWORD");

// L'URL se construit toute seule maintenant !
private final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME + "?allowPublicKeyRetrieval=true&useSSL=false";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public List<plat> getPlatsParCategorie(String categorie) {
        List<plat> liste = new ArrayList<>();
        String sql = "SELECT * FROM plats WHERE categorie = ? AND quantite > 0";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, categorie);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                plat p = new plat(rs.getInt("id"),rs.getString("nom"), rs.getString("description"), 
                                  rs.getInt("quantite"), rs.getDouble("prix"),rs.getString("categorie"));
                
                liste.add(p);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return liste;
    }
    public int RestePlat(plat plat_choisi){
        String sql = "SELECT quantite FROM plats WHERE id = ?";
        try (Connection conn = getConnection(); 
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
         pstmt.setInt(1, plat_choisi.getId());
         try(ResultSet rs = pstmt.executeQuery()){
             if(rs.next()){
             return rs.getInt("quantite");
         }          
             }
         
     }catch(SQLException e){
         e.printStackTrace();
     }
        return 0;
}
    
    public void RetirerPlat(plat p){
        String sql = "UPDATE plats SET quantite = ? WHERE id = ?";
        try(Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, p.getQuantite()-1);
            pstmt.setInt(2, p.getId());
            int lignesModifiees = pstmt.executeUpdate();
            if (lignesModifiees > 0) {
            System.out.println("Quantité mise à jour en BDD pour : " + p.getNom());
            // Optionnel : on met à jour l'objet Java aussi
            p.setQuantite(p.getQuantite() - 1);
        }
           System.out.println("Table modifié avec succès");
        }catch(SQLException e){
            e.printStackTrace();
            System.err.println("Erreur SQL : "+e.getMessage());
        }
        
    }
        public List<plat> getToutLesPlats(){
            List<plat> liste = new ArrayList();
        String sql = "SELECT * FROM plats";
        try (Connection conn = getConnection(); 
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
         try(ResultSet rs = pstmt.executeQuery()){
             while(rs.next()){
             liste.add(new plat(rs.getInt("id"),rs.getString("nom"),rs.getString("description"),rs.getInt("quantite"),rs.getDouble("prix"),rs.getString("categorie")));
         }
             
             }
         
         
     }catch(SQLException e){
         e.printStackTrace();
     }
        return liste;
}
         public void ajouterPlat(String nom, double prix, int qte, String categorie) {
        String sql = "INSERT INTO plats (nom, prix,quantite, categorie) VALUES (?,?,?,?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nom);
            ps.setDouble(2, prix);
            ps.setInt(3, qte);
            ps.setString(4, categorie);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
         public void supprimerPlat(plat p) {
        String sql = "DELETE FROM plats WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, p.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
         public void modifierPlat(plat p) {
        String sql = "UPDATE tables_restaurant SET nom =?, prix = ?, quantite = ?, categorie = ? WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getNom());
            ps.setDouble(2, p.getPrix());
            ps.setInt(3, p.getQuantite());
            ps.setString(4, p.getCategorie());
            ps.setInt(5, p.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

} 
