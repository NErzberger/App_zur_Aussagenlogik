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
 * Das Fragment <b>ZweiFormelFragment</b> erbt von der Klasse {@link Fragment} und implementiert das Interface {@link IOnBackPressed}.
 * Es wird aus dem Fragment {@link MainFragment} heraus aufgerufen, wenn sich das MainFragment im Modus <b>Formel</b> befindet
 * und der Parser entweder kein Fehler zurück gegeben hat oder die Fehlerkcodes -20 oder -30 geworfen hat.
 * @author Nico Erzberger
 * @author Daniel Miller
 * @author Laura Mayer
 * @version 1.0
 */
public class ZweiFormelFragment extends Fragment implements IOnBackPressed {

    /**
     * Für den Kontext ist die mainActivity der Klasse {@link MainActivity} wichtig.
     */
    private MainActivity mainActivity;

    /**
     * Mittels dem HomeButton ist es dem User möglich, in das MainFragment zurück zu wechseln.
     */
    private Button homeButton;

    /**
     * Die Wertetabelle wird in einer Tabelle des Typs {@link TableLayout} dargestellt.
     */
    private TableLayout truthTable;

    /**
     * Das Ergebnis der Wertetabelle wird im TextView textResult dargestellt.
     */
    private TextView textResult;

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
     * der Modus mitgegeben werden.
     */
    private History history;

    /**
     * Parametrisierter Konstrukter der Klasse ZweiFormelFragment.
     * @param mainActivity Übergabeparameter der Klasse {@link MainActivity}.
     * @param fehlercode Übergabeparameter des Typs int.
     * @param history Übergabeparameter der Klasse {@link History}.
     */
    public ZweiFormelFragment(MainActivity mainActivity, int fehlercode, History history) {
        this.mainActivity = mainActivity;
        this.fehlercode = fehlercode;
        this.history = history;
    }

    /**
     * Parametrisierter Konstrukter der Klasse ZweiFormelFragment.
     * @param mainActivity Übergabeparameter der Klasse {@link MainActivity}.
     * @param truthTableByInt Übergabeparameter als zweidimensionales int Array.
     * @param variables Liste mit Character - Generics
     * @param history Übergabeparameter der Klasse {@link History}.
     */
    public ZweiFormelFragment(MainActivity mainActivity, int[][] truthTableByInt, ArrayList<Character> variables, History history) {
        this.mainActivity = mainActivity;
        this.truthTableByInt = truthTableByInt;
        this.variables = variables;
        this.history = history;
    }

    /**
     * Parametrisierter Konstrukter der Klasse ZweiFormelFragment.
     * @param mainActivity Übergabeparameter der Klasse {@link MainActivity}.
     * @param truthTableByInt Übergabeparameter als zweidimensionales int Array.
     * @param variables Liste mit Character - Generics
     * @param fehlercode Übergabeparameter des Typs int.
     * @param history Übergabeparameter der Klasse {@link History}.
     */
    public ZweiFormelFragment(MainActivity mainActivity, int[][] truthTableByInt, ArrayList<Character> variables, int fehlercode, History history) {
        this.mainActivity = mainActivity;
        this.truthTableByInt = truthTableByInt;
        this.variables = variables;
        this.fehlercode = fehlercode;
        this.history = history;
    }

    /**
     * Parametrisierter Konstrukter der Klasse ZweiFormelFragment.
     * @param mainActivity Übergabeparameter der Klasse {@link MainActivity}.
     * @param truthTableByInt Übergabeparameter als zweidimensionales int Array.
     * @param fehlercode Übergabeparameter des Typs int.
     * @param history Übergabeparameter der Klasse {@link History}.
     */
    public ZweiFormelFragment(MainActivity mainActivity, int[][] truthTableByInt, int fehlercode, History history) {
        this.mainActivity = mainActivity;
        this.truthTableByInt = truthTableByInt;
        this.fehlercode = fehlercode;
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
     * Zunächst wird die View XML Ressource fragment_zwei_formel geladen und der MainActivty das aktuelle Fragment gemeldet.
     * Daraufhin wird der HomeButton erstellt.
     * Abhängig des Fehlercodes wird die TextView textResult mit Texten belegt:
     * <ul>
     *     <li>Kein Fehlercode: 'Die Formeln stimmen überein'</li>
     *     <li>-10: 'Golden bug' <k>(Sollte nicht mehr erscheinen)</k></li>
     *     <li>-20: 'Die Formeln stimmen nicht überein.'</li>
     *     <li>-30: 'Die Variablen der Formel stimmen nicht überein.'</li>
     * </ul>
     * <br>
     * Liegt entweder kein Fehlercode oder der Fehlercode -20 vor, so wird eine Wertetabelle aufgebaut:
     * Zunächst wird der TableHead aufgebaut, indem die Variablen gelesen und als Tabellen Überschriften geschrieben werden
     * und anschließend F1 und F2 geschreiben wird. Daraufhin wird zeilenweise der Tabellenrumpf aufgebaut. Hierfür wird für
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_zwei_formel, container, false);
        mainActivity.setActiveFragment(this);
        homeButton = view.findViewById(R.id.buttonHome);

        // ClickListener für den Home Button
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBackToMainFragment();
            }
        });

        // Wennd der Fehlercode 0 oder -20 ist, soll die truthTable gemacht werden
        if(fehlercode == 0 || fehlercode == -20){
            this.truthTable = view.findViewById(R.id.truthTable);
        }
        this.textResult = view.findViewById(R.id.textResult);

        /*
        * Text von textResult setzten
        *
         */
        if(fehlercode==-20){
            this.textResult.setText("Die Formeln stimmen nicht überein.");
            this.textResult.setTextSize(20);
        }else if(fehlercode==-10){
            this.textResult.setText("Golden bug!");
            this.textResult.setTextSize(20);
        }else if(fehlercode==-30){
            this.textResult.setText("Die Variablen der Formeln stimmen nicht überein.");
            this.textResult.setTextSize(20);
        }else{
            this.textResult.setText("Die Formeln stimmen überein.");
            this.textResult.setTextSize(20);
        }

        // Tabelle nur aufbauen, wenn Fehlercode == 0 oder -20 ist
        if(fehlercode == 0 || fehlercode == -20) {

            // Tabellenüberschrift machen
            TableRow header = new TableRow(truthTable.getContext());
            for (int i = 0; i < variables.size() + 2; i++) {
                TextView textView = new TextView(header.getContext());

                if (i < variables.size()) {
                    textView.setText(" " + Character.toString(variables.get(i)) + " ");
                } else if (i == variables.size()) {
                    textView.setText("F1");
                } else if (i == variables.size() + 1) {
                    textView.setText("F2");
                }

                textView.setTextSize(25);
                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                //textView.setPadding(10, 5, 10, 5);
                textView.setBackground(ContextCompat.getDrawable(truthTable.getContext(), R.drawable.table_border));
                header.addView(textView);
            }
            truthTable.addView(header, 0);

            // Tabellenrumpf zeilenweise Aufbauen
            for (int i = 0; i < truthTableByInt[0].length; i++) {
                TableRow tableRow = new TableRow(truthTable.getContext());
                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                tableRow.setLayoutParams(layoutParams);

                for (int j = 0; j < truthTableByInt.length; j++) {
                    // Für jedes Zeichen eine TextView erstellen
                    TextView textView = new TextView(tableRow.getContext());
                    textView.setText(Integer.toString(truthTableByInt[j][i]));
                    textView.setTextSize(25);
                    textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    //textView.setPadding(10, 5, 10, 5);
                    textView.setBackground(ContextCompat.getDrawable(truthTable.getContext(), R.drawable.table_border));
                    tableRow.addView(textView);
                }
                truthTable.addView(tableRow, i + 1);
            }
        }
        return view;
    }

    /**
     * Die Methode goBackToMainFragment wird aufgerufen, um ins MainFragment zurück zu wechseln. Es wird dabei das History
     * Element mitgegeben, um die Formeln und den Modus direkt mit anzugeben.
     */
    @Override
    public void goBackToMainFragment() {
        mainActivity.replaceFragment(new MainFragment(mainActivity, history));
    }
}