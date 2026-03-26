package de.hemane.boardgamerapp.datenbank;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBVerwaltung extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "boardgamerapp.db"; //Name der DB
    private static final int DATABASE_VERSION = 1; // Version der DB

    public DBVerwaltung(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) { // führt SQL aus und erstellt Tabellen
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
                        "teilnahme INTEGER NOT NULL," +
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
