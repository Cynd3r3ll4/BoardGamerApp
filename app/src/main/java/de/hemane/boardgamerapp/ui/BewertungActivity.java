package de.hemane.boardgamerapp.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import de.hemane.boardgamerapp.R;
import de.hemane.boardgamerapp.controller.APIServer;
import de.hemane.boardgamerapp.klassen.Spiel;
import de.hemane.boardgamerapp.klassen.Spieler;
import de.hemane.boardgamerapp.klassen.Termin;

public class BewertungActivity extends AppCompatActivity {

    private APIServer apiServer;
    private TextView textDatum;
    private TextView textGastgeber;
    private ListView listTeilnehmer;
    private TextView textGewinnerSpiel;
    private int spielerId;
    private Termin termin;
    private RatingBar ratingGastgeber;
    private RatingBar ratingEssen;
    private RatingBar ratingAllgemein;

    private TextView textGastgeberSchnitt;
    private TextView textEssenSchnitt;
    private TextView textAllgemeinSchnitt;

    private LinearLayout layoutGastgeberKommentare;
    private LinearLayout layoutEssenKommentare;
    private LinearLayout layoutAllgemeinKommentare;

        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_bewertung);

            textDatum = findViewById(R.id.textDatum);
            textGastgeber = findViewById(R.id.textGastgeber);
            listTeilnehmer = findViewById(R.id.listTeilnehmer);
            textGewinnerSpiel = findViewById(R.id.textGewinnerSpiel);
            ratingGastgeber = findViewById(R.id.ratingGastgeber);
            ratingEssen = findViewById(R.id.ratingEssen);
            ratingAllgemein = findViewById(R.id.ratingAllgemein);
            textGastgeberSchnitt = findViewById(R.id.textGastgeberSchnitt);
            textEssenSchnitt = findViewById(R.id.textEssenSchnitt);
            textAllgemeinSchnitt = findViewById(R.id.textAllgemeinAvg);
            layoutGastgeberKommentare = findViewById(R.id.layoutGastgeberKommentare);
            layoutEssenKommentare = findViewById(R.id.layoutEssenKommentare);
            layoutAllgemeinKommentare = findViewById(R.id.layoutAllgemeinKommentare);

            SharedPreferences prefs = getSharedPreferences("app", MODE_PRIVATE);
            spielerId = prefs.getInt("spielerId", -1);

            apiServer = new APIServer(this);

            ladeTermin();
        }

        private void ladeTermin() {

            termin = apiServer.getLetzterTerminVonGastgeber(spielerId);

            if (termin == null) {
                textDatum.setText(getString(R.string.keinTermin));
                return;
            }

            Spieler gastgeber = apiServer.getSpielerById(termin.getGastgeberId());

            textDatum.setText(termin.getDatum());
            textGastgeber.setText(getString(R.string.gastgeber, gastgeber.getName()));

            ladeTeilnehmerliste();
            ladeGewinnerSpiel();
            ladeBewertungen();
        }

    private void ladeTeilnehmerliste() {

        List<Spieler> teilnehmer = apiServer.getTeilnehmerByTerminId(termin.getId());

        List<String> namen = new ArrayList<>();

        if (teilnehmer.isEmpty()) {
            namen.add(getString(R.string.keineTeilnehmer));
        } else {
            for (Spieler s : teilnehmer) {
                namen.add(s.getName());
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                namen
        );

        listTeilnehmer.setAdapter(adapter);
    }

    private void ladeGewinnerSpiel() {

        Spiel spiel = apiServer.getGewinnerSpiel(termin.getId());

        if (spiel != null) {
            textGewinnerSpiel.setText(spiel.getName());
        } else {
            textGewinnerSpiel.setText(getString(R.string.keinspiel));
        }
    }

    private void ladeBewertungen() {

        float avgGastgeber = apiServer.getSchnittGastgeberBewertung(termin.getId());
        float avgEssen = apiServer.getSchnittEssenBewertung(termin.getId());
        float avgAllgemein = apiServer.getSchnittAllgemeinBewertung(termin.getId());

        ratingGastgeber.setRating(avgGastgeber);
        ratingEssen.setRating(avgEssen);
        ratingAllgemein.setRating(avgAllgemein);

        textGastgeberSchnitt.setText(String.format("%.1f", avgGastgeber));
        textEssenSchnitt.setText(String.format("%.1f", avgEssen));
        textAllgemeinSchnitt.setText(String.format("%.1f", avgAllgemein));

        ladeKommentare();
    }

    private void ladeKommentare() {

        List<String> gastgeberKommentare = apiServer.getGastgeberKommentare(termin.getId());
        List<String> essenKommentare = apiServer.getEssenKommentare(termin.getId());
        List<String> allgemeinKommentare = apiServer.getAllgemeinKommentare(termin.getId());

        addKommentareZuLayout(layoutGastgeberKommentare,gastgeberKommentare);
        addKommentareZuLayout(layoutEssenKommentare, essenKommentare);
        addKommentareZuLayout(layoutAllgemeinKommentare, allgemeinKommentare);
    }

    private void addKommentareZuLayout(LinearLayout layout, List<String> kommentare) {

        layout.removeAllViews();

        for (String kommentar : kommentare) {

            if (kommentar == null || kommentar.isEmpty()) continue;

            TextView tv = new TextView(this);
            tv.setText("- " + kommentar);

            layout.addView(tv);
        }
    }

}
