package de.hemane.boardgamerapp.controller;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import de.hemane.boardgamerapp.datenbank.AbstimmungDAO;
import de.hemane.boardgamerapp.datenbank.BewertungDAO;
import de.hemane.boardgamerapp.datenbank.ChatnachrichtDAO;
import de.hemane.boardgamerapp.datenbank.SpielDAO;
import de.hemane.boardgamerapp.datenbank.SpielerDAO;
import de.hemane.boardgamerapp.datenbank.TeilnahmeDAO;
import de.hemane.boardgamerapp.datenbank.TerminDAO;
import de.hemane.boardgamerapp.klassen.Abstimmung;
import de.hemane.boardgamerapp.klassen.Bewertung;
import de.hemane.boardgamerapp.klassen.Chatnachricht;
import de.hemane.boardgamerapp.klassen.Spiel;
import de.hemane.boardgamerapp.klassen.Spieler;
import de.hemane.boardgamerapp.klassen.Teilnahme;
import de.hemane.boardgamerapp.klassen.Termin;

public class APIServer {

    private SpielerDAO spielerDAO;
    private TerminDAO terminDAO;
    private TeilnahmeDAO teilnahmeDAO;
    private SpielDAO spielDAO;
    private AbstimmungDAO abstimmungDAO;
    private BewertungDAO bewertungDAO;
    private ChatnachrichtDAO chatnachrichtDAO;

    public APIServer(Context context) { // Verwaltung aller DAOs, um deren Methoden aufzurufen
        spielerDAO = new SpielerDAO(context);
        terminDAO = new TerminDAO(context);
        teilnahmeDAO = new TeilnahmeDAO(context);
        spielDAO = new SpielDAO(context);
        abstimmungDAO = new AbstimmungDAO(context);
        bewertungDAO = new BewertungDAO(context);
        chatnachrichtDAO = new ChatnachrichtDAO(context);
    }

    // Klasse simuliert REST-API, damit nicht direkt auf DAOs zugegriffen wird

    // SpielerDAO-Methoden
    public void insertSpieler(String name) {
        spielerDAO.insertSpieler(name);
    }

    public List<Spieler> getAlleSpieler() {
        return spielerDAO.getAlleSpieler();
    }

    public Spieler getSpielerById(int id) {
        return spielerDAO.getSpielerById(id);
    }

    public void updateSpieler(int id, String name) {
        spielerDAO.updateSpieler(id, name);
    }

    public void deleteSpieler(int id) {
        spielerDAO.deleteSpieler(id);
    }

    // TerminDAO-Methoden
    public void insertTermin(String datum, int gastgeberId) {
        terminDAO.insertTermin(datum,gastgeberId);
    }

    public List<Termin> getAlleTermine() {
        return terminDAO.getAlleTermine();
    }

    public Termin getTerminById(int id) {
        return terminDAO.getTerminById(id);
    }

    public void updateTermin(int id, String datum, int gastgeberId) {
        terminDAO.updateTermin(id, datum, gastgeberId);
    }

    public void deleteTermin(int id) {
        terminDAO.deleteTermin(id);
    }

    public Termin getLetzterTerminVonGastgeber(int spielerId) {
        return terminDAO.getLetzterTerminVonGastgeber(spielerId);
    }

    // TeilnahmeDAO-Methoden
    public void insertTeilnahme(int spielerId, int terminId, int teilnahme) {
        teilnahmeDAO.insertTeilnahme(spielerId, terminId, teilnahme);
    }

    public List<Teilnahme> getAlleTeilnahmen() {
        return teilnahmeDAO.getAlleTeilnahmen();
    }

    public Teilnahme getTeilnahmeById(int id) {
        return teilnahmeDAO.getTeilnahmeById(id);
    }

    public void updateTeilnahme(int id, int spielerId, int terminId, int teilnahme) {
        teilnahmeDAO.updateTeilnahme(id, spielerId, terminId, teilnahme);
    }

    public void updateTeilnahmeOhneId(int spielerId, int terminId, int wert) {
        teilnahmeDAO.updateTeilnahmeOhneId(spielerId, terminId, wert);
    }

    public Teilnahme getTeilnahmeBySpielerUndTermin(int spielerId, int terminId) {
        return teilnahmeDAO.getTeilnahmeBySpielerUndTermin(spielerId, terminId);
    }

    public void deleteTeilnahme(int id) {
        teilnahmeDAO.deleteTeilnahme(id);
    }

    // SpielDAO-Methoden
    public void insertSpiel(String name, int terminId) {
        spielDAO.insertSpiel(name, terminId);
    }

    public List<Spiel> getAlleSpiele() {
        return spielDAO.getAlleSpiele();
    }

    public Spiel getSpieleById(int id) {
        return spielDAO.getSpieleById(id);
    }

    public List<Spiel> getSpieleByTermin(int terminId) {
        return spielDAO.getSpieleByTermin(terminId);
    }

    public void updateSpiel(int id, String name, int terminId) {
        spielDAO.updateSpiel(id, name, terminId);
    }

    public void deleteSpiel(int id){
        spielDAO.deleteSpiel(id);
    }

    //AbstimmungDAO-Methoden
    public void insertAbstimmung(int spielerId, int spielId) {
        abstimmungDAO.insertAbstimmung(spielerId, spielId);
    }

    public List<Abstimmung> getAlleAbstimmungen() {
        return abstimmungDAO.getAlleAbstimmungen();
    }

    public Abstimmung getAbstimmungById(int id) {
        return abstimmungDAO.getAbstimmungById(id);
    }

    public void updateAbstimmung(int id, int spielerId, int spielId) {
        abstimmungDAO.updateAbstimmung(id, spielerId, spielId);
    }

    public void deleteAbstimmung(int id) {
        abstimmungDAO.deleteAbstimmung(id);
    }

    public int getStimmenCount(int spielId) {
        return abstimmungDAO.getStimmenAnzahl(spielId);
    }

    public void deleteAbstimmungBySpieler(int spielerId) {
        abstimmungDAO.deleteAbstimmungBySpieler(spielerId);
    }

    public int getAbstimmungBySpieler(int spielerId) {
        return abstimmungDAO.getSpielIdBySpieler(spielerId);
    }

    // BewertungDAO-Methoden
    public List<Bewertung> getAlleBewertungen() {
        return bewertungDAO.getAlleBewertungen();
    }

    public Bewertung getBewertungenById(int id) {
        return bewertungDAO.getBewertungenById(id);
    }

    public Bewertung getBewertungByTerminUndSpieler(int terminId, int spielerId) {
        return bewertungDAO.getBewertungByTerminUndSpieler(terminId, spielerId);
    }

    public void updateBewertungen(int id, int terminId, int spielerId, int gastgeberSterne, String gastgeberKommentar, int essenSterne, String essenKommentar, int allgemeinSterne, String allgemeinKommentar) {
        bewertungDAO.updateBewertungen(id, terminId, spielerId, gastgeberSterne, gastgeberKommentar, essenSterne, essenKommentar, allgemeinSterne, allgemeinKommentar);
    }

    public void deleteBewertungen(int id) {
        bewertungDAO.deleteBewertungen(id);
    }

    public void speicherBewertung(int terminId, int spielerId, int gastgeberSterne, String gastgeberKommentar, int essenSterne, String essenKommentar, int allgemeinSterne, String allgemeinKommentar) { // ersetzt CRUD insert
        bewertungDAO.insertBewertung(terminId, spielerId, gastgeberSterne, gastgeberKommentar, essenSterne, essenKommentar, allgemeinSterne, allgemeinKommentar);
    }

    public float getSchnittGastgeberBewertung(int terminId) {
        return bewertungDAO.getSchnittGastgeberBewertung(terminId);
    }

    public float getSchnittEssenBewertung(int terminId) {
        return bewertungDAO.getSchnittEssenBewertung(terminId);
    }

    public float getSchnittAllgemeinBewertung(int terminId) {
        return bewertungDAO.getSchnittAllgemeinBewertung(terminId);
    }

    public List<String> getGastgeberKommentare(int terminId) {
        return bewertungDAO.getGastgeberKommentare(terminId);
    }

    public List<String> getEssenKommentare(int terminId) {
        return bewertungDAO.getEssenKommentare(terminId);
    }

    public List<String> getAllgemeinKommentare(int terminId) {
        return bewertungDAO.getAllgemeinKommentare(terminId);
    }

    // ChatnachrichtDAO-Methoden
    public List<Chatnachricht> getAlleChatnachrichten() {
        return chatnachrichtDAO.getAlleChatnachrichten();
    }

    public Chatnachricht getChatnachrichtenById(int id) {
        return chatnachrichtDAO.getChatnachrichtenById(id);
    }

    public void updateChatnachrichten(int id, int spielerId, String text, String zeitpunkt) {
        chatnachrichtDAO.updateChatnachrichten(id, spielerId, text, zeitpunkt);
    }

    public void deleteChatnachrichten(int id) {
        chatnachrichtDAO.deleteChatnachrichten(id);
    }

    public void sendeChatnachricht(int spielerId, String text, String zeitpunkt) { // ersetzt CRUD insert
        chatnachrichtDAO.insertChatnachrichten(spielerId, text, zeitpunkt);
    }

    // gemischte Funktionen
    public List<Spieler> getTeilnehmerByTerminId(int terminId) {
        List<Teilnahme> teilnahmen = teilnahmeDAO.getTeilnahmeByTerminId(terminId);
        List<Spieler> spielerListe = new ArrayList<>();

        for (Teilnahme t : teilnahmen) {
            if (t.getTeilnahme() == 1) {
                spielerListe.add(spielerDAO.getSpielerById(t.getSpielerId()));
            }
        }

        return spielerListe;
    }

    public Spiel getGewinnerSpiel(int terminId) {
        List<Spiel> spiele = spielDAO.getSpieleByTermin(terminId);

        Spiel gewinner = null;
        int maxStimmen = 0;

        for (Spiel spiel : spiele) {
            int stimmen = abstimmungDAO.getStimmenAnzahl(spiel.getId());

            if (stimmen > maxStimmen) {
                maxStimmen = stimmen;
                gewinner = spiel;
            }
        }

        return gewinner;
    }

    public boolean hatTerminBewertet(int terminId, int spielerId) {
        return bewertungDAO.existiertBewertung(terminId, spielerId);
    }
}
