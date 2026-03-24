package de.hemane.boardgamerapp.klassen;

public class Abstimmung {

    private int id;
    private int spielerId;
    private int spielId;

    public Abstimmung(int id, int spielerId, int spielId) {
        this.id = id;
        this.spielerId = spielerId;
        this.spielId = spielId;
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

    public int getSpielId() {
        return spielId;
    }

    public void setSpielId(int spielId) {
        this.spielId = spielId;
    }
}
