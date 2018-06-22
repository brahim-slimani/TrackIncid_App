package esi.siw.map.Model;

import java.io.Serializable;

/**
 * Created by Slimani on 14/06/2018.
 */

public class Intervention implements Serializable {

    private String nom;
    private String prenom;
    private String age;
    private String sexe;
    private String incident;
    private String etat;
    private String Groupage;
    private String hopital_id;
    private String wilaya;

    public Intervention(String nom, String prenom, String age, String sexe, String incident, String etat, String groupage) {
        this.nom = nom;
        this.prenom = prenom;
        this.age = age;
        this.sexe = sexe;
        this.incident = incident;
        this.etat = etat;
        Groupage = groupage;
    }

    public Intervention(String nom, String prenom, String age, String sexe, String incident, String etat, String groupage, String hopital_id) {
        this.nom = nom;
        this.prenom = prenom;
        this.age = age;
        this.sexe = sexe;
        this.incident = incident;
        this.etat = etat;
        Groupage = groupage;
        this.hopital_id = hopital_id;
    }

    public String getHopital_id() {
        return hopital_id;
    }

    public void setHopital_id(String hopital_id) {
        this.hopital_id = hopital_id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getIncident() {
        return incident;
    }

    public void setIncident(String incident) {
        this.incident = incident;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getGroupage() {
        return Groupage;
    }

    public void setGroupage(String groupage) {
        Groupage = groupage;
    }

    public String getWilaya() {
        return wilaya;
    }

    public void setWilaya(String wilaya) {
        this.wilaya = wilaya;
    }
}
