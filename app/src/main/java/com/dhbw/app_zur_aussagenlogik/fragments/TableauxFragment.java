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
 * A simple {@link Fragment} subclass.
 * Use the {@link TableauxFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TableauxFragment extends Fragment implements IOnBackPressed {

    private MainActivity mainActivity;

    private View view;

    private Button homeButton;

    public TableauxFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Tableaux.
     */
    // TODO: Rename and change types and number of parameters
    public static TableauxFragment newInstance(MainActivity mainActivity) {
        TableauxFragment fragment = new TableauxFragment(mainActivity);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tableaux, container, false);
        mainActivity.setActiveFragment(this);
        homeButton = view.findViewById(R.id.buttonHome);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBackToMainFragment();
            }
        });

        return view;
    }

    @Override
    public void goBackToMainFragment() {
        mainActivity.replaceFragment(new MainFragment(mainActivity));
    }
}