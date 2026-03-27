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

    public void insertSpieler(String name) { // Spieler in DB-Tabelle hinzufügen
        SQLiteDatabase db = dbVerwaltung.getWritableDatabase(); // schreibbare DB

        ContentValues values = new ContentValues(); // um Werte zu speichern
        values.put("name", name);

        db.insert("Spieler", null, values); // Einfügen in SQLite (Tabellen- & Spaltenname)

        db.close();
    }

    public List<Spieler> getAlleSpieler() { // Liste aller Spieler aus DB holen
        List<Spieler> spielerListe = new ArrayList<>(); // Array-Liste anlegen

        SQLiteDatabase db = dbVerwaltung.getReadableDatabase(); // lesbare DB

        Cursor cursor = db.rawQuery("SELECT * FROM Spieler", null); // holt alle Elemente aus DB, um sie in Objekte zu verwandeln

        if (cursor.moveToFirst()) { // bei erster Zeile der Tabelle beginnend
            do {
                int spielerId = cursor.getInt(0); // Spieler-ID aus DB holen
                String name = cursor.getString(1); // Spieler-Name aus DB holen

                Spieler spieler = new Spieler(spielerId, name); // Objekt aus den geholten Daten erstellen

                spielerListe.add(spieler); // neues Spieler-Objekt in Liste hinzufügen
            } while
            (cursor.moveToNext()); // zur nächsten Zeile gehen
        }

        cursor.close();
        db.close();

        return spielerListe;
    }

    public Spieler getSpielerById(int id) { // einzelne Spieler über bestimmte ID raussuchen
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

    public void updateSpieler(int id, String name) { // Daten in Tabellen updaten/ ändern)
        SQLiteDatabase db= dbVerwaltung.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", name);

         db.update("Spieler", values, "id=?", new String[]{String.valueOf(id)});

         db.close();
    }

    public void deleteSpieler(int id) { // Spieler anhand der ID löschen
        SQLiteDatabase db = dbVerwaltung.getWritableDatabase();

        db.delete("Spieler", "id=?", new String[]{String.valueOf(id)});

        db.close();
    }
}
