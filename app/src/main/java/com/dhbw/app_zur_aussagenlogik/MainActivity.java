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
import com.dhbw.app_zur_aussagenlogik.interfaces.IOnBackPressed;
import com.dhbw.app_zur_aussagenlogik.sql.dbHelper.HistoryDataSource;

/**
 * Die Klass MainActivity dient als Container für alle Fragments vom Typ {@link IOnBackPressed}.
 * Der User bleibt ständig in dieser Activity und wechselt immer durch die Fragments.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Der FragmentManager ist für die Verwaltung der Fragments zuständig.
     * Alle Fragments hängen im Fragmentmanager drin und können über diesen geswitcht werden.
     */
    public FragmentManager fragmentManager;


    /**
     * Das mainFragment ist die erste Ansicht und Hauptfragment und wird als erstes geladen.
     */
    private MainFragment mainFragment = new MainFragment(this);

    /**
     * Das activeFragment hält das aktuelle Fragment und ist vom Typ {@link IOnBackPressed}.
     * Die Verwendung dieses Attributs ist für die Zurücktaste von Android wichtig.
     */
    private IOnBackPressed activeFragment;

    /**
     * In der onCreate Methode wird die View activity_main gesetzt und der {@link ErrorHandler} instantiiert.
     * Daraufhin wird der FragmentManager aufgebaut und das mainFragment als erste Standardansicht geöffnet.
     * @param savedInstanceState
     */
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

    /**
     * Mittels der Methode replaceFragment wird über die mainActivity das aktuelle Fragment gewechselt.
     * Dabei wird der FragmentManager verwendet, indem ein Fragment mitgegeben wird und das aktuelle
     * mit diesem ausgetauscht wird.
     * @param fragment
     */
    public void replaceFragment(Fragment fragment){
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    /**
     * Die Methode onCreateOptionsMenu wird beim Start der Activity aufgerufen. Sie lädt das Menü
     * aus der View bootom_nav_menu. Es wird der MenuInflater aufgerufen, welcher die View übergeben bekommt.
     * Anschließend wird die überschriebene Methode der Klasse {@link android.app.Activity} aufgerfen und das Menü mitgegeben.
     * @param menu Menü der Klasse {@link Menu}
     * @return gibt einen boolean zurück
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.bottom_nav_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Die Methode onOptionsItemSelected wird aufgerufen, wenn im Menü ein Item ausgewählt wird.
     * Es können die Menüpunkte
     * <ul>
     *     <li>{@link AboutUsFragment}</li>
     *     <li>{@link HistoryFragment}</li>
     *     <li>{@link InstructionFragment}</li>
     * </ul>
     * gewählt werden. Entsprechend der Auswahl werden die Fragments mittles dem FragmentManager gewechselt.
     * @param item
     * @return
     */
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

    /**
     * Die Methode onBackPressed wird aufgerufen, wenn der Android Zurück-Button gedrückt wird.
     * Es wird geprüft, welches Fragment gerade aktiv ist. Dabei wird die Instanz des activeFragment
     * geprüft. Ist activeFragment von der Instanz von {@link MainFragment}, so wird das System mit 0 verlassen,
     * andernfalls wird über das Interface {@link IOnBackPressed} die Methode goBackToMainFragment aufgerufen.
     * Jedes untergeordnete Fragment muss diese Methode implementieren.
     */
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