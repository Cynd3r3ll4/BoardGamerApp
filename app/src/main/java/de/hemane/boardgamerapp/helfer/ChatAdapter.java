package de.hemane.boardgamerapp.helfer;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import de.hemane.boardgamerapp.R;
import de.hemane.boardgamerapp.controller.APIServer;
import de.hemane.boardgamerapp.klassen.Chatnachricht;
import de.hemane.boardgamerapp.klassen.Spieler;

public class ChatAdapter extends ArrayAdapter<Chatnachricht> {

    private int aktuellerSpielerId;
    private APIServer apiServer;

    public ChatAdapter(Context context, List<Chatnachricht> liste, int spielerId) {
        super(context, 0, liste);
        this.aktuellerSpielerId = spielerId;
        this.apiServer = new APIServer(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Chatnachricht nachricht = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.einzelne_chatnachricht, parent, false);
        }

        TextView textNachricht = convertView.findViewById(R.id.textNachricht);
        TextView textMeta = convertView.findViewById(R.id.textMetadaten);

        Spieler spieler = apiServer.getSpielerById(nachricht.getSpielerId());
        String name = (spieler != null) ? spieler.getName() : getContext().getString(R.string.unbekannt);

        textNachricht.setText(nachricht.getText());
        textMeta.setText(name + " • " + nachricht.getZeitpunkt());

        // 👉 LINKS / RECHTS LOGIK
        LinearLayout layout = (LinearLayout) convertView;

        if (nachricht.getSpielerId() == aktuellerSpielerId) {
            layout.setGravity(Gravity.END); // schiebt Text-Bubble nach rechts
            textNachricht.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);
        } else {
            layout.setGravity(Gravity.START); // schiebt Text-Bubble nach links
            textNachricht.setBackgroundResource(android.R.drawable.dialog_holo_dark_frame);
        }

        return convertView;
    }
}
