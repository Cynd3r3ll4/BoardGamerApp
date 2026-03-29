package de.hemane.boardgamerapp.datenbank;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import de.hemane.boardgamerapp.klassen.Spiel;

public class SpielDAO {

    private DBVerwaltung dbVerwaltung;

    public SpielDAO(Context context) { // für SQLite-Zugriff über DBVerwaltung (kein direkter Zugriff)
        dbVerwaltung = new DBVerwaltung(context);
    }

    public void insertSpiel(String name, int terminId) {
        SQLiteDatabase db = dbVerwaltung.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("terminId", terminId);

        db.insert("Spiel", null, values);

        db.close();
    }

    public List<Spiel> getAlleSpiele() {
        List<Spiel> spielListe = new ArrayList<>();

        SQLiteDatabase db = dbVerwaltung.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM Spiel", null);

        if (cursor.moveToFirst()) {
            do {
                int spielId = cursor.getInt(0);
                String name = cursor.getString(1);
                int terminId = cursor.getInt(2);

                Spiel spiel = new Spiel(spielId, name, terminId);

                spielListe.add(spiel);
            } while
            (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return spielListe;
    }

    public Spiel getSpieleById(int id) {
        SQLiteDatabase db = dbVerwaltung.getReadableDatabase();

        Cursor cursor= db.rawQuery("SELECT * FROM Spiel WHERE id=?", new String[]{String.valueOf(id)});

        Spiel spiel = null;

        if (cursor.moveToFirst()) {
            int spielId = cursor.getInt(0);
            String name = cursor.getString(1);
            int terminId = cursor.getInt(2);

            spiel = new Spiel(spielId, name, terminId);
        }

        cursor.close();
        db.close();

        return spiel;
    }

    public List<Spiel> getSpieleByTermin(int terminId) {

        List<Spiel> spieleliste = new ArrayList<>();

        SQLiteDatabase db = dbVerwaltung.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM Spiel WHERE terminId = ?",
                new String[]{String.valueOf(terminId)}
        );

        while (cursor.moveToNext()) {

            Spiel spiel = new Spiel(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getInt(2)
            );

            spieleliste.add(spiel);
        }
        cursor.close();

        return spieleliste;
    }

    public void updateSpiel(int id, String name, int terminId) {
        SQLiteDatabase db= dbVerwaltung.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("terminId", terminId);

        db.update("Spiel", values, "id=?", new String[]{String.valueOf(id)});

        db.close();
    }

    public void deleteSpiel(int id) {
        SQLiteDatabase db = dbVerwaltung.getWritableDatabase();

        db.delete("Spiel", "id=?", new String[]{String.valueOf(id)});

        db.close();
    }
}
