package de.hemane.boardgamerapp.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import de.hemane.boardgamerapp.MainActivity;
import de.hemane.boardgamerapp.R;
import de.hemane.boardgamerapp.controller.APIServer;
import de.hemane.boardgamerapp.klassen.Spieler;

public class SpielerAuswahlActivity extends AppCompatActivity {

    private APIServer apiServer; // DB-Zugriff über fake REST API
    private ListView spielerauswahl; //Liste, die angezeigt wird

    private List<Spieler> spielerListe; // angelegte Spieler

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_spieler_auswahl); // Java & XML verbinden

        spielerauswahl = findViewById(R.id.listViewSpieler); // ListView aus Layout adressieren

        apiServer = new APIServer(this); //Fake REST API Server-Objekt für Zugriffsverwaltung

        ladeSpieler();
    }

    private void ladeSpieler() {

        spielerListe = apiServer.getAlleSpieler(); // Daten aus der DB holen

        List<String> namensListe = new ArrayList<>();// nur Namen raussuchen

        for (Spieler s : spielerListe) { //durch Spielerliste iterieren
            namensListe.add(s.getName());
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<>( // Daten mit ListView verbinden
                this, android.R.layout.simple_list_item_1, namensListe);// Standard-Listen-Layout

        spielerauswahl.setAdapter(adapter); // Liste anzeigen

        spielerauswahl.setOnItemClickListener((parent, view, position, id) -> { // Klick auf Spieler

            Spieler angeklickt = spielerListe.get(position); // anzeigen, welcher Spieler angeklickt wurde


            speichereSpieler(angeklickt); // gewählten Spieler speichern, damit klar ist, wer aktiv ist

            Intent intent = new Intent(SpielerAuswahlActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        );
    }

    private void speichereSpieler(Spieler spieler) {

        SharedPreferences prefs = getSharedPreferences("app", MODE_PRIVATE); // speichert ausgewählten Spieler auf Gerät
        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt("spielerId", spieler.getId());
        editor.putString("spielerName", spieler.getName());

        editor.apply();
    }
}