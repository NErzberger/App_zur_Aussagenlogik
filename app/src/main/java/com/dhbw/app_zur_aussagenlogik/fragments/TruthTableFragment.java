package com.dhbw.app_zur_aussagenlogik.fragments;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.dhbw.app_zur_aussagenlogik.MainActivity;
import com.dhbw.app_zur_aussagenlogik.R;
import com.dhbw.app_zur_aussagenlogik.interfaces.IOnBackPressed;
import com.dhbw.app_zur_aussagenlogik.sql.dataObjects.History;

import java.util.ArrayList;

/**
 * Das Fragment <b>TruthTableFragment</b> dient dazu, die Wahrheitstabellen der eingegebenen Formel darzustellen.
 * Die Klasse erbt von der Klasse {@link Fragment} und implementiert das Interface {@link IOnBackPressed}.
 * Das Fragment wird aus dem MainFragment im Modus <i>Wertetabelle</i> heraus aufgerufen.
 * @author Nico Erzberger
 * @author Daniel Miller
 * @version 1.0
 */
public class TruthTableFragment extends Fragment implements IOnBackPressed {

    /**
     * Um auf den Kontext zugreifen zu können, wird die mainActivity als Klassenattribut verwendet.
     */
    private final MainActivity mainActivity;

    /**
     * Es wird dem Fragment ZweiFromelFragment ein zweidimensionaler int Array mitgegeben, welcher in truthTableByInt geschrieben wird.
     */
    private int[][] truthTableByInt;

    /**
     * Um mögliche Fehlerfälle zu erkennen, wird ein fehlercode benötigt.
     */
    private int fehlercode = 0;

    /**
     * Zur Verarbeitung ist eine Liste mit Charactern notwendig.
     */
    private ArrayList<Character> variables;

    /**
     * Um korrekt in das MainFragment zurück zu wechseln ist ein History Element notwendig. Anhand von diesem können beide Formeln und
     *      * der Modus mitgegeben werden.
     */
    private History history;

    /**
     * Um ein Objekt des Fragments erstellen zu können, ist die MainActivity, ein zweidimensionales Integerarray, eine Liste mit Charactern und
     * ein historisches Element notwendig.
     * @param mainActivity Übergabeparameter der Klasse {@link MainActivity}.
     * @param truthTableByInt Übergabeparameter als zweidimensionales int Array.
     * @param variables Liste mit Character - Generics
     * @param history Übergabeparameter der Klasse {@link History}.
     */
    public TruthTableFragment(MainActivity mainActivity, int[][] truthTableByInt, ArrayList<Character> variables, History history) {
        this.mainActivity = mainActivity;
        this.truthTableByInt = truthTableByInt;
        this.variables = variables;
        this.history = history;
    }


    /**
     * Die Methode onCreate ruft lediglich die super Methode onCreate auf und gibt die
     * Übergabeparameter direkt weiter.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Die Methode onCreateView erzeugt die View des Fragments.
     * Zunächst wird die View XML Ressource fragment_truth_table geladen und der MainActivty das aktuelle Fragment gemeldet.
     * Daraufhin wird der HomeButton erstellt.
     * <br>
     * Zunächst wird der TableHead aufgebaut, indem die Variablen gelesen und als Tabellen Überschriften geschrieben werden
     * und anschließend F geschreiben wird. Daraufhin wird zeilenweise der Tabellenrumpf aufgebaut. Hierfür wird für
     * jeden Wert eine TextVeiw erzeugt und der Wert der Variable als Text geschrieben. Die Schriftgröße wird auf 25 gesetzt,
     * der Text zentriet und der Hintergrund auf Weiß gesetzt, um den Effekt eines schwarzen Rahmens zu erzeugen.
     * Daraufhin wird die TextView der view hinzugefügt.
     *
     * @param inflater Übergabeparameter der Klasse {@link LayoutInflater}
     * @param container Übergabeparameter der Klasse {@link ViewGroup}
     * @param savedInstanceState Übergabeparameter der Klasse {@link Bundle}
     * @return Es wird die view zurückgegeben.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_truth_table, container, false);
        mainActivity.setActiveFragment(this);
        Button homeButton = view.findViewById(R.id.buttonHome2);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBackToMainFragment();
            }
        });

        TableLayout truthTable = view.findViewById(R.id.truthTable);

        TableRow header = new TableRow(truthTable.getContext());
        for (int i = 0; i < variables.size() + 1; i++) {
            TextView textView = new TextView(header.getContext());

            if (i < variables.size()) {
                textView.setText(" " + Character.toString(variables.get(i)) + " ");
            } else if (i == variables.size()) {
                textView.setText(" " + "F" + " ");
            }

            textView.setTextSize(25);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textView.setBackground(ContextCompat.getDrawable(truthTable.getContext(), R.drawable.table_border));
            header.addView(textView);
        }
        truthTable.addView(header, 0);

        for (int i = 0; i < truthTableByInt[0].length; i++) {
            TableRow tableRow = new TableRow(truthTable.getContext());
            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            tableRow.setLayoutParams(layoutParams);

            for (int j = 0; j < truthTableByInt.length; j++) {
                TextView textView = new TextView(tableRow.getContext());
                textView.setText(Integer.toString(truthTableByInt[j][i]));
                textView.setTextSize(25);
                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                textView.setBackground(ContextCompat.getDrawable(truthTable.getContext(), R.drawable.table_border));
                tableRow.addView(textView);
            }
            truthTable.addView(tableRow, i + 1);
        }
        return view;
    }

    /**
     * Implementierung der Mehtode goBackToMainFragment. Es wird eine replace Aktion durchgeführt und
     * über die mainActivity zum mainFragment zurück gewechselt.
     */
    @Override
    public void goBackToMainFragment() {
        mainActivity.replaceFragment(new MainFragment(mainActivity, history));
    }
}