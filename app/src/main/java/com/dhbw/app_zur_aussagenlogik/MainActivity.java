package com.dhbw.app_zur_aussagenlogik;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dhbw.app_zur_aussagenlogik.parser.Parser;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private TabLayout layout;

    private TabItem item1;
    private TabItem item2;
    private TabItem item3;
    private TabItem item4;
    private TabItem item5;

    private EditText inputText;

    private Button buttonA;
    private Button buttonB;
    private Button buttonC;
    private Button buttonD;
    private Button buttonE;
    private Button buttonNegation;
    private Button buttonDelete;
    private Button buttonCE;
    private Button buttonAnd;
    private Button buttonOr;
    private Button buttonImplikation;
    private Button buttonImplikationBeidseitig;
    private Button buttonReturn;
    private Button buttonKlammerAuf;
    private Button buttonKlammerZu;

    private FragmentManager fragmentManager;

    private Modi modus = Modi.DNF;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout = findViewById(R.id.tabLayout);
        /*item1 = findViewById(R.id.tabItem1);
        item2 = findViewById(R.id.tabItem2);
        item3 = findViewById(R.id.tabItem3);
        item4 = findViewById(R.id.tabItem4);
        item5 = findViewById(R.id.tabItem5);*/

        // Deklaration der Tastatur

        buttonA = findViewById(R.id.buttonA);
        buttonB = findViewById(R.id.buttonB);
        buttonC = findViewById(R.id.buttonC);
        buttonD = findViewById(R.id.buttonD);
        buttonE = findViewById(R.id.buttonE);
        buttonNegation = findViewById(R.id.buttonNegaition);
        buttonDelete = findViewById(R.id.buttonDelete);
        buttonCE = findViewById(R.id.buttonCE);
        buttonAnd = findViewById(R.id.buttonAnd);
        buttonOr = findViewById(R.id.buttonOr);
        buttonImplikation = findViewById(R.id.buttonImplikation);
        buttonImplikationBeidseitig = findViewById(R.id.buttonImplikationBeidseitig);
        buttonReturn = findViewById(R.id.buttonReturn);
        buttonKlammerAuf = findViewById(R.id.buttonKlammerAuf);
        buttonKlammerZu = findViewById(R.id.buttonKlammerZu);

        inputText = findViewById(R.id.input);

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.resolutionFragment, Resolution.class, null, "tag")
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();

        // OnClickListener für das TabLayout

        layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String tabName = tab.getText().toString();
                switch (tabName) {
                    case "DNF":
                        modus = Modi.DNF;
                        break;
                    case "KNF":
                        modus = Modi.KNF;
                        break;
                    case "Resolution":
                        modus = Modi.RESOLUTION;
                        break;
                    case "2 Formeln":
                        modus = Modi.FORMELN;
                        break;
                    case "Tableaux":
                        modus = Modi.TABLEAUX;
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }

        });

        buttonA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputText.setText(inputText.getText() + "a");
            }
        });

        buttonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputText.setText(inputText.getText() + "b");
            }
        });

        buttonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputText.setText(inputText.getText() + "c");
            }
        });

        buttonD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputText.setText(inputText.getText() + "d");
            }
        });

        buttonE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputText.setText(inputText.getText() + "e");
            }
        });

        buttonNegation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputText.setText(inputText.getText() + "\u00AC");
            }
        });
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String formular = inputText.getText().toString();
                if(!formular.isEmpty()) {
                    inputText.setText(formular.substring(0, formular.length() - 1));
                }
            }
        });

        buttonCE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputText.setText("");
            }
        });

        buttonAnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputText.setText(inputText.getText() + "\u2227");
            }
        });

        buttonOr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputText.setText(inputText.getText() + "\u22C1");
            }
        });

        buttonImplikation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputText.setText(inputText.getText() + "\u2192");
            }
        });

        buttonImplikationBeidseitig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputText.setText(inputText.getText() + "\u2194");
            }
        });

        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (modus) {
                    case RESOLUTION:
                        // Resolution Fragment aufmachen
                        Resolution r = (Resolution) fragmentManager.findFragmentByTag("tag");

                    case TABLEAUX:
                        // Tableaux Fragment aufmachen
                }
                //launchParser();
            }
        });

        buttonKlammerAuf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputText.setText(inputText.getText() + "(");
            }
        });

        buttonKlammerZu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputText.setText(inputText.getText() + ")");
            }
        });


    }

    private void launchParser(){
        Parser parser = Parser.getInstance();
        parser.setParserParameter(modus, inputText.getText().toString());
    }


}