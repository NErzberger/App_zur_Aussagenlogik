package com.dhbw.app_zur_aussagenlogik.fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.dhbw.app_zur_aussagenlogik.MainActivity;
import com.dhbw.app_zur_aussagenlogik.Modi;
import com.dhbw.app_zur_aussagenlogik.R;
import com.dhbw.app_zur_aussagenlogik.core.Parser;
import com.dhbw.app_zur_aussagenlogik.core.ParserException;
import com.google.android.material.tabs.TabLayout;

import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {

    private TabLayout layout;
    private EditText inputText;
    private EditText resultText;
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
    private Button buttonRechenweg;
    private Button buttonOneBack;


    private Modi modus = Modi.DNF;

    private MainActivity mainActivity;

    private View view;



    public MainFragment(AppCompatActivity mainActivity) {
        this.mainActivity = (MainActivity) mainActivity;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MainFragment.
     */

    public static MainFragment newInstance(AppCompatActivity mainActivity) {
        MainFragment fragment = new MainFragment(mainActivity);
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
        view = inflater.inflate(R.layout.fragment_main, container, false);

        layout = view.findViewById(R.id.tabLayout);

        // Deklaration der Tastatur
        buttonA = view.findViewById(R.id.buttonA);
        buttonB = view.findViewById(R.id.buttonB);
        buttonC = view.findViewById(R.id.buttonC);
        buttonD = view.findViewById(R.id.buttonD);
        buttonE = view.findViewById(R.id.buttonE);
        buttonNegation = view.findViewById(R.id.buttonNegation);
        buttonDelete = view.findViewById(R.id.buttonDelete);
        buttonCE = view.findViewById(R.id.buttonCE);
        buttonAnd = view.findViewById(R.id.buttonAnd);
        buttonOr = view.findViewById(R.id.buttonOr);
        buttonImplikation = view.findViewById(R.id.buttonImplikation);
        buttonImplikationBeidseitig = view.findViewById(R.id.buttonImplikationBeidseitig);
        buttonReturn = view.findViewById(R.id.buttonReturn);
        buttonKlammerAuf = view.findViewById(R.id.buttonKlammerAuf);
        buttonKlammerZu = view.findViewById(R.id.buttonKlammerZu);
        buttonRechenweg = view.findViewById(R.id.buttonRechenweg);
        buttonOneBack = view.findViewById(R.id.buttonOneBack);

        inputText = view.findViewById(R.id.input);
        resultText = view.findViewById(R.id.solution);

        //normale Tastatur wird direkt wieder geschlossen
        inputText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIUtil.hideKeyboard(view.getContext(), view);
            }});

        // OnClickListener für das TabLayout

        layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String tabName = tab.getText().toString(); // Achtung Text nicht ändern oder über id?
                switch (tabName) {
                    case "DNF":
                        resultText.setEnabled(false);
                        modus = Modi.DNF;
                        break;
                    case "KNF":
                        resultText.setEnabled(false);
                        modus = Modi.KNF;
                        break;
                    case "Resolu-tion":
                        resultText.setEnabled(false);
                        modus = Modi.RESOLUTION;
                        break;
                    case "2 For-\nmeln":
                        resultText.setEnabled(true);
                        modus = Modi.FORMELN;
                        break;
                    case "Tab-\nleaux":
                        resultText.setEnabled(false);
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
                    case KNF:
                        launchParser(Modi.KNF);
                        break;
                    case DNF:
                        launchParser(Modi.DNF);
                        break;
                    case RESOLUTION:
                       mainActivity.replaceFragment(new ResolutionFragment(mainActivity));
                       break;
                    case TABLEAUX:
                        mainActivity.replaceFragment(new TableauxFragment(mainActivity));
                        break;
                }


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

        buttonRechenweg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.replaceFragment(new NormalformFragment(mainActivity));
// Rechenweg erstellen
            }
        });

        buttonOneBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // eine Formel zurück
            }
        });

        return this.view;
    }

    private void launchParser(Modi modus){
        Parser parser = Parser.getInstance();
        String eingabeFormel = inputText.getText().toString();
        try {
            parser.setModus(modus);
            String resultFormel = parser.parseFormula(eingabeFormel);
            resultText.setText(resultFormel);
        }catch (ParserException pe){

        }
    }
}