/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jar.model;

/**
 *
 * @author JEndays
 */
public class reservation_details {
    private int id_detail;
    private int id_res;
    private int id_table;
    private String nom_table;
    private int quantite;

    public String getNom_table() {
        return nom_table;
    }

    public void setNom_table(String nom_table) {
        this.nom_table = nom_table;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public int getId_detail() {
        return id_detail;
    }

    public int getId_res() {
        return id_res;
    }

    public int getId_table() {
        return id_table;
    }

    public reservation_details(int id_detail, int id_res, int id_table, String nom_table, int quantite) {
        this.id_detail = id_detail;
        this.id_res = id_res;
        this.id_table = id_table;
        this.nom_table = nom_table;
        this.quantite = quantite;
    }
    
}
