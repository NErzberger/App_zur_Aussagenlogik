package com.dhbw.app_zur_aussagenlogik.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.dhbw.app_zur_aussagenlogik.MainActivity;
import com.dhbw.app_zur_aussagenlogik.R;

public class AboutUsFragment extends Fragment {

    private MainActivity mainActivity;

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
}
