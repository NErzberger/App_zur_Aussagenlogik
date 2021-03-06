package com.dhbw.app_zur_aussagenlogik;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.dhbw.app_zur_aussagenlogik.fragments.MainFragment;

public class MainActivity extends AppCompatActivity {

    public FragmentManager fragmentManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, new MainFragment(this))
                .setReorderingAllowed(true)
                .commit();
    }

    public void replaceFragment(Fragment fragment){
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }
}