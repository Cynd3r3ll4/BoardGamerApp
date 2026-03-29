package de.hemane.boardgamerapp.klassen;

public class Bewertung {

    private int id;
    private int terminId;
    private int spielerId;
    private int gastgeberSterne;
    private String gastgeberKommentar;
    private int essenSterne;
    private String essenKommentar;
    private int allgemeinSterne;
    private String allgemeinKommentar;

    public Bewertung(int id, int terminId, int spielerId, int gastgeberSterne, String gastgeberKommentar, int essenSterne, String essenKommentar, int allgemeinSterne, String allgemeinKommentar) {
        this.id = id;
        this.terminId = terminId;
        this.spielerId = spielerId;
        this.gastgeberSterne = gastgeberSterne;
        this.gastgeberKommentar = gastgeberKommentar;
        this.essenSterne = essenSterne;
        this.essenKommentar = essenKommentar;
        this.allgemeinSterne = allgemeinSterne;
        this.allgemeinKommentar = allgemeinKommentar;
    }

    public Bewertung(int terminId, int spielerId, int gastgeberSterne, String gastgeberKommentar, int essenSterne, String essenKommentar, int allgemeinSterne, String allgemeinKommentar) {
        this.terminId = terminId;
        this.spielerId = spielerId;
        this.gastgeberSterne = gastgeberSterne;
        this.gastgeberKommentar = gastgeberKommentar;
        this.essenSterne = essenSterne;
        this.essenKommentar = essenKommentar;
        this.allgemeinSterne = allgemeinSterne;
        this.allgemeinKommentar = allgemeinKommentar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTerminId() {
        return terminId;
    }

    public void setTerminId(int terminId) {
        this.terminId = terminId;
    }

    public int getSpielerId() {
        return spielerId;
    }

    public void setSpielerId(int spielerId) {
        this.spielerId = spielerId;
    }

    public int getGastgeberSterne() {
        return gastgeberSterne;
    }

    public void setGastgeberSterne(int gastgeberSterne) {
        this.gastgeberSterne = gastgeberSterne;
    }

    public String getGastgeberKommentar() {
        return gastgeberKommentar;
    }

    public void setGastgeberKommentar(String gastgeberKommentar) {
        this.gastgeberKommentar = gastgeberKommentar;
    }

    public int getEssenSterne() {
        return essenSterne;
    }

    public void setEssenSterne(int essenSterne) {
        this.essenSterne = essenSterne;
    }

    public String getEssenKommentar() {
        return essenKommentar;
    }

    public void setEssenKommentar(String essenKommentar) {
        this.essenKommentar = essenKommentar;
    }

    public int getAllgemeinSterne() {
        return allgemeinSterne;
    }

    public void setAllgemeinSterne(int allgemeinSterne) {
        this.allgemeinSterne = allgemeinSterne;
    }

    public String getAllgemeinKommentar() {
        return allgemeinKommentar;
    }

    public void setAllgemeinKommentar(String allgemeinKommentar) {
        this.allgemeinKommentar = allgemeinKommentar;
    }
}
