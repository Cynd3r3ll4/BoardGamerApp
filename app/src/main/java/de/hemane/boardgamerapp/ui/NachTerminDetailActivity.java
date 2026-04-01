package de.hemane.boardgamerapp.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.List;

import de.hemane.boardgamerapp.R;
import de.hemane.boardgamerapp.controller.APIServer;
import de.hemane.boardgamerapp.helfer.DatumHelfer;
import de.hemane.boardgamerapp.klassen.Bewertung;
import de.hemane.boardgamerapp.klassen.Spiel;
import de.hemane.boardgamerapp.klassen.Spieler;
import de.hemane.boardgamerapp.klassen.Teilnahme;
import de.hemane.boardgamerapp.klassen.Termin;

public class NachTerminDetailActivity extends AppCompatActivity {

    private APIServer apiServer;
    private TextView textDatum;
    private TextView textGastgeber;
    private int terminId;
    private Termin termin;
    private LinearLayout listTeilnehmer;
    private TextView textGewinnerSpiel;
    private int gastgeberBewertung = 0;
    private int essenBewertung = 0;
    private int allgemeinBewertung = 0;
    private EditText editGastgeberKommentar;
    private EditText editEssenKommentar;
    private EditText editAllgemeinKommentar;
    private Button buttonBewertung;
    private ImageView[] gastgeberSterne;
    private ImageView[] essenSterne;
    private ImageView[] allgemeinSterne;
    private int spielerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nach_termin_detail);

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material);
        toolbar.setNavigationOnClickListener(v -> finish());

        textDatum = findViewById(R.id.textDatum);
        textGastgeber = findViewById(R.id.textGastgeber);
        listTeilnehmer = findViewById(R.id.listTeilnehmer);
        textGewinnerSpiel = findViewById(R.id.textGewinnerSpiel);
        editGastgeberKommentar = findViewById(R.id.editGastgeberKommentar);
        editEssenKommentar = findViewById(R.id.editEssenKommentar);
        editAllgemeinKommentar = findViewById(R.id.editAllgemeinKommentar);
        buttonBewertung = findViewById(R.id.buttonBewertung);

        SharedPreferences prefs = getSharedPreferences("app", MODE_PRIVATE);
        spielerId = prefs.getInt("spielerId", -1);

        gastgeberSterne = new ImageView[]{
                findViewById(R.id.gastgeberStern1),
                findViewById(R.id.gastgeberStern2),
                findViewById(R.id.gastgeberStern3),
                findViewById(R.id.gastgeberStern4),
                findViewById(R.id.gastgeberStern5)
        };
        essenSterne = new ImageView[]{
                findViewById(R.id.essenStern1),
                findViewById(R.id.essenStern2),
                findViewById(R.id.essenStern3),
                findViewById(R.id.essenStern4),
                findViewById(R.id.essenStern5)
        };
        allgemeinSterne = new ImageView[]{
                findViewById(R.id.allgStern1),
                findViewById(R.id.allgStern2),
                findViewById(R.id.allgStern3),
                findViewById(R.id.allgStern4),
                findViewById(R.id.allgStern5)
        };

        for (int i = 0; i < gastgeberSterne.length; i++) { // KLick-Listener für jeden einzelnen Stern
            final int index = i;
            gastgeberSterne[i].setOnClickListener(v -> {
                gastgeberBewertung = index + 1;
                updateSterne(gastgeberSterne, gastgeberBewertung);
            });
        }

        for (int i = 0; i < essenSterne.length; i++) {
            final int index = i;
            essenSterne[i].setOnClickListener(v -> {
                essenBewertung = index + 1;
                updateSterne(essenSterne, essenBewertung);
            });
        }

        for (int i = 0; i < allgemeinSterne.length; i++) {
            final int index = i;
            allgemeinSterne[i].setOnClickListener(v -> {
                allgemeinBewertung = index + 1;
                updateSterne(allgemeinSterne, allgemeinBewertung);
            });
        }

        buttonBewertung.setOnClickListener(v -> {
            apiServer.speicherBewertung(
                    terminId,
                    spielerId,
                    gastgeberBewertung,
                    editGastgeberKommentar.getText().toString(),
                    essenBewertung,
                    editEssenKommentar.getText().toString(),
                    allgemeinBewertung,
                    editAllgemeinKommentar.getText().toString()
            );
            Toast.makeText(this, getString(R.string.btoast), Toast.LENGTH_SHORT).show();
            disableBewertung();
        });

        apiServer = new APIServer(this);
        terminId = getIntent().getIntExtra("terminId", -1);
        ladeTermin();
    }

    private void ladeTermin() {
        termin = apiServer.getTerminById(terminId);
        Spieler gastgeber = apiServer.getSpielerById(termin.getGastgeberId());

        textDatum.setText(termin.getDatum());
        textGastgeber.setText(getString(R.string.gastgeber, gastgeber.getName()));

        ladeTeilnehmerliste();
        ladeGewinnerSpiel();
        ladeBewertung();
        aufBewertungPruefen();
        checkeZeitNachTermin();
        checkeTeilnahme();
    }

    private void ladeTeilnehmerliste() {
        List<Spieler> teilnehmer = apiServer.getTeilnehmerByTerminId(terminId);
        listTeilnehmer.removeAllViews(); // Layout leeren, damit keine alten Einträge stehen bleiben


        if (teilnehmer.isEmpty()) {
            TextView tv = new TextView(this);
            tv.setText(getString(R.string.keineTeilnehmer));
            tv.setPadding(0, 10, 0, 10);
            listTeilnehmer.addView(tv);
        } else {
            for (Spieler s : teilnehmer) {
                TextView tv = new TextView(this);
                tv.setText(getString(R.string.teilnehmer_eintrag, s.getName()));
                tv.setPadding(0, 5, 0, 5);
                tv.setTextSize(16);
                listTeilnehmer.addView(tv);
            }
        }
    }

    private void ladeGewinnerSpiel() {
        Spiel spiel = apiServer.getGewinnerSpiel(terminId);
        if (spiel != null) {
            textGewinnerSpiel.setText(spiel.getName());
        } else {
            textGewinnerSpiel.setText(getString(R.string.keinspiel));
        }
    }

    private void updateSterne(ImageView[] sterne, int bewertung) {
        for (int i = 0; i < sterne.length; i++) {
            if (i < bewertung) {
                sterne[i].setImageResource(android.R.drawable.btn_star_big_on);
            } else {
                sterne[i].setImageResource(android.R.drawable.btn_star_big_off);
            }
        }
    }

    private void disableBewertung() {
        buttonBewertung.setEnabled(false);
        buttonBewertung.setAlpha(0.5f);
        editGastgeberKommentar.setEnabled(false);
        editEssenKommentar.setEnabled(false);
        editAllgemeinKommentar.setEnabled(false);

        for (ImageView s : gastgeberSterne) {
            s.setEnabled(false);
            s.setAlpha(0.5f);
        }
        for (ImageView s : essenSterne) {
            s.setEnabled(false);
            s.setAlpha(0.5f);
        }
        for (ImageView s : allgemeinSterne) {
            s.setEnabled(false);
            s.setAlpha(0.5f);
        }
    }

    private void aufBewertungPruefen() {
        if (apiServer.hatTerminBewertet(terminId, spielerId)) {
            disableBewertung();
        }
    }

    private void checkeZeitNachTermin() {
        if (DatumHelfer.istZweiTageNachTermin(termin.getDatum())) {
            disableBewertung();
        }
    }

    private void checkeTeilnahme() { // nur Spieler, die teilgenommen haben, dürfen bewerten
        Teilnahme teilnahme = apiServer.getTeilnahmeBySpielerUndTermin(spielerId, terminId);
        if (teilnahme == null || teilnahme.getTeilnahme() != 1) {
            disableBewertung();
        }
    }

    private void ladeBewertung() {
        Bewertung bewertung = apiServer.getBewertungByTerminUndSpieler(terminId, spielerId);
        if (bewertung != null) {
            updateSterne(gastgeberSterne, bewertung.getGastgeberSterne());
            updateSterne(essenSterne, bewertung.getEssenSterne());
            updateSterne(allgemeinSterne, bewertung.getAllgemeinSterne());
            editGastgeberKommentar.setText(bewertung.getGastgeberKommentar());
            editEssenKommentar.setText(bewertung.getEssenKommentar());
            editAllgemeinKommentar.setText(bewertung.getAllgemeinKommentar());
            disableBewertung();
        }
    }
}
