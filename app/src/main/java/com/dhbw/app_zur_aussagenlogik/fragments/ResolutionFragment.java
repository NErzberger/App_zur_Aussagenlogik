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
 * Use the {@link ResolutionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResolutionFragment extends Fragment implements IOnBackPressed {


    private MainActivity mainActivity;

    private View view;

    private Button homeButton;

    public ResolutionFragment(MainActivity mainActivity) {
       super(R.layout.fragment_resolution);
       this.mainActivity = mainActivity;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment Resolution.
     */
    public static ResolutionFragment newInstance(MainActivity mainActivity) {
        ResolutionFragment fragment = new ResolutionFragment(mainActivity);
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
        view =  inflater.inflate(R.layout.fragment_resolution, container, false);
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