package com.dhbw.app_zur_aussagenlogik;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.dhbw.app_zur_aussagenlogik.core.ErrorHandler;
import com.dhbw.app_zur_aussagenlogik.fragments.AboutUsFragment;
import com.dhbw.app_zur_aussagenlogik.fragments.HistoryFragment;
import com.dhbw.app_zur_aussagenlogik.fragments.InstructionFragment;
import com.dhbw.app_zur_aussagenlogik.fragments.MainFragment;
import com.dhbw.app_zur_aussagenlogik.fragments.IOnBackPressed;
import com.dhbw.app_zur_aussagenlogik.sql.dbHelper.HistoryDataSource;

public class MainActivity extends AppCompatActivity {

    public FragmentManager fragmentManager;

    private MainFragment mainFragment = new MainFragment(this);

    private IOnBackPressed activeFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ErrorHandler.newInstance();
        HistoryDataSource dataSource = new HistoryDataSource(this);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, mainFragment)
                .setReorderingAllowed(true)
                .commit();
    }

    public void replaceFragment(Fragment fragment){
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.bottom_nav_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch (item.getItemId()){
            case R.id.UeberUns:
                replaceFragment(new AboutUsFragment(this));
                return true;
            case R.id.history:
                replaceFragment(new HistoryFragment(this));
                return true;
            case R.id.anleitung:
                replaceFragment(new InstructionFragment(this));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed(){
        Log.d(MainActivity.class.getSimpleName(), "Es wurde die Zurücktaste gedrückt");
        if(activeFragment instanceof MainFragment){
            System.exit(0);
        }else {
            activeFragment.goBackToMainFragment();
        }
    }

    public MainFragment getMainFragment(){
        return this.mainFragment;
    }

    public IOnBackPressed getActiveFragment() {
        return activeFragment;
    }

    public void setActiveFragment(IOnBackPressed activeFragment) {
        this.activeFragment = activeFragment;
    }
}