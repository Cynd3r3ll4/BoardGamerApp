package de.hemane.boardgamerapp.datenbank;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import de.hemane.boardgamerapp.klassen.Chatnachricht;

public class ChatnachrichtDAO {

    private DBVerwaltung dbVerwaltung;

    public ChatnachrichtDAO(Context context) {
        dbVerwaltung = new DBVerwaltung(context);
    }

    public void insertChatnachrichten(int spielerId, String text, String zeitpunkt) {
        SQLiteDatabase db = dbVerwaltung.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("spielerId", spielerId);
        values.put("text", text);
        values.put("zeitpunkt", zeitpunkt);

        db.insert("Chatnachricht", null, values);

        db.close();
    }

    public List<Chatnachricht> getAlleChatnachrichten() {
        List<Chatnachricht> chatnachrichtenListe = new ArrayList<>();

        SQLiteDatabase db = dbVerwaltung.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM Chatnachricht", null);

        if (cursor.moveToFirst()) {
            do {
                int chatnachrichtId = cursor.getInt(0);
                int spielerId = cursor.getInt(1);
                String text = cursor.getString(2);
                String zeitpunkt = cursor.getString(3);

                Chatnachricht chatnachricht = new Chatnachricht(chatnachrichtId, spielerId, text, zeitpunkt);

                chatnachrichtenListe.add(chatnachricht);
            } while
            (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return chatnachrichtenListe;
    }

    public Chatnachricht getChatnachrichtenById(int id) {
        SQLiteDatabase db = dbVerwaltung.getReadableDatabase();

        Cursor cursor= db.rawQuery("SELECT * FROM Chatnachricht WHERE id=?", new String[]{String.valueOf(id)});

        Chatnachricht chatnachricht = null;

        if (cursor.moveToFirst()) {
            int chatnachrichtId = cursor.getInt(0);
            int spielerId = cursor.getInt(1);
            String text = cursor.getString(2);
            String zeitpunkt = cursor.getString(3);

            chatnachricht = new Chatnachricht(chatnachrichtId, spielerId, text, zeitpunkt);
        }

        cursor.close();
        db.close();

        return chatnachricht;
    }

    public void updateChatnachrichten(int id, int spielerId, String text, String zeitpunkt) {
        SQLiteDatabase db= dbVerwaltung.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("spielerId", spielerId);
        values.put("text", text);
        values.put("zeitpunkt", zeitpunkt);

        db.update("Chatnachricht", values, "id=?", new String[]{String.valueOf(id)});

        db.close();
    }

    public void deleteChatnachrichten(int id) {
        SQLiteDatabase db = dbVerwaltung.getWritableDatabase();

        db.delete("Chatnachricht", "id=?", new String[]{String.valueOf(id)});

        db.close();
    }
}
