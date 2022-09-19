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

public class AboutUsFragment extends Fragment implements IOnBackPressed {

    private MainActivity mainActivity;

    private Button buttonHome;

    private View view;

    public AboutUsFragment(MainActivity mainActivity) {
        super(R.layout.fragment_aboutus);
        this.mainActivity = mainActivity;
    }

    public static AboutUsFragment newInstance(MainActivity mainActivity) {
        AboutUsFragment fragment = new AboutUsFragment(mainActivity);
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

    @Override
    public void goBackToMainFragment() {
        mainActivity.replaceFragment(new MainFragment(mainActivity));
    }
}
