package de.hemane.boardgamerapp.klassen;

public class Spiel {

    private int id;
    private String name;
    private int terminId;

    public Spiel(int id, String name, int terminId) {
        this.id = id;
        this.name = name;
        this.terminId = terminId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTerminId() {
        return terminId;
    }

    public void setTerminId(int terminId) {
        this.terminId = terminId;
    }
}
