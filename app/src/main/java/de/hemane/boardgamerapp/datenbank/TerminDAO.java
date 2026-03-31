package de.hemane.boardgamerapp.datenbank;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import de.hemane.boardgamerapp.helfer.DatumHelfer;
import de.hemane.boardgamerapp.klassen.Termin;

public class TerminDAO {
    private DBVerwaltung dbVerwaltung;
    public TerminDAO(Context context) { // für DB-Zugriff über DBVerwaltung
        dbVerwaltung = new DBVerwaltung(context);
    }

    public void insertTermin(String datum, int gastgeberId) {
        SQLiteDatabase db = dbVerwaltung.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("datum", datum);
        values.put("gastgeberId", gastgeberId);

        db.insert("Termin", null, values);

        db.close();
    }

    public List<Termin> getAlleTermine() {
        List<Termin> terminListe = new ArrayList<>();

        SQLiteDatabase db = dbVerwaltung.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM Termin", null);

        if (cursor.moveToFirst()) {
            do {
                int terminId = cursor.getInt(0);
                String datum = cursor.getString(1);
                int gastgeberId = cursor.getInt(2);

                Termin termin = new Termin(terminId, datum, gastgeberId);

                terminListe.add(termin);
            } while
            (cursor.moveToNext());
        }

        cursor.close(); // schließen, sonst Gefahr von Memory Leaks
        db.close();

        return terminListe;
    }

    public Termin getTerminById(int id) {
        SQLiteDatabase db = dbVerwaltung.getReadableDatabase();

        Cursor cursor= db.rawQuery("SELECT * FROM Termin WHERE id=?", new String[]{String.valueOf(id)});

        Termin termin = null;

        if (cursor.moveToFirst()) {
            int terminId = cursor.getInt(0);
            String datum = cursor.getString(1);
            int gastgeberId = cursor.getInt(2);

            termin = new Termin(terminId, datum, gastgeberId);
        }

        cursor.close();
        db.close();

        return termin;
    }

    public Termin getLetzterTerminVonGastgeber(int spielerId) {

        List<Termin> alleTermine = getAlleTermine();

        Termin letzterTermin = null;
        LocalDateTime letztesDatum = null;
        LocalDateTime jetzt = LocalDateTime.now();

        for (Termin t : alleTermine) {

            if (t.getGastgeberId() == spielerId) {

                LocalDateTime datum = DatumHelfer.parseDatum(t.getDatum());

                if (datum != null && datum.isBefore(jetzt)) { // nur vergangene Termine berücksichtigen
                    if (letztesDatum == null || datum.isAfter(letztesDatum)) { // jüngsten vergangenen Termin finden
                        letzterTermin = t;
                        letztesDatum = datum;
                    }
                }
            }
        }
        return letzterTermin;
    }

    public void updateTermin(int id, String datum, int gastgeberId) {
        SQLiteDatabase db= dbVerwaltung.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("datum", datum);
        values.put("gastgeberId", gastgeberId);

        db.update("Termin", values, "id=?", new String[]{String.valueOf(id)});

        db.close();
    }

    public void deleteTermin(int id) {
        SQLiteDatabase db = dbVerwaltung.getWritableDatabase();

        db.delete("Termin", "id=?", new String[]{String.valueOf(id)});

        db.close();
    }
}
