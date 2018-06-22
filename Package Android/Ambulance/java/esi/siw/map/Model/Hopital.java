package esi.siw.map.Model;



public class Hopital {
    private int id;
    private String nom;
    private String type;
    private String service;
    private int lits;
    private String longitude;
    private String latitude;
    private String adresse;
    private String tel;

    public Hopital() {
    }

    public Hopital(int id, String nom, String type, String service, int lits, String longitude, String latitude, String adresse, String tel) {
        this.id = id;
        this.nom = nom;
        this.type = type;
        this.service = service;
        this.lits = lits;
        this.longitude = longitude;
        this.latitude = latitude;
        this.adresse = adresse;
        this.tel = tel;
    }

    public Hopital(String nom, String type, String service, int lits, String longitude, String latitude, String adresse, String tel) {
        this.nom = nom;
        this.type = type;
        this.service = service;
        this.lits = lits;
        this.longitude = longitude;
        this.latitude = latitude;
        this.adresse = adresse;
        this.tel = tel;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public int getLits() {
        return lits;
    }

    public void setLits(int lits) {
        this.lits = lits;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
