package de.hemane.boardgamerapp.helfer;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class DatumHelfer {

    public static LocalDateTime parseDatum(String datumString) { // um aus Datums-String richtiges Datum zu machen, static → kein Objekt nötig

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm 'Uhr'");
            return LocalDateTime.parse(datumString, formatter);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean istZweiTagesFristAbgelaufen(String datumString) { // boolean, ob frist abgelaufen

        LocalDateTime terminDatum = parseDatum(datumString); // String zu Datum

        if (terminDatum == null){ // wenn nicht umwandelbar → Frist "abgelaufen"
            return true;
        }

        LocalDateTime jetzt = LocalDateTime.now();

        LocalDateTime frist = terminDatum.minusDays(2); // Frist berechnen (Termin - 2 Tage)

        return jetzt.isAfter(frist); // Vgl.: jetzt & Frist, true wenn nach Frist, false, wenn davor true
    }

    public static boolean istZweiTageNachTermin(String datumString) {
        LocalDateTime terminDatum = parseDatum(datumString);

        if (terminDatum == null) {
            return true;
        }

        LocalDateTime jetzt = LocalDateTime.now();

        LocalDateTime fristEnde = terminDatum.plusDays(2); // 2 Tage nach Termin

        return jetzt.isAfter(fristEnde); // Vgl.: jetzt & Frist, true wenn nach Frist, false, wenn davor true
    }
}
