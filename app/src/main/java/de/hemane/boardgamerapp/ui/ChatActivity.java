package de.hemane.boardgamerapp.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import de.hemane.boardgamerapp.R;
import de.hemane.boardgamerapp.controller.APIServer;
import de.hemane.boardgamerapp.helfer.ChatAdapter;
import de.hemane.boardgamerapp.helfer.DatumHelfer;
import de.hemane.boardgamerapp.klassen.Chatnachricht;
import de.hemane.boardgamerapp.klassen.Teilnahme;
import de.hemane.boardgamerapp.klassen.Termin;

public class ChatActivity extends AppCompatActivity {

    private ListView listViewChat;
    private EditText editTextNachricht;
    private Button buttonSenden;
    private APIServer apiServer;
    private List<Chatnachricht> nachrichtenListe;
    private ChatAdapter adapter;
    private int aktuellerSpielerId;
    private LinearLayout layoutQuickActions;
    private Button buttonAbsage;
    private Button buttonZusage;
    private Termin naechsterTermin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material);
        toolbar.setNavigationOnClickListener(v -> finish());

        listViewChat = findViewById(R.id.listViewChat);
        editTextNachricht = findViewById(R.id.editTextNachricht);
        buttonSenden = findViewById(R.id.buttonSenden);
        layoutQuickActions = findViewById(R.id.layoutQuickActions);
        buttonAbsage = findViewById(R.id.buttonAbsage);
        buttonZusage = findViewById(R.id.buttonZusage);

        apiServer = new APIServer(this);

        SharedPreferences prefs = getSharedPreferences("app", MODE_PRIVATE);
        aktuellerSpielerId = prefs.getInt("spielerId", -1);

        nachrichtenListe = apiServer.getAlleChatnachrichten();
        adapter = new ChatAdapter(this, nachrichtenListe, aktuellerSpielerId);
        listViewChat.setAdapter(adapter);

        listViewChat.post(() -> listViewChat.setSelection(adapter.getCount() - 1)); // beim ersten Laden in der Chatliste nach unten scrollen

        buttonSenden.setOnClickListener(v -> {
            String text = editTextNachricht.getText().toString().trim();
            if (text.isEmpty()) return;

            sendeNachricht(text);
            editTextNachricht.setText(""); // Eingabefeld leeren
        });

        buttonAbsage.setOnClickListener(v -> { //Nachricht senden und Teilnahme aktualisieren
            sendeChatUndUpdateTeilnahme(getString(R.string.absage), 0);
        });

        buttonZusage.setOnClickListener(v -> {
            sendeChatUndUpdateTeilnahme(getString(R.string.zusage), 1);
        });

        setVorgefertigteNachrichten();
    }

    private void sendeNachricht(String text) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm 'Uhr'");
        String zeit = LocalDateTime.now().format(formatter);

        apiServer.sendeChatnachricht(aktuellerSpielerId, text, zeit);

        List<Chatnachricht> neu = apiServer.getAlleChatnachrichten();
        if (neu != null) {
            nachrichtenListe.clear();
            nachrichtenListe.addAll(neu);
            adapter.notifyDataSetChanged();

            listViewChat.post(() -> listViewChat.setSelection(adapter.getCount() - 1)); // nach unten scrollen nach neuer Nachricht (Chat-Gefühl)
        }
    }

    private Termin getNaechsterTermin() {
        List<Termin> alle = apiServer.getAlleTermine();
        LocalDateTime jetzt = LocalDateTime.now();
        Termin naechster = null;

        for (Termin t : alle) {
            LocalDateTime datum = DatumHelfer.parseDatum(t.getDatum());
            if (datum == null) continue;

            if (datum.isAfter(jetzt)) {
                if (naechster == null || datum.isBefore(DatumHelfer.parseDatum(naechster.getDatum()))) {
                    naechster = t;
                }
            }
        }
        return naechster;
    }

    private void setVorgefertigteNachrichten() {
        naechsterTermin = getNaechsterTermin();

        if (naechsterTermin == null) {
            layoutQuickActions.setVisibility(View.GONE);
            return;
        }

        boolean innerhalbZweiTage = DatumHelfer.istZweiTagesFristAbgelaufen(naechsterTermin.getDatum());

        if (!innerhalbZweiTage) { // wenn icht schon 2 Tage vor Termin → ausblenden
            layoutQuickActions.setVisibility(View.GONE);
            return;
        }

        layoutQuickActions.setVisibility(View.VISIBLE);
        
        Teilnahme teilnahme = apiServer.getTeilnahmeBySpielerUndTermin(aktuellerSpielerId, naechsterTermin.getId()); // Teilnahmestatus des Spielers

        if (teilnahme != null && teilnahme.getTeilnahme() == 1) { // wenn teilnahme == null (noch kein Eintrag) / teilnahme == 0 (abgesagt) --> Zusage-Button zeigen
            buttonAbsage.setVisibility(View.VISIBLE);
            buttonZusage.setVisibility(View.GONE);
        } else {  // wenn teilnahme == 1 (zugesagt) -> Absage-Button zeigen
            buttonAbsage.setVisibility(View.GONE);
            buttonZusage.setVisibility(View.VISIBLE);
        }
        buttonAbsage.setEnabled(true);
        buttonZusage.setEnabled(true);
    }

    private void sendeChatUndUpdateTeilnahme(String text, int teilnahmeWert) {
        buttonAbsage.setEnabled(false); // Deaktiviert umd Doppel-Klicks zu vermeiden
        buttonZusage.setEnabled(false);

        sendeNachricht(text);

        Teilnahme vorhanden = apiServer.getTeilnahmeBySpielerUndTermin(aktuellerSpielerId, naechsterTermin.getId()); // Teilnahme in DB prüfen & updaten / neu anlegen

        if (vorhanden == null) {
            apiServer.insertTeilnahme(aktuellerSpielerId, naechsterTermin.getId(), teilnahmeWert);
        } else {
            apiServer.updateTeilnahmeOhneId(aktuellerSpielerId, naechsterTermin.getId(), teilnahmeWert);
        }
        setVorgefertigteNachrichten(); // Ansicht aktualisieren
    }
}
