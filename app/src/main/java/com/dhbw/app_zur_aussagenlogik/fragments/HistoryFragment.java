package com.dhbw.app_zur_aussagenlogik.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dhbw.app_zur_aussagenlogik.MainActivity;
import com.dhbw.app_zur_aussagenlogik.R;
import com.dhbw.app_zur_aussagenlogik.interfaces.IOnBackPressed;
import com.dhbw.app_zur_aussagenlogik.sql.dataObjects.History;
import com.dhbw.app_zur_aussagenlogik.sql.dbHelper.HistoryDataSource;

import java.util.List;
import java.util.Objects;

/**
 * Das Fragment HistoryFragment wird dazu verwendt, um den Verlauf, welcher in der Datenbank gespeichert ist, zu visualiseren.
 * Es wird aus dem Menü aus der {@link MainActivity} heraus ausgerufen.
 * Das Fragment erbt von der Klasse {@link Fragment} und implementiert das Interface {@link IOnBackPressed},
 * um auf das {@link MainFragment} zurück zu wechseln.
 * @author Nico Erzberger
 * @author Laura Mayer
 * @version 1.0
 * @see Adapter_for_HistroyView
 */
public class HistoryFragment extends Fragment implements IOnBackPressed {

    /**
     * Das Klassenattribut mainActivity der Klasse {@link MainActivity} ist notewendig,
     * um auf den Kontext zugreifen zu können.
     */
    private final MainActivity mainActivity;

    /**
     * Der Verlauf wird in der grafischen Componente history_view der Klasse {@link RecyclerView} dargestellt.
     * Hierzu ist die Variable als Klassenattribut notwendig.
     * Es ist für die Liste erforderlich, dass ein Adapter mit eingebunden werden kann, um die Verbindung zwischen der GUI und dem Background zu steuern.
     * Der Adapter wird von der Klasse {@link Adapter_for_HistroyView} gestellt, welcher über die Klasse {@link HistoryDataSource}
     * mittels dem {@link com.dhbw.app_zur_aussagenlogik.sql.dbHelper.HistoryDbHelper} auf die Datenbank geht und die historischen Daten abfragt.
     */
    private RecyclerView history_view;

    /**
     * Für die Erstellung des Fragments ist lediglich die {@link MainActivity} erforderlich.
     * Es wird die View Ressource fragment_history geladen und dargestellt.
     * @param mainActivity Übergabeparameter der Klasse {@link MainActivity}
     */
    public HistoryFragment(MainActivity mainActivity) {
       super(R.layout.fragment_history);
       this.mainActivity = mainActivity;
    }

    /**
     * Die Methode onCreate ruft lediglich die super Methode onCreate auf und gibt die
     * Übergabeparameter direkt weiter.
     * @param savedInstanceState Übergabeparameter der Klasse {@link Bundle}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Die Methode onCreateView baut die View auf. Es wird zunüchst der Button homeButton erstellt und mit einem OnClickListerner ausgestattet.
     * Daraufhin werden alle historischen Daten geladen und eine Überschrift mit der id -1 erstellt. Die id -1 bedeutet, dass in dem weiteren
     * Aufbau eine Überschrift folgt. Der histrory_view wird ein LinearLayoutManager gesetzt. Daraufhin wird ein Adapter erstellt und dieser der history_view gesetzt.
     * Dieser übernimmt den Aufbau der View der Liste selbst und die Verbindung zwischen der GUI und dem Backend.
     * Schließlich wird ein OnClickListener auf den Adapter erstellt, um auf Klicks auf die Liste reagieren zu können.
     *
     * @param inflater Übergabeparameter der Klasse {@link LayoutInflater}
     * @param container Übergabeparameter der Klasse {@link ViewGroup}
     * @param savedInstanceState Übergabeparameter der Klase {@link Bundle}
     * @return Es wird eine View zurückgegeben.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_history, container, false);
        mainActivity.setActiveFragment(this);

        Button homeButton = view.findViewById(R.id.buttonHome);
        history_view = view.findViewById(R.id.List_History);

        homeButton.setOnClickListener(view1 -> goBackToMainFragment());

        // Objekt der dataSorce erzeugen und den Kontext mit geben
        HistoryDataSource dataSource = new HistoryDataSource(getContext());

        // Liste laden
        List<History> historyList = dataSource.getAllHistoryEntries();

        // Überschrift machen und der Liste hinzufügen
        historyList.add(0, new History(-1, "Modi", "Orginalformel", "Ergebnisformel/Zweitformel"));

        // Liste darstellen
        history_view.setLayoutManager(new LinearLayoutManager(getContext()));
        Adapter_for_HistroyView adapter = new Adapter_for_HistroyView(historyList);
        history_view.setAdapter(adapter);

        // onClick listener
        adapter.setOnItemClickListener((View itemView, int position) -> {
                Log.d(HistoryFragment.class.getSimpleName(), itemView.getId()+ " an der Stelle " + position + " wurde angeklickt.");
                Log.d(HistoryFragment.class.getSimpleName(), position + " hat die ID "+ Objects.requireNonNull(history_view.getAdapter()).getItemId(position));

                // neues historisches Element erzeugen
                TextView inputFormula = itemView.findViewById(R.id.firstFormula);
                TextView resultFormula = itemView.findViewById(R.id.secondFormula);
                TextView formulaId = itemView.findViewById(R.id.formulaId);
                TextView formulaModi = itemView.findViewById(R.id.formulaModi);
                History historyElement = new History(Integer.parseInt(formulaId.getText().toString()), formulaModi.getText().toString(), inputFormula.getText().toString(), resultFormula.getText().toString());

                // Zurück in das MainFragment wechseln
                mainActivity.replaceFragment(new MainFragment(mainActivity, historyElement));

            }
        );

        return view;
    }


    /**
     * Implementierung der Methode goBackToMainFragment. Es wird eine replace Aktion in der mainActivity ausgeführt und zum MainFragment zurückgewechselt.
     */
    @Override
    public void goBackToMainFragment() {
        mainActivity.replaceFragment(new MainFragment(mainActivity));
    }
}