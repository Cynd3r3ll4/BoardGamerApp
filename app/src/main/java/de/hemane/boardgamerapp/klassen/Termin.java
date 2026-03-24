package de.hemane.boardgamerapp.klassen;

public class Termin {

    private int id;
    private String datum; // String, weil SQLite kein Datum kennt!
    private int gastgeberId;

    public Termin(int id, String datum, int gastgeberId) {
        this.id = id;
        this.datum = datum;
        this.gastgeberId = gastgeberId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public int getGastgeberId() {
        return gastgeberId;
    }

    public void setGastgeberId(int gastgeberId) {
        this.gastgeberId = gastgeberId;
    }
}
