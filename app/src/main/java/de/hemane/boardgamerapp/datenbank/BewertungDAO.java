package de.hemane.boardgamerapp.datenbank;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import de.hemane.boardgamerapp.klassen.Bewertung;

public class BewertungDAO {

    private DBVerwaltung dbVerwaltung;

    public BewertungDAO(Context context) { // für SQLite-Zugriff über DBVerwaltung (kein direkter Zugriff)
        dbVerwaltung = new DBVerwaltung(context);
    }

    public void insertBewertung(int terminId, int spielerId, int gastgeberSterne, String gastgeberKommentar, int essenSterne, String essenKommentar, int allgemeinSterne, String allgemeinKommentar) {
        SQLiteDatabase db = dbVerwaltung.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("terminId", terminId);
        values.put("spielerId", spielerId);
        values.put("gastgeberSterne", gastgeberSterne);
        values.put("gastgeberKommentar", gastgeberKommentar);
        values.put("essenSterne", essenSterne);
        values.put("essenKommentar", essenKommentar);
        values.put("allgemeinSterne", allgemeinSterne);
        values.put("allgemeinKommentar", allgemeinKommentar);

        db.insert("Bewertung", null, values);

        db.close();
    }

    public List<Bewertung> getAlleBewertungen() {
        List<Bewertung> bewertungsListe = new ArrayList<>();

        SQLiteDatabase db = dbVerwaltung.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM Bewertung", null);

        if (cursor.moveToFirst()) {
            do {
                int bewertungId = cursor.getInt(0);
                int terminId = cursor.getInt(1);
                int spielerId = cursor.getInt(2);
                int gastgeberSterne = cursor.getInt(3);
                String gastgeberKommentar = cursor.getString(4);
                int essenSterne = cursor.getInt(5);
                String essenKommentar = cursor.getString(6);
                int allgemeinSterne = cursor.getInt(7);
                String allgemeinKommentar = cursor.getString(8);


                Bewertung bewertung = new Bewertung(bewertungId, terminId, spielerId, gastgeberSterne, gastgeberKommentar, essenSterne, essenKommentar, allgemeinSterne, allgemeinKommentar);

                bewertungsListe.add(bewertung);
            } while
            (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return bewertungsListe;
    }

    public Bewertung getBewertungenById(int id) {
        SQLiteDatabase db = dbVerwaltung.getReadableDatabase();

        Cursor cursor= db.rawQuery("SELECT * FROM Bewertung WHERE id=?", new String[]{String.valueOf(id)});

        Bewertung bewertungen = null;

        if (cursor.moveToFirst()) {
            int bewertungId = cursor.getInt(0);
            int terminId = cursor.getInt(1);
            int spielerId = cursor.getInt(2);
            int gastgeberSterne = cursor.getInt(3);
            String gastgeberKommentar = cursor.getString(4);
            int essenSterne = cursor.getInt(5);
            String essenKommentar = cursor.getString(6);
            int allgemeinSterne = cursor.getInt(7);
            String allgemeinKommentar = cursor.getString(8);


            bewertungen = new Bewertung(bewertungId, terminId, spielerId, gastgeberSterne, gastgeberKommentar, essenSterne, essenKommentar, allgemeinSterne, allgemeinKommentar);
        }

        cursor.close();
        db.close();

        return bewertungen;
    }

    public void updateBewertungen(int id, int terminId, int spielerId, int gastgeberSterne, String gastgeberKommentar, int essenSterne, String essenKommentar, int allgemeinSterne, String allgemeinKommentar) {
        SQLiteDatabase db= dbVerwaltung.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("terminId", terminId);
        values.put("spielerId", spielerId);
        values.put("gastgeberSterne", gastgeberSterne);
        values.put("gastgeberKommentar", gastgeberKommentar);
        values.put("essenSterne", essenSterne);
        values.put("essenKommentar", essenKommentar);
        values.put("allgemeinSterne", allgemeinSterne);
        values.put("allgemeinKommentar", allgemeinKommentar);

        db.update("Bewertung", values, "id=?", new String[]{String.valueOf(id)});

        db.close();
    }

    public void deleteBewertungen(int id) {
        SQLiteDatabase db = dbVerwaltung.getWritableDatabase();

        db.delete("Bewertung", "id=?", new String[]{String.valueOf(id)});

        db.close();
    }

    public boolean existiertBewertung(int terminId, int spielerId) {

        SQLiteDatabase db = dbVerwaltung.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM Bewertung WHERE terminId = ? AND spielerId = ?", new String[]{String.valueOf(terminId), String.valueOf(spielerId)} // zählt Bewertung
        );

        boolean existiert = false;

        if (cursor.moveToFirst()) {
            existiert = cursor.getInt(0) > 0;
        }

        cursor.close();
        db.close();

        return existiert;
    }

    public Bewertung getBewertungByTerminUndSpieler(int terminId, int spielerId) {

        SQLiteDatabase db = dbVerwaltung.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM Bewertung WHERE terminId = ? AND spielerId = ?",
                new String[]{String.valueOf(terminId), String.valueOf(spielerId)}
        );

        Bewertung bewertung = null;

        if (cursor.moveToFirst()) {
            bewertung = new Bewertung(
                    cursor.getInt(cursor.getColumnIndexOrThrow("terminId")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("spielerId")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("gastgeberSterne")),
                    cursor.getString(cursor.getColumnIndexOrThrow("gastgeberKommentar")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("essenSterne")),
                    cursor.getString(cursor.getColumnIndexOrThrow("essenKommentar")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("allgemeinSterne")),
                    cursor.getString(cursor.getColumnIndexOrThrow("allgemeinKommentar"))
            );
        }

        cursor.close();
        db.close();

        return bewertung;
    }

    public float getSchnittGastgeberBewertung(int terminId) {

        SQLiteDatabase db = dbVerwaltung.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT AVG(gastgeberSterne) FROM Bewertung WHERE terminId = ?", new String[]{String.valueOf(terminId)} // Durchschnitt der Bewertungen
        );

        float schnitt = 0;

        if (cursor.moveToFirst()) {
            schnitt = cursor.getFloat(0);
        }

        cursor.close();

        return schnitt;
    }

    public float getSchnittEssenBewertung(int terminId) {

        SQLiteDatabase db = dbVerwaltung.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT AVG(essenSterne) FROM Bewertung WHERE terminId = ?", new String[]{String.valueOf(terminId)} // Durchschnitt der Bewertungen
        );

        float schnitt = 0;

        if (cursor.moveToFirst()) {
            schnitt = cursor.getFloat(0);
        }

        cursor.close();

        return schnitt;
    }

    public float getSchnittAllgemeinBewertung(int terminId) {

        SQLiteDatabase db = dbVerwaltung.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT AVG(allgemeinSterne) FROM Bewertung WHERE terminId = ?", new String[]{String.valueOf(terminId)} // Durchschnitt der Bewertungen
        );

        float schnitt = 0;

        if (cursor.moveToFirst()) {
            schnitt = cursor.getFloat(0);
        }

        cursor.close();

        return schnitt;
    }

    public List<String> getGastgeberKommentare(int terminId) {

        SQLiteDatabase db = dbVerwaltung.getReadableDatabase();

        List<String> kommentare = new ArrayList<>();

        Cursor cursor = db.rawQuery(
                "SELECT gastgeberKommentar FROM Bewertung WHERE terminId = ?",
                new String[]{String.valueOf(terminId)}
        );

        while (cursor.moveToNext()) {
            String kommentar = cursor.getString(0);
            kommentare.add(kommentar);
        }

        cursor.close();

        return kommentare;
    }

    public List<String> getEssenKommentare(int terminId) {

        SQLiteDatabase db = dbVerwaltung.getReadableDatabase();

        List<String> kommentare = new ArrayList<>();

        Cursor cursor = db.rawQuery(
                "SELECT essenKommentar FROM Bewertung WHERE terminId = ?",
                new String[]{String.valueOf(terminId)}
        );

        while (cursor.moveToNext()) {
            String kommentar = cursor.getString(0);
            kommentare.add(kommentar);
        }

        cursor.close();

        return kommentare;
    }

    public List<String> getAllgemeinKommentare(int terminId) {

        SQLiteDatabase db = dbVerwaltung.getReadableDatabase();

        List<String> kommentare = new ArrayList<>();

        Cursor cursor = db.rawQuery(
                "SELECT allgemeinKommentar FROM Bewertung WHERE terminId = ?",
                new String[]{String.valueOf(terminId)}
        );

        while (cursor.moveToNext()) {
            String kommentar = cursor.getString(0);
            kommentare.add(kommentar);
        }

        cursor.close();

        return kommentare;
    }
}
