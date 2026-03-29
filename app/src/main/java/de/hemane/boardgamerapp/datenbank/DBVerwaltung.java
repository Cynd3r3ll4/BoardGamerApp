package de.hemane.boardgamerapp.datenbank;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBVerwaltung extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "boardgamerapp.db"; //Name der DB
    private static final int DATABASE_VERSION = 4; // Version der DB--> erhöhen bei Änderungen an Tabellen/Datensätzen
    private long spieler1IdSave;
    private long spieler2IdSave;
    private long spieler3IdSave;
    private long spieler4IdSave;
    private long spieler5IdSave;
    private long spieler6IdSave;

    public DBVerwaltung(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) { // erstellt Tabellen + befüllt mit Initialdaten
        erstelleTabellen(db);
        erstelleAlleInitialdaten(db);
    }

    private void erstelleTabellen(SQLiteDatabase db){
        db.execSQL( // Tabelle Spieler
                "CREATE TABLE Spieler (" +
                        "id	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                        "name TEXT NOT NULL);"
        );

        db.execSQL( // Tabelle Termin
                "CREATE TABLE Termin (" +
                        "id	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                        "datum TEXT NOT NULL," +
                        "gastgeberId INTEGER NOT NULL," +
                        "FOREIGN KEY(gastgeberId) REFERENCES Spieler(id));"
        );

        db.execSQL( // Tabelle Spiel
                "CREATE TABLE Spiel (" +
                        "id	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                        "name TEXT NOT NULL," +
                        "terminId INTEGER NOT NULL," +
                        "FOREIGN KEY(terminId) REFERENCES Termin(id));"
        );

        db.execSQL( // Tabelle Teilnahme
                "CREATE TABLE Teilnahme (" +
                        "id	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                        "spielerId INTEGER NOT NULL," +
                        "terminId INTEGER NOT NULL," +
                        "teilnahme INTEGER NOT NULL," +
                        "FOREIGN KEY(spielerId) REFERENCES Spieler(id)," +
                        "FOREIGN KEY(terminId) REFERENCES Termin(id));"
        );

        db.execSQL( // Tabelle Abstimmung
                "CREATE TABLE Abstimmung (" +
                        "id	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                        "spielerId INTEGER NOT NULL," +
                        "spielId INTEGER NOT NULL," +
                        "FOREIGN KEY(spielerId) REFERENCES Spieler(id)," +
                        "FOREIGN KEY(spielId) REFERENCES Spiel(id));"
        );

        db.execSQL( // Tabelle Chatnachrichten
                "CREATE TABLE Chatnachricht (" +
                        "id	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                        "spielerId INTEGER NOT NULL," +
                        "text TEXT NOT NULL," +
                        "zeitpunkt TEXT NOT NULL," +
                        "FOREIGN KEY(spielerId) REFERENCES Spieler(id));"
        );

        db.execSQL( // Tabelle Bewertungen
                "CREATE TABLE Bewertung (" +
                        "id	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                        "terminId INTEGER NOT NULL," +
                        "spielerId INTEGER NOT NULL," +
                        "gastgeberSterne INTEGER NOT NULL," +
                        "gastgeberKommentar TEXT," +
                        "essenSterne INTEGER NOT NULL," +
                        "essenKommentar TEXT," +
                        "allgemeinSterne INTEGER NOT NULL," +
                        "allgemeinKommentar TEXT," +
                        "FOREIGN KEY(terminId) REFERENCES Termin(id)," +
                        "FOREIGN KEY(spielerId) REFERENCES Spieler(id));"
        );
    }

    private void erstelleAlleInitialdaten(SQLiteDatabase db) {
        insertSpieler(db);
        insertTermin(db);
    }

    private void insertSpieler(SQLiteDatabase db){
        ContentValues values = new ContentValues();

        values.put("name", "Tarlyne"); //Spieler1 erstellen
        long spieler1Id =db.insert("Spieler", null, values);

        values.clear();
        values.put("name", "Cynd3r3ll4"); //Spieler2 erstellen
        long spieler2Id =db.insert("Spieler", null, values);

        values.clear();
        values.put("name", "Garlyne"); //Spieler3 erstellen
        long spieler3Id =db.insert("Spieler", null, values);

        values.clear();
        values.put("name", "LordDiaper"); //Spieler4 erstellen
        long spieler4Id =db.insert("Spieler", null, values);

        values.clear();
        values.put("name", "Starlyne"); //Spieler5 erstellen
        long spieler5Id =db.insert("Spieler", null, values);

        values.clear();
        values.put("name", "Thorabur"); //Spieler6 erstellen
        long spieler6Id =db.insert("Spieler", null, values);

        spieler1IdSave = spieler1Id;
        spieler2IdSave = spieler2Id;
        spieler3IdSave = spieler3Id;
        spieler4IdSave = spieler4Id;
        spieler5IdSave = spieler5Id;
        spieler6IdSave = spieler6Id;
    }

    private void insertTermin(SQLiteDatabase db){
        ContentValues values = new ContentValues();

        values.put("datum", "01.01.2026, 18:00 Uhr");
        values.put("gastgeberId", spieler1IdSave);
        db.insert("Termin", null, values);

        values.clear();
        values.put("datum", "05.02.2026, 18:00 Uhr");
        values.put("gastgeberId", spieler2IdSave);
        db.insert("Termin", null, values);

        values.clear();
        values.put("datum", "05.03.2026, 18:00 Uhr");
        values.put("gastgeberId", spieler3IdSave);
        db.insert("Termin", null, values);

        values.clear();
        values.put("datum", "02.04.2026, 18:00 Uhr");
        values.put("gastgeberId", spieler4IdSave);
        db.insert("Termin", null, values);

        values.clear();
        values.put("datum", "07.05.2026, 18:00 Uhr");
        values.put("gastgeberId", spieler5IdSave);
        db.insert("Termin", null, values);

        values.clear();
        values.put("datum", "04.06.2026, 18:00 Uhr");
        values.put("gastgeberId", spieler6IdSave);
        db.insert("Termin", null, values);

        values.clear();
        values.put("datum", "02.07.2026, 18:00 Uhr");
        values.put("gastgeberId", spieler1IdSave);
        db.insert("Termin", null, values);

        values.clear();
        values.put("datum", "06.08.2026, 18:00 Uhr");
        values.put("gastgeberId", spieler2IdSave);
        db.insert("Termin", null, values);

        values.clear();
        values.put("datum", "03.09.2026, 18:00 Uhr");
        values.put("gastgeberId", spieler3IdSave);
        db.insert("Termin", null, values);

        values.clear();
        values.put("datum", "01.10.2026, 18:00 Uhr");
        values.put("gastgeberId", spieler4IdSave);
        db.insert("Termin", null, values);

        values.clear();
        values.put("datum", "05.11.2026, 18:00 Uhr");
        values.put("gastgeberId", spieler5IdSave);
        db.insert("Termin", null, values);

        values.clear();
        values.put("datum", "03.12.2026, 18:00 Uhr");
        values.put("gastgeberId", spieler6IdSave);
        db.insert("Termin", null, values);

        values.clear();
        values.put("datum", "31.03.2026, 20:00 Uhr");
        values.put("gastgeberId", spieler2IdSave);
        db.insert("Termin", null, values);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { // Tabellenänderungen übernehmen
        db.execSQL("DROP TABLE IF EXISTS Bewertung");
        db.execSQL("DROP TABLE IF EXISTS Chatnachricht");
        db.execSQL("DROP TABLE IF EXISTS Abstimmung");
        db.execSQL("DROP TABLE IF EXISTS Teilnahme");
        db.execSQL("DROP TABLE IF EXISTS Spiel");
        db.execSQL("DROP TABLE IF EXISTS Termin");
        db.execSQL("DROP TABLE IF EXISTS Spieler");

        onCreate(db);
    }
}
