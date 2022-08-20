package com.dhbw.app_zur_aussagenlogik.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dhbw.app_zur_aussagenlogik.MainActivity;
import com.dhbw.app_zur_aussagenlogik.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NormalformFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NormalformFragment extends Fragment {


    private MainActivity mainActivity;

    private View view;

    private Button homeButton;

    public NormalformFragment(MainActivity mainActivity) {
        super(R.layout.fragment_normalform);
        this.mainActivity = mainActivity;
    }


    // TODO: Rename and change types and number of parameters
    public static NormalformFragment newInstance(MainActivity mainActivity) {
        NormalformFragment fragment = new NormalformFragment(mainActivity);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_normalform, container, false);

        homeButton = view.findViewById(R.id.buttonHome);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.replaceFragment(new MainFragment(mainActivity));
            }
        });

        return view;
    }
}