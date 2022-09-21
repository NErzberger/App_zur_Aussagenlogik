package com.dhbw.app_zur_aussagenlogik.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dhbw.app_zur_aussagenlogik.MainActivity;
import com.dhbw.app_zur_aussagenlogik.Modi;
import com.dhbw.app_zur_aussagenlogik.R;
import com.dhbw.app_zur_aussagenlogik.core.Formel;
import com.dhbw.app_zur_aussagenlogik.core.Parser;
import com.dhbw.app_zur_aussagenlogik.interfaces.IOnBackPressed;
import com.dhbw.app_zur_aussagenlogik.sql.dataObjects.History;

import java.util.ArrayList;
import java.util.List;

/**
 * Das <b>NormalformFragment</b> dient zur Darstellung des Rechenwegs zu einer Normalform.
 * Die Klasse erbt von der Klasse {@link Fragment} und implementiert das Interface IOnBackPressed.
 * Es wird vom MainFragment aus aufgerufen, wenn auf den den Button buttonRechenweg geklickt wird.
 * @author Nico Erzberger
 * @author Daniel Miller
 * @author Laura Mayer
 * @version 1.0
 */
public class NormalformFragment extends Fragment implements IOnBackPressed {

    /**
     * Um auf den Kontext zugreifen zu können, ist die mainActivity als Klassendiagramm notwendig.
     */
    private final MainActivity mainActivity;

    /**
     * Um festzustellen, ob es sich um eine KNF oder DNF handelt, ist der Modi notwendig.
     * Hierzu wird das Attribut als Klassenatribut deklariet.
     */
    private Modi modi;

    /**
     * Um einen Rechenweg darzustellen, wird der rechenweg als Klassenattribut vom Typ List mit dem
     * Generic {@link Formel} deklariert.
     */
    private List<Formel> rechenweg;

    /**
     * Um auf das MainFragment zurückwechseln zu können und dort die mitegebene Formel darstellen zu können,
     * wird ein historischen Element als Klassenattribut gespeichert.
     */
    private final History historyElement;

    /**
     * Um ein Objekt der Klasse NormalformFragment erstellen zu können, ist ein Objekt der {@link MainActivity} und ein
     * historisches Element {@link History} erforderlich.
     * @param mainActivity Übergabeparameter der Klasse {@link MainActivity}
     * @param historyElement Übergabeparameter der Klasse {@link History}
     */
    public NormalformFragment(MainActivity mainActivity, History historyElement) {
        super(R.layout.fragment_normalform);
        this.mainActivity = mainActivity;
        this.historyElement = historyElement;
    }

    /**
     * Die Methode onCreate holt eine Instanz des Parsers und erstellt die Liste rechenweg und schreibt den Rechenweg des Parsers in die erstellte Liste.
     * Daraufhin wird der Modus des Parsers übernommen.
     * @param savedInstanceState Übergabeparameter der Klasse {@link Bundle}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Parser p = Parser.getInstance();
        rechenweg = new ArrayList<>();
        rechenweg = p.getRechenweg();
        modi = p.getModus();
    }

    /**
     * Die Methode onCreateView lädt die XML View Ressource fragment_normalform. Daraufhin wird der Button homeButton erstellt und mit einem OnClickListener
     * ausgestattet. Es werden daraufhin die TextViews aus der XML Resscourcen View geladen und mit den Formel der Liste belegt.
     * <table border="3" style="border-collapse: collapse;">
     *     <tr>
     *         <th>TextView</th>
     *         <th>ListenIndex</th>
     *     </tr>
     *     <tr>
     *         <td>pfeileErgebnis</td>
     *         <td>0</td>
     *     </tr>
     *     <tr>
     *         <td>deMorganErgebnis</td>
     *         <td>1</td>
     *     </tr>
     *     <tr>
     *         <td>normalformErgebnis</td>
     *         <td>2</td>
     *     </tr>
     *     <tr>
     *         <td>orginalformel</td>
     *         <td>3</td>
     *     </tr>
     * </table>
     * @param inflater Übergabeparameter der Klasse {@link LayoutInflater}
     * @param container Übergabeparameter der Klasse {@link ViewGroup}
     * @param savedInstanceState Übergabeparameter der Klasse {@link Bundle}
     * @return Es wird eine View zurückgegeben.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_normalform, container, false);
        mainActivity.setActiveFragment(this);
        Button homeButton = view.findViewById(R.id.buttonHome);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBackToMainFragment();
            }
        });

        TextView pfeileErgebnis = view.findViewById(R.id.pfeileErgebnis);
        TextView deMorganErgebnis = view.findViewById(R.id.deMorganErgebnis);
        TextView normalformErgebnis = view.findViewById(R.id.normalformErgebnis);
        TextView normalformText = view.findViewById(R.id.normalformText);
        TextView orginalformel = view.findViewById(R.id.orginalformel);

        if(modi == Modi.DNF){
            normalformText.setText("DNF");
        }else if(modi == Modi.KNF){
            normalformText.setText("KNF");
        }

        /*
         * Rechenweg 0 = Orginale Formel
         * Rechenweg 1 = Pfeile Auflösen Ergebnis
         * Rechenweg 2 = DeMorgan Ergebnis
         * Rechenweg 3 = Normalform
         */
        orginalformel.setText(rechenweg.get(0).toString());
        pfeileErgebnis.setText(rechenweg.get(1).toString());
        deMorganErgebnis.setText(rechenweg.get(2).toString());
        normalformErgebnis.setText(rechenweg.get(3).toString());

        return view;
    }

    /**
     * Implementierung der Mehtode goBackToMainFragment. Es wird eine replace Aktion durchgeführt und
     * über die mainActivity zum mainFragment zurück gewechselt.
     */
    @Override
    public void goBackToMainFragment() {
        mainActivity.replaceFragment(new MainFragment(mainActivity, historyElement));
    }
}