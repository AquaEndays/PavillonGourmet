/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jar.model;

/**
 *
 * @author JEndays
 */

/**
 *
 * @author JEndays
 */

public class table {
    private int id;
    private int capacite;
    private TableStatut statut;

    // Constructeur pour le DAO (quand on lit la BD)
    public table(int id, int capacite, TableStatut statut) {
        this.id = id;
        this.capacite = capacite;
        this.statut = statut;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public TableStatut getStatut() {
        return statut;
    }

    public void setStatut(TableStatut statut) {
        this.statut = statut;
    }

    public int getId() {
        return id;
    }

    
}
