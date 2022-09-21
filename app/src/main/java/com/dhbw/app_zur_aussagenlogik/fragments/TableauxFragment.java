package com.dhbw.app_zur_aussagenlogik.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dhbw.app_zur_aussagenlogik.MainActivity;
import com.dhbw.app_zur_aussagenlogik.R;
import com.dhbw.app_zur_aussagenlogik.interfaces.IOnBackPressed;

/**
 * Das Fragment <b>TableauxFragment</b> stellt das Tableaux Verfahren dar.
 * Es erbt von der Klasse {@link Fragment} und implementiert das Interface {@link IOnBackPressed}.
 * Das Fragment ist nicht in Verwendung.
 * @author Nico Erzberger
 * @author Daniel Miller
 * @author Laura Mayer
 * @version 1.0
 * @deprecated Das Fragment ist nicht in Verwendung
 */
public class TableauxFragment extends Fragment implements IOnBackPressed {

    /**
     * Um auf den Kontext zugreifen zu können ist die mainActivity als Klassenattribut notwendig.
     */
    private final MainActivity mainActivity;

    /**
     * Um eine Objekt erzeugen zu können, ist ein Objekt der Klasse {@link MainActivity} notwendig.
     * @param mainActivity Übergabeparameter der Klasse {@link MainActivity}
     */
    public TableauxFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
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
     * Die Methode onCreateView lädt die XML View Ressource fragment_tableaux und erstellt den homeButton und stattet diesen mit einem OnClickListener aus.
     * @param inflater Übergabeparameter der Klasse {@link LayoutInflater}
     * @param container Übergabeparameter der Klasse {@link ViewGroup}
     * @param savedInstanceState Übergabeparameter der Klasse {@link Bundle}
     * @return Es wird eine View zurückgegeben.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tableaux, container, false);
        mainActivity.setActiveFragment(this);
        Button homeButton = view.findViewById(R.id.buttonHome);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBackToMainFragment();
            }
        });

        return view;
    }
    /**
     * Implementierung der Mehtode goBackToMainFragment. Es wird eine replace Aktion durchgeführt und
     * über die mainActivity zum mainFragment zurück gewechselt.
     */
    @Override
    public void goBackToMainFragment() {
        mainActivity.replaceFragment(new MainFragment(mainActivity));
    }
}