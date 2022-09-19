package com.dhbw.app_zur_aussagenlogik.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.dhbw.app_zur_aussagenlogik.MainActivity;
import com.dhbw.app_zur_aussagenlogik.R;
import com.dhbw.app_zur_aussagenlogik.interfaces.IOnBackPressed;

/**
 * Das Fragment AboutUs dient als Impressum in der App und soll die Entwickler als auch den Zweck der App vorstellen.
 * Das Fragment erbt von der parent Klasse {@link Fragment} und implementiert das Interface {@link IOnBackPressed},
 * um den Home-Button zu implementieren.
 */
public class AboutUsFragment extends Fragment implements IOnBackPressed {

    /**
     * Das Klassenattribut mainActivity der Klasse {@link MainActivity} ist notewendig,
     * um auf den Kontext zugreifen zu können.
     */
    private MainActivity mainActivity;

    /**
     * Der Home Button wird verwendet, um auf das mainFragment zurück zu kommen.
     */
    private Button buttonHome;

    /**
     * Die view beinhaltet alle grafischen Komponenten.
     */
    private View view;

    /**
     * Der Konstruktor benötigt zwangsweise ein Objekt der Klasse {@link MainActivity}, welches dem
     * Klassenattribut zugewiesen wird. Darüber hinaus wird der super Konstruktor aufgerufen.
     * @param mainActivity
     */
    public AboutUsFragment(MainActivity mainActivity) {
        super(R.layout.fragment_aboutus);
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
     * Die Methode onCreateView benötigt für die Funktionalität ein Objekt der Klasse {@link LayoutInflater},
     * ein Objekt der Klasse {@link ViewGroup} und eins der Klasse {@link Bundle}.
     * Es wird über den inflater das XML Layout fragment_aboutus geladen und dem Klassenattribut view zugewiesen.
     * Daraufhin wird der MainActivity das jetzt aufbauende Fragment als aktuelles Fragment mitgegeben.
     * Darauf wird der HomeButton erzeugt und mit einem OnClickListener ausgestattet, der die Methode des Interfaces verwendet.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return Es wird die view zurückgegegeben.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_aboutus, container, false);
        mainActivity.setActiveFragment(this);
        this.buttonHome = view.findViewById(R.id.buttonHome);
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               goBackToMainFragment();
            }
        });
        return view;
    }

    /**
     * Die Methode goBackToMainFragment ist die Implementierung des Interfaces {@link IOnBackPressed}.
     * Es wird die Methode der mainActivity aufgerufen, um das Fragment zum mainFragment zu wechseln.
     */
    @Override
    public void goBackToMainFragment() {
        mainActivity.replaceFragment(new MainFragment(mainActivity));
    }
}
