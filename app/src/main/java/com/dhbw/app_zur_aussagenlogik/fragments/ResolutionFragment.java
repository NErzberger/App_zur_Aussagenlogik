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
 * Das <b>ResolutionFragment</b> dient zum visualisieren der Rechenschritte der Resolution und der Schritte des Resolutionsverfahren.
 * Die Klasse erbt von der Klasse {@link Fragment} und implementiert das Interface {@link IOnBackPressed}.
 * Das Fragment wird vom MainFragment und im Modus <i>Resolution</i> aufgerufen.
 * @author Nico Erzberger
 * @author Daniel Miller
 * @version 1.0
 */
public class ResolutionFragment extends Fragment implements IOnBackPressed {

    /**
     * Um auf den Kontext zugreifen zu können, wird die mainActivity als Klassendiagramm benötigt.
     */
    private final MainActivity mainActivity;

    /**
     * Um ein Objekt der Klasse {@link ResolutionFragment} erstellen zu können, ist ein Objekt der Klasse
     * MainFragment erforderlich.
     * @param mainActivity Übergabeparameter der Klasse {@link MainActivity}
     */
    public ResolutionFragment(MainActivity mainActivity) {
       super(R.layout.fragment_resolution);
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
     * Die Methode onCreateView lädt die XML View Ressource fragment_resolution und erstellt den homeButton und belegt diesen mit einem OnClickListener.
     * @param inflater Übergabeparameter der Klasse {@link LayoutInflater}
     * @param container Übergabeparameter der Klasse {@link ViewGroup}
     * @param savedInstanceState Übergabeparameter der Klasse {@link Bundle}
     * @return Es wird eine View zurückgegeben.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_resolution, container, false);
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