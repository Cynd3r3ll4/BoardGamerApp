package de.hemane.boardgamerapp.klassen;

public class Teilnahme {

    private int id;
    private int spielerId;
    private int terminId;
    private int Teilnahme;

    public Teilnahme(int id, int spielerId, int terminId, int teilnahme) {
        this.id = id;
        this.spielerId = spielerId;
        this.terminId = terminId;
        Teilnahme = teilnahme;
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

    public int getTerminId() {
        return terminId;
    }

    public void setTerminId(int terminId) {
        this.terminId = terminId;
    }

    public int getTeilnahme() {
        return Teilnahme;
    }

    public void setTeilnahme(int teilnahme) {
        Teilnahme = teilnahme;
    }
}
