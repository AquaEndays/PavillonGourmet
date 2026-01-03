/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jar.model;

/**
 *
 * @author JEndays
 */
public class plat {
    private int id;

   
    private String nom;
    private String description;
    private int quantite;
    private double prix;
    private String categorie;
    private PlatStatut statut;

    public plat(int id, String nom, String description, int quantite, double prix, String categorie) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.quantite = quantite;
        this.prix = prix;
        this.categorie = categorie;
        if (quantite > 0){
            this.statut = PlatStatut.DISPONIBLE;
        }else{
            this.statut = PlatStatut.FINI;
        }
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }
    

    public PlatStatut getStatut() {
        return statut;
    }

    public void setStatut(PlatStatut statut) {
        this.statut = statut;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }
    
    @Override
public String toString() {
    return this.nom + " - " + this.prix + " €";
}
    

    
    
}
