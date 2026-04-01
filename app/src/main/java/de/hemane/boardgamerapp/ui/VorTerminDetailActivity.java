package de.hemane.boardgamerapp.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.List;

import de.hemane.boardgamerapp.R;
import de.hemane.boardgamerapp.controller.APIServer;
import de.hemane.boardgamerapp.helfer.DatumHelfer;
import de.hemane.boardgamerapp.klassen.Spiel;
import de.hemane.boardgamerapp.klassen.Spieler;
import de.hemane.boardgamerapp.klassen.Teilnahme;
import de.hemane.boardgamerapp.klassen.Termin;

public class VorTerminDetailActivity extends AppCompatActivity {

    private APIServer apiServer;
    private TextView textDatum;
    private TextView textGastgeber;
    private CheckBox checkTeilnahme;
    private int terminId;
    private Termin termin;
    private RadioGroup radioGroupSpiele;
    private EditText bearbeiteSpielVorschlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vor_termin_detail);

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material);
        toolbar.setNavigationOnClickListener(v -> finish());

        textDatum = findViewById(R.id.textDatum);
        textGastgeber = findViewById(R.id.textGastgeber);
        checkTeilnahme = findViewById(R.id.checkTeilnahme);
        radioGroupSpiele = findViewById(R.id.radioGroupSpiele);
        bearbeiteSpielVorschlag = findViewById(R.id.editSpielVorschlag);

        apiServer = new APIServer(this);

        terminId = getIntent().getIntExtra("terminId", -1);

        ladeTermin();
        setupTeilnahme();
        ladeSpiele();
        checkeFristablauf();

        bearbeiteSpielVorschlag.setOnEditorActionListener((v, actionId, event) -> {
            if (!checkTeilnahme.isChecked()) { // nur wenn Spieler teilnimmt
                return true;
            }

            String name = bearbeiteSpielVorschlag.getText().toString();

            if (!name.isEmpty()) {

                apiServer.insertSpiel(name, terminId);

                bearbeiteSpielVorschlag.setText(""); // Textfeld nach Eingabe wieder leeren

                ladeSpiele();
            }

            return true;
        });
    }

    private void ladeTermin() {
        termin = apiServer.getTerminById(terminId);

        Spieler gastgeber = apiServer.getSpielerById(termin.getGastgeberId());

        textDatum.setText(termin.getDatum());
        textGastgeber.setText(getString(R.string.gastgeber, gastgeber.getName()));
    }

    private void setupTeilnahme() {

        SharedPreferences prefs = getSharedPreferences("app", MODE_PRIVATE);
        int spielerId = prefs.getInt("spielerId", -1);

        Teilnahme teilnahme = apiServer.getTeilnahmeBySpielerUndTermin(spielerId, terminId);

        if (teilnahme != null) {
            checkTeilnahme.setChecked(teilnahme.getTeilnahme() == 1);
        }

        updateUIStatus(checkTeilnahme.isChecked());

        checkTeilnahme.setOnCheckedChangeListener((buttonView, isChecked) -> { // Klick um Haken für Teilnahme zu setzen/entfernen
            updateUIStatus(isChecked);

            if (!isChecked) {
                apiServer.deleteAbstimmungBySpieler(spielerId); // Voting löschen, falls Spieler Teilnahme zurückzieht
            }

            int wert = isChecked ? 1 : 0;

            if (teilnahme == null) {
                apiServer.insertTeilnahme(spielerId, terminId, wert);
            } else {
                apiServer.updateTeilnahme(
                        teilnahme.getId(),
                        spielerId,
                        terminId,
                        wert
                );
            }
        });
    }

    private void ladeSpiele() {
        SharedPreferences prefs = getSharedPreferences("app", MODE_PRIVATE);
        int spielerId = prefs.getInt("spielerId", -1);

        int abgestimmtesSpielId = apiServer.getAbstimmungBySpieler(spielerId);

        radioGroupSpiele.removeAllViews();

        List<Spiel> spiele = apiServer.getSpieleByTermin(terminId);

        radioGroupSpiele.setOnCheckedChangeListener(null); // um Klick zu resetten

        for (Spiel spiel : spiele) {

            int stimmen = apiServer.getStimmenCount(spiel.getId());

            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(getString(R.string.platzhalterMitKlammern, spiel.getName(), stimmen));

            radioButton.setTag(spiel.getId());

            if (spiel.getId() == abgestimmtesSpielId) { // damit Button ausgewählt bleibt
                radioButton.setChecked(true);
            }

            radioGroupSpiele.addView(radioButton);
        }

        radioGroupSpiele.setOnCheckedChangeListener((group, checkedId) -> {
            if (!checkTeilnahme.isChecked()) {
                return;
            }

            RadioButton selected = findViewById(checkedId);

            if (selected != null) {

                int spielId = (int) selected.getTag();

                apiServer.deleteAbstimmungBySpieler(spielerId); // alte Abstimmung löschen

                apiServer.insertAbstimmung(spielerId, spielId); // neue Abstimmung speichern

                ladeSpiele();
                updateUIStatus(checkTeilnahme.isChecked()); // Radiobuttons updaten, damit ausgegraut bleibt, falls keine Teilnahme
            }
        });
    }

    private void updateUIStatus(boolean nimmtTeil) {

        for (int i = 0; i < radioGroupSpiele.getChildCount(); i++) {
            radioGroupSpiele.getChildAt(i).setEnabled(nimmtTeil);
        }

        bearbeiteSpielVorschlag.setEnabled(nimmtTeil);

        float alpha = nimmtTeil ? 1.0f : 0.5f;
        radioGroupSpiele.setAlpha(alpha);
        bearbeiteSpielVorschlag.setAlpha(alpha);
    }

    private void checkeFristablauf() {

        boolean abgelaufen = DatumHelfer.istZweiTagesFristAbgelaufen(termin.getDatum());

        if (abgelaufen) { // wenn abgelaufen, sperren & ausgrauen

            checkTeilnahme.setEnabled(false);
            radioGroupSpiele.setEnabled(false);
            bearbeiteSpielVorschlag.setEnabled(false);

            checkTeilnahme.setAlpha(0.5f);
            radioGroupSpiele.setAlpha(0.5f);
            bearbeiteSpielVorschlag.setAlpha(0.5f);
        }
    }

}
