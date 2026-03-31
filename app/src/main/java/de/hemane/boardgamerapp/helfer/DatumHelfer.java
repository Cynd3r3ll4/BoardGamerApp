package de.hemane.boardgamerapp.helfer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DatumHelfer {

    // SQLite bietet kein Datumsformat, somit ist die einfachste Lösung (besser lesbar als ein UNIX-Format) ein String, der jedoch umgewandelt werden muss, um mit einem Datum verglichen werden zu können

    public static LocalDateTime parseDatum(String datumString) {

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm 'Uhr'");
            return LocalDateTime.parse(datumString, formatter);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean istZweiTagesFristAbgelaufen(String datumString) {

        LocalDateTime terminDatum = parseDatum(datumString);

        if (terminDatum == null){ // wenn nicht umwandelbar → Frist "abgelaufen"
            return true;
        }

        LocalDateTime jetzt = LocalDateTime.now();

        LocalDateTime frist = terminDatum.minusDays(2); // Frist berechnen (Termin - 2 Tage)

        return jetzt.isAfter(frist); // true = Frist abgelaufen, false = Frist noch nicht abgelaufen
    }

    public static boolean istZweiTageNachTermin(String datumString) {
        LocalDateTime terminDatum = parseDatum(datumString);

        if (terminDatum == null) {
            return true;
        }

        LocalDateTime jetzt = LocalDateTime.now();

        LocalDateTime fristEnde = terminDatum.plusDays(2); // Frist berechnen (Termin + 2 Tage)


        return jetzt.isAfter(fristEnde); // true = Frist abgelaufen, false = Frist noch nicht abgelaufen
    }
}
