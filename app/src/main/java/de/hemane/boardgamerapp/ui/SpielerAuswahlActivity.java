package de.hemane.boardgamerapp.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import de.hemane.boardgamerapp.R;
import de.hemane.boardgamerapp.controller.APIServer;
import de.hemane.boardgamerapp.klassen.Spieler;

public class SpielerAuswahlActivity extends AppCompatActivity {

    private APIServer apiServer;
    private ListView spielerauswahl;

    private List<Spieler> spielerListe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spieler_auswahl);

        spielerauswahl = findViewById(R.id.listViewSpieler);

        apiServer = new APIServer(this);

        ladeSpieler();
    }

    private void ladeSpieler() {
        spielerListe = apiServer.getAlleSpieler();

        List<String> namensListe = new ArrayList<>();

        for (Spieler s : spielerListe) {
            namensListe.add(s.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, namensListe);

        spielerauswahl.setAdapter(adapter);

        spielerauswahl.setOnItemClickListener((parent, view, position, id) -> {
            Spieler angeklickt = spielerListe.get(position);

            speichereSpieler(angeklickt);

            Intent intent = new Intent(SpielerAuswahlActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        );
    }

    private void speichereSpieler(Spieler spieler) { // Spieler auswählen und Auswahl speichern, für weitere Aktionen
        SharedPreferences prefs = getSharedPreferences("app", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt("spielerId", spieler.getId());
        editor.putString("spielerName", spieler.getName());

        editor.apply();
    }
}