package de.hemane.boardgamerapp.datenbank;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import de.hemane.boardgamerapp.klassen.Spieler;

public class SpielerDAO {

    private DBVerwaltung dbVerwaltung;

    public SpielerDAO(Context context) { // für SQLite-Zugriff über DBVerwaltung (kein direkter Zugriff)
        dbVerwaltung = new DBVerwaltung(context);
    }

    public void insertSpieler(String name) {
        SQLiteDatabase db = dbVerwaltung.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", name);

        db.insert("Spieler", null, values);

        db.close();
    }

    public List<Spieler> getAlleSpieler() {
        List<Spieler> spielerListe = new ArrayList<>();

        SQLiteDatabase db = dbVerwaltung.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM Spieler", null);

        if (cursor.moveToFirst()) {
            do {
                int spielerId = cursor.getInt(0);
                String name = cursor.getString(1);

                Spieler spieler = new Spieler(spielerId, name);

                spielerListe.add(spieler);
            } while
            (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return spielerListe;
    }

    public Spieler getSpielerById(int id) {
        SQLiteDatabase db = dbVerwaltung.getReadableDatabase();

        Cursor cursor= db.rawQuery("SELECT * FROM Spieler WHERE id=?", new String[]{String.valueOf(id)});

         Spieler spieler = null;

         if (cursor.moveToFirst()) {
             int spielerId = cursor.getInt(0);
             String name = cursor.getString(1);

             spieler = new Spieler(spielerId, name);
         }

         cursor.close();
         db.close();

         return spieler;
    }

    public void updateSpieler(int id, String name) {
        SQLiteDatabase db= dbVerwaltung.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", name);

         db.update("Spieler", values, "id=?", new String[]{String.valueOf(id)});

         db.close();
    }

    public void deleteSpieler(int id) {
        SQLiteDatabase db = dbVerwaltung.getWritableDatabase();

        db.delete("Spieler", "id=?", new String[]{String.valueOf(id)});

        db.close();
    }
}
