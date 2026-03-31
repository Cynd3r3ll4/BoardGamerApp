package de.hemane.boardgamerapp.helfer;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

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
        LinearLayout layout = (LinearLayout) convertView;

        Spieler spieler = apiServer.getSpielerById(nachricht.getSpielerId());
        String name = (spieler != null) ? spieler.getName() : getContext().getString(R.string.unbekannt);

        textNachricht.setText(nachricht.getText());
        textMeta.setText(name + " • " + nachricht.getZeitpunkt());

        // Theme-Farben für Textkontrast laden
        TypedValue typedValue = new TypedValue();
        
        if (nachricht.getSpielerId() == aktuellerSpielerId) {
            // EIGENE NACHRICHT (Rechts)
            layout.setGravity(Gravity.END);
            textMeta.setGravity(Gravity.END);
            textNachricht.setBackgroundResource(R.drawable.bubble_sent);
            
            // Kontrastfarbe für Text auf PrimaryContainer (OnPrimaryContainer)
            getContext().getTheme().resolveAttribute(com.google.android.material.R.attr.colorOnPrimaryContainer, typedValue, true);
            textNachricht.setTextColor(typedValue.data);
            
        } else {
            // ANDERE NACHRICHT (Links)
            layout.setGravity(Gravity.START);
            textMeta.setGravity(Gravity.START);
            textNachricht.setBackgroundResource(R.drawable.bubble_received);
            
            // Kontrastfarbe für Text auf SurfaceVariant (OnSurfaceVariant)
            getContext().getTheme().resolveAttribute(com.google.android.material.R.attr.colorOnSurfaceVariant, typedValue, true);
            textNachricht.setTextColor(typedValue.data);
        }

        return convertView;
    }
}
