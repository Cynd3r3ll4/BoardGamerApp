package de.hemane.boardgamerapp.datenbank;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import de.hemane.boardgamerapp.klassen.Teilnahme;

public class TeilnahmeDAO {
    private DBVerwaltung dbVerwaltung;

    public TeilnahmeDAO(Context context) { // für SQLite-Zugriff über DBVerwaltung (kein direkter Zugriff)
        dbVerwaltung = new DBVerwaltung(context);
    }

    public void insertTeilnahme(int spielerId, int terminId, int teilnahme) {
        SQLiteDatabase db = dbVerwaltung.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("spielerId", spielerId);
        values.put("terminId", terminId);
        values.put("teilnahme", teilnahme);

        db.insert("Teilnahme", null, values);

        db.close();
    }

    public List<Teilnahme> getAlleTeilnahmen() {
        List<Teilnahme> teilnahmeListe = new ArrayList<>();

        SQLiteDatabase db = dbVerwaltung.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM Teilnahme", null);

        if (cursor.moveToFirst()) {
            do {
                int teilnahmeId = cursor.getInt(0);
                int spielerId = cursor.getInt(1);
                int terminId = cursor.getInt(2);
                int teilnahme = cursor.getInt(3);

                Teilnahme teilnahmen = new Teilnahme(teilnahmeId, spielerId, terminId, teilnahme);

                teilnahmeListe.add(teilnahmen);
            } while
            (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return teilnahmeListe;
    }

    public Teilnahme getTeilnahmeById(int id) {
        SQLiteDatabase db = dbVerwaltung.getReadableDatabase();

        Cursor cursor= db.rawQuery("SELECT * FROM Teilnahme WHERE id=?", new String[]{String.valueOf(id)});

        Teilnahme teilnahmen = null;

        if (cursor.moveToFirst()) {
            int teilnahmeId = cursor.getInt(0);
            int spielerId = cursor.getInt(1);
            int terminId = cursor.getInt(2);
            int teilnahme = cursor.getInt(3);

            teilnahmen = new Teilnahme(teilnahmeId, spielerId, terminId, teilnahme);
        }

        cursor.close();
        db.close();

        return teilnahmen;
    }

    public List<Teilnahme> getTeilnahmeByTerminId(int terminId) {
        SQLiteDatabase db = dbVerwaltung.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM Teilnahme WHERE terminId=?", new String[]{String.valueOf(terminId)});

        List<Teilnahme> teilnahmen = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                int teilnahmeId = cursor.getInt(0);
                int spielerId = cursor.getInt(1);
                int terminIds = cursor.getInt(2);
                int teilnahme = cursor.getInt(3);

                teilnahmen.add(new Teilnahme(teilnahmeId, spielerId, terminIds, teilnahme));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return teilnahmen;
    }

    public Teilnahme getTeilnahmeBySpielerUndTermin(int spielerId, int terminId){
        SQLiteDatabase db = dbVerwaltung.getReadableDatabase();

        Cursor cursor= db.rawQuery("SELECT * FROM Teilnahme WHERE spielerId=? AND terminId=? LIMIT 1", new String[]{String.valueOf(spielerId), String.valueOf(terminId)});

        Teilnahme teilnahmen = null;

        if (cursor.moveToFirst()) {
            int teilnahmeId = cursor.getInt(0);
            int spielerIds = cursor.getInt(1);
            int terminIds = cursor.getInt(2);
            int teilnahme = cursor.getInt(3);

            teilnahmen = new Teilnahme(teilnahmeId, spielerIds, terminIds, teilnahme);
        }

        cursor.close();
        db.close();

        return teilnahmen;
    }

    public void updateTeilnahme(int id, int spielerId, int terminId, int teilnahme) {
        SQLiteDatabase db= dbVerwaltung.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("spielerId", spielerId);
        values.put("terminId", terminId);
        values.put("teilnahme", teilnahme);

        db.update("Teilnahme", values, "id=?", new String[]{String.valueOf(id)});

        db.close();
    }

    public void updateTeilnahmeOhneId(int spielerId, int terminId, int wert) {

        SQLiteDatabase db = dbVerwaltung.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("teilnahme", wert);

        db.update("Teilnahme", values, "spielerId=? AND terminId=?", new String[]{String.valueOf(spielerId), String.valueOf(terminId)}
        );
    }

    public void deleteTeilnahme(int id) {
        SQLiteDatabase db = dbVerwaltung.getWritableDatabase();

        db.delete("Teilnahme", "id=?", new String[]{String.valueOf(id)});

        db.close();
    }
}
