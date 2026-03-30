package de.hemane.boardgamerapp;

import static de.hemane.boardgamerapp.helfer.DatumHelfer.parseDatum;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hemane.boardgamerapp.controller.APIServer;
import de.hemane.boardgamerapp.klassen.Spieler;
import de.hemane.boardgamerapp.klassen.Termin;
import de.hemane.boardgamerapp.ui.BewertungActivity;
import de.hemane.boardgamerapp.ui.ChatActivity;
import de.hemane.boardgamerapp.ui.NachTerminDetailActivity;
import de.hemane.boardgamerapp.ui.VorTerminDetailActivity;

public class MainActivity extends AppCompatActivity {

    private APIServer apiServer;
    private ListView listView;
    private List<Termin> terminListe;
    private ListView listViewLetzterTermin;
    private List<Termin> aktuelleTermine;
    private Termin letzterTermin;
    private ImageView buttonBewertung;
    private ImageView buttonChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listViewTermine);
        listViewLetzterTermin = findViewById(R.id.listViewLetzterTermin);
        buttonBewertung = findViewById(R.id.buttonBewertung);
        buttonChat = findViewById(R.id.buttonChat);

        apiServer = new APIServer(this);

        buttonBewertung.setOnClickListener(v -> {

            Intent intent = new Intent(MainActivity.this, BewertungActivity.class);
            startActivity(intent);

        });

        buttonChat.setOnClickListener(v -> {

            Intent intent = new Intent(MainActivity.this, ChatActivity.class);
            startActivity(intent);

        });

        ladeTermine();
    }

    private void ladeTermine() {

        terminListe = apiServer.getAlleTermine();

        List<Termin> kommende = new ArrayList<>(); // Liste kommender Termin
        List<Termin> vergangene = new ArrayList<>(); //Liste vergangener Termine

        LocalDateTime heute = LocalDateTime.now();

        for (Termin t : terminListe) {

            LocalDateTime terminZeit = parseDatum(t.getDatum());

            if (terminZeit == null) continue;

            if (terminZeit.isAfter(heute)) {
                kommende.add(t);
            } else {
                vergangene.add(t);
            }
        }

        kommende.sort((a, b) -> Objects.requireNonNull(parseDatum(a.getDatum())).compareTo(parseDatum(b.getDatum()))); // sortiert Listen
        vergangene.sort((a, b) -> Objects.requireNonNull(parseDatum(b.getDatum())).compareTo(parseDatum(a.getDatum())));

        List<Termin> kommendeVier = kommende.subList(0, Math.min(4, kommende.size())); // nur noch 4 der zukünftigen

        Termin letzter = !vergangene.isEmpty() ? vergangene.get(0) : null; // nur noch 1 letzter

        List<String> listeKommender = new ArrayList<>();
        for (Termin t : kommendeVier) {
            Spieler s = apiServer.getSpielerById(t.getGastgeberId());
            listeKommender.add(t.getDatum() + " - " + s.getName());
        }

        List<String> listeLetzter = new ArrayList<>();
        if (letzter != null) {
            Spieler s = apiServer.getSpielerById(letzter.getGastgeberId());
            listeLetzter.add(letzter.getDatum() + " - " + s.getName());
        }

        listView.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, listeKommender));

        listViewLetzterTermin.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, listeLetzter));

        aktuelleTermine = kommendeVier;
        letzterTermin = letzter;

        listView.setOnItemClickListener((parent, view, position, id) -> {

            Termin geklickt = aktuelleTermine.get(position);

            Intent intent = new Intent(MainActivity.this, VorTerminDetailActivity.class);
            intent.putExtra("terminId", geklickt.getId());

            startActivity(intent);
        });

        listViewLetzterTermin.setOnItemClickListener((parent, view, position, id) -> {

            if (letzterTermin == null) return;

            Intent intent = new Intent(MainActivity.this, NachTerminDetailActivity.class);
            intent.putExtra("terminId", letzterTermin.getId());

            startActivity(intent);
        });
    }

}
