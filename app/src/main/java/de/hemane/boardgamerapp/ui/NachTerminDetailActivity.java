package de.hemane.boardgamerapp.ui;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import de.hemane.boardgamerapp.R;
import de.hemane.boardgamerapp.controller.APIServer;
import de.hemane.boardgamerapp.klassen.Spieler;
import de.hemane.boardgamerapp.klassen.Termin;

public class NachTerminDetailActivity extends AppCompatActivity {

    private APIServer apiServer;
    private TextView textDatum;
    private TextView textGastgeber;
    private int terminId;
    private Termin termin;
    private ListView listTeilnehmer;
    private TextView textGewinnerSpiel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nach_termin_detail);

        textDatum = findViewById(R.id.textDatum); // Views aus Layout ansprechen
        textGastgeber = findViewById(R.id.textGastgeber);
        listTeilnehmer = findViewById(R.id.listTeilnehmer);
        textGewinnerSpiel = findViewById(R.id.textGewinnerSpiel);

        apiServer = new APIServer(this);

        terminId = getIntent().getIntExtra("terminId", -1);

        ladeTermin();
    }

    private void ladeTermin() {
        termin = apiServer.getTerminById(terminId); // Termin laden

        Spieler gastgeber = apiServer.getSpielerById(termin.getGastgeberId()); // Gastgeber laden

        textDatum.setText(termin.getDatum());
        textGastgeber.setText(getString(R.string.gastgeber, gastgeber.getName()));
    }

    private void ladeTeilnehmerliste() {
        List<Spieler> teilnehmer = apiServer.getTeilnehmerByTerminId(terminId);

        List<String> namen = new ArrayList<>();

        for (Spieler s : teilnehmer) {
            namen.add(s.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                namen
        );

        listTeilnehmer.setAdapter(adapter);
    }
}