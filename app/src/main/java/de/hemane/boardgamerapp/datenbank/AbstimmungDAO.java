package de.hemane.boardgamerapp.datenbank;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import de.hemane.boardgamerapp.klassen.Abstimmung;

public class AbstimmungDAO {

    private DBVerwaltung dbVerwaltung;

    public AbstimmungDAO(Context context) { // für SQLite-Zugriff über DBVerwaltung (kein direkter Zugriff)
        dbVerwaltung = new DBVerwaltung(context);
    }

    public void insertAbstimmung(int spielerId, int spielId) {
        SQLiteDatabase db = dbVerwaltung.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("spielerId", spielerId);
        values.put("spielId", spielId);

        db.insert("Abstimmung", null, values);

        db.close();
    }

    public List<Abstimmung> getAlleAbstimmungen() {
        List<Abstimmung> abstimmungListe = new ArrayList<>();

        SQLiteDatabase db = dbVerwaltung.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM Abstimmung", null);

        if (cursor.moveToFirst()) {
            do {
                int abstimmungId = cursor.getInt(0);
                int spielerId = cursor.getInt(1);
                int spielId = cursor.getInt(2);

                Abstimmung abstimmung = new Abstimmung(abstimmungId, spielerId, spielId);

                abstimmungListe.add(abstimmung);
            } while
            (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return abstimmungListe;
    }

    public Abstimmung getAbstimmungById(int id) {
        SQLiteDatabase db = dbVerwaltung.getReadableDatabase();

        Cursor cursor= db.rawQuery("SELECT * FROM Abstimmung WHERE id=?", new String[]{String.valueOf(id)});

        Abstimmung abstimmung = null;

        if (cursor.moveToFirst()) {
            int abstimmungId = cursor.getInt(0);
            int spielerId = cursor.getInt(1);
            int spielId = cursor.getInt(2);

            abstimmung = new Abstimmung(abstimmungId, spielerId, spielId);
        }

        cursor.close();
        db.close();

        return abstimmung;
    }

    public void updateAbstimmung(int id, int spielerId, int spielId) {
        SQLiteDatabase db= dbVerwaltung.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("spielerId", spielerId);
        values.put("spielId", spielId);

        db.update("Abstimmung", values, "id=?", new String[]{String.valueOf(id)});

        db.close();
    }

    public void deleteAbstimmung(int id) {
        SQLiteDatabase db = dbVerwaltung.getWritableDatabase();

        db.delete("Abstimmung", "id=?", new String[]{String.valueOf(id)});

        db.close();
    }

    public void deleteAbstimmungBySpieler(int spielerId) {

        SQLiteDatabase db = dbVerwaltung.getWritableDatabase();

        db.delete("Abstimmung", "spielerId = ?", new String[]{String.valueOf(spielerId)});
    }

    public int getStimmenAnzahl(int spielId) {

        SQLiteDatabase db = dbVerwaltung.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM Abstimmung WHERE spielId = ?",
                new String[]{String.valueOf(spielId)}
        );

        int zähler = 0;

        if (cursor.moveToFirst()) {
            zähler = cursor.getInt(0);
        }

        cursor.close();

        return zähler;
    }

    public int getSpielIdBySpieler(int spielerId) { // wenn Spieler abgestimmt hat

        SQLiteDatabase db = dbVerwaltung.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT spielId FROM Abstimmung WHERE spielerId = ?",
                new String[]{String.valueOf(spielerId)}
        );

        int spielId = -1; // bleibt -1, wenn nicht abgestimmt

        if (cursor.moveToFirst()) {
            spielId = cursor.getInt(0);
        }

        cursor.close();

        return spielId;
    }
}
