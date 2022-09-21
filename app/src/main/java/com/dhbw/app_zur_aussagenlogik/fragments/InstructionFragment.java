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
 * Das Fragment <b>InstructionFragment</b> dient zur Darstellung der Anleitung der App. Das Fragment erbt von der Klasse {@link Fragment}
 * und implementiert das Interface {@link IOnBackPressed}, um zurück ins MainFragment wechseln zu können.
 * Das Fragment wird aus dem Menü der MainActivity heraus aufgerufen.
 * @author Nico Erzberger
 * @author Daniel Miller
 * @version 1.0
 */
public class InstructionFragment extends Fragment implements IOnBackPressed {

    /**
     * Für den Konstext ist die MainActivity als Klassenattribut notwendig.
     */
    private final MainActivity mainActivity;

    /**
     * Der Konstruktor benötigt zur Erstellung eines Objektes ein Objekt der Klasse {@link MainActivity}
     * @param mainActivity Übergabeparameter der Klasse {@link MainActivity}.
     */
    public InstructionFragment(MainActivity mainActivity) {
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
     * Die Methode onCreateView lädt die XML View Ressource fragment_instruction und gibt dem buttonHome einen OnClickListener mit.
     * @param inflater Übergabeparameter der Klasse LayoutInflater
     * @param container Übergabeparameter der Klasse ViewGroup
     * @param savedInstanceState Übergabeparameter der Klasse Bundle
     * @return Die Methode gibt eine View zurück.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_instruction, container, false);
        mainActivity.setActiveFragment(this);
        Button buttonHome = view.findViewById(R.id.buttonHome);
        buttonHome.setOnClickListener(new View.OnClickListener() {
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