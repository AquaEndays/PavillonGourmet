/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jar.model;

import java.sql.Date;
import java.time.LocalDate;

/**
 *
 * @author JEndays
 */
public class Reservation {
    private int id_res;
    private int id_client;
    private int id_table;
    private Date date_res;
    private String heure_res;
    private double total_payee;

    public Date getDate_res() {
        return date_res;
    }

    public void setDate_res(Date date_res) {
        this.date_res = date_res;
    }

    public Reservation(int id_res, int id_client, int id_table, Date date_res, String heure_res, double total_payee) {
        this.id_res = id_res;
        this.id_client = id_client;
        this.id_table = id_table;
        this.date_res = date_res;
        this.heure_res = heure_res;
        this.total_payee = total_payee;
    }

    

    

    public int getId_res() {
        return id_res;
    }

    public int getId_client() {
        return id_client;
    }

    public int getId_table() {
        return id_table;
    }

    public String getHeure_res() {
        return heure_res;
    }

    public void setHeure_res(String heure_res) {
        this.heure_res = heure_res;
    }

    

    public double getTotal_payee() {
        return total_payee;
    }

    public void setTotal_payee(double total_payee) {
        this.total_payee = total_payee;
    }
}
