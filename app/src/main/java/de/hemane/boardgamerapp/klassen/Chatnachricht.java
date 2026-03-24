package de.hemane.boardgamerapp.klassen;

public class Chatnachricht {

    private int id;
    private int spielerId;

    private String text;
     private String zeitpunkt; // auch als String, weil kein Datum/Uhrzeit in SQLite

    public Chatnachricht(int id, int spielerId, String text, String zeitpunkt) {
        this.id = id;
        this.spielerId = spielerId;
        this.text = text;
        this.zeitpunkt = zeitpunkt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSpielerId() {
        return spielerId;
    }

    public void setSpielerId(int spielerId) {
        this.spielerId = spielerId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getZeitpunkt() {
        return zeitpunkt;
    }

    public void setZeitpunkt(String zeitpunkt) {
        this.zeitpunkt = zeitpunkt;
    }
}
