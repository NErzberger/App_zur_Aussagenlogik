package com.dhbw.app_zur_aussagenlogik.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dhbw.app_zur_aussagenlogik.MainActivity;
import com.dhbw.app_zur_aussagenlogik.Modi;
import com.dhbw.app_zur_aussagenlogik.R;
import com.dhbw.app_zur_aussagenlogik.core.Parser;
import com.dhbw.app_zur_aussagenlogik.core.ParserException;
import com.dhbw.app_zur_aussagenlogik.sql.dataObjects.History;
import com.dhbw.app_zur_aussagenlogik.sql.dbHelper.HistoryDataSource;
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
    private MenuItem itemVerlauf;
    private MenuItem itemAnleitung;
    private MenuItem itemUeberUns;

    private TextView textIhreFormelErgebnis;


    private Modi modus = Modi.DNF;

    private MainActivity mainActivity;

    private View view;

    private HistoryDataSource dataSource;

    public int formulaHistoryPosition;

    private String firstFormula;
    private String secondFormula;

    private History historyElement;

    private History newHistoryElement;

    public MainFragment(AppCompatActivity mainActivity) {
        this.mainActivity = (MainActivity) mainActivity;
    }

    public MainFragment(AppCompatActivity mainActivity, History historyElement) {
        this.mainActivity = (MainActivity) mainActivity;
        this.historyElement = historyElement;
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

        dataSource = new HistoryDataSource(getContext());



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

        itemVerlauf = view.findViewById(R.id.history);
        itemAnleitung = view.findViewById(R.id.anleitung);
        itemUeberUns = view.findViewById(R.id.UeberUns);

        inputText = view.findViewById(R.id.input);
        resultText = view.findViewById(R.id.solution);



        textIhreFormelErgebnis = view.findViewById(R.id.textIhreFormelErgebnis);

        //normale Tastatur wird direkt wieder geschlossen
        inputText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


            }});

        layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String tabName = tab.getText().toString(); // Achtung Text nicht ändern oder über id?
                switch (tabName) {
                    case "DNF":
                        modus = Modi.DNF;
                        break;
                    case "KNF":
                        modus = Modi.KNF;
                        break;
                    case "Resolu-tion":
                        modus = Modi.RESOLUTION;
                        break;
                    case "2 For-\nmeln":
                        modus = Modi.FORMELN;
                        //changeLayout(modus);
                        //mainActivity.replaceFragment(new Resolution(mainActivity));
                        break;
                    case "Tab-\nleaux":
                        modus = Modi.TABLEAUX;
                        break;
                }
                changeLayout(modus);
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
                resultText.setText("");
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
                    case FORMELN:
                        launchParser(Modi.FORMELN);
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
                mainActivity.replaceFragment(new NormalformFragment(mainActivity, historyElement));
            }
        });

        buttonOneBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(historyElement!=null) {
                    try {
                        HistoryDataSource dataSource = new HistoryDataSource(getContext());
                        History h = dataSource.getOneBeforeHistory(historyElement.getId());
                        historyElement = h;
                        switchModi();
                        setFormulas();
                    }catch(Exception e){

                    }
                }
            }
        });

        switchModi();

        setFormulas();

        return this.view;
    }

    private void switchModi(){
        /*
         * Selected Tab wählen
         */

        if(historyElement != null){
            modus = getModusByString(historyElement.getModi());
        }
        switch (modus){
            case DNF:
                TabLayout.Tab tab = layout.getTabAt(0);
                tab.select();
                break;
            case KNF:
                TabLayout.Tab tab1 = layout.getTabAt(1);
                tab1.select();
                break;
            case RESOLUTION:
                TabLayout.Tab tab2 = layout.getTabAt(2);
                tab2.select();
                break;
            case FORMELN:
                TabLayout.Tab tab3 = layout.getTabAt(3);
                tab3.select();
                break;
            case TABLEAUX:
                TabLayout.Tab tab4 = layout.getTabAt(4);
                tab4.select();
                break;
        }
    }

    private void setFormulas(){
        if(historyElement != null){
            inputText.setText(historyElement.getFormula());
            resultText.setText(historyElement.getSecondFormula());
        }
    }



    private void launchParser(Modi modus){
        Parser parser = Parser.getInstance();
        String eingabeFormel = inputText.getText().toString();
        try {
            if(modus==Modi.FORMELN){
                String zweiteFormel = resultText.getText().toString();
                try {
                    int[][] truthTable = parser.parseTwoFormula(eingabeFormel, zweiteFormel);
                    this.newHistoryElement = new History(0, getModiText(modus), eingabeFormel, zweiteFormel);
                    this.historyElement = dataSource.addHistoryEntry(this.newHistoryElement);
                    mainActivity.replaceFragment(new ZweiFormelFragment(mainActivity, truthTable));
                }catch (ParserException pe){
                    // Formeln stimmen nicht über ein
                    if(pe.getFehlercode()==-20){
                        int[][] truthTable = pe.getTruthTable();
                        this.newHistoryElement = new History(0, getModiText(modus), eingabeFormel, zweiteFormel);
                        this.historyElement=dataSource.addHistoryEntry(this.newHistoryElement);
                        mainActivity.replaceFragment(new ZweiFormelFragment(mainActivity, truthTable, -20));
                        // Falsche Eingabe
                    }else if(pe.getFehlercode()==-10){

                    }
                }
            }else {

                parser.setModus(modus);
                String resultFormel = parser.parseFormula(eingabeFormel);
                resultText.setText(resultFormel);
                this.newHistoryElement = new History(0, getModiText(modus), eingabeFormel, resultFormel);
                this.historyElement=dataSource.addHistoryEntry(this.newHistoryElement);
            }
            this.buttonRechenweg.setEnabled(true);
        }catch (ParserException pe){
            // Falsche Eingabe
            if(pe.getFehlercode()==-10){

            }
        }
    }

    private void changeLayout(Modi modus){

        if(modus == Modi.DNF || modus == Modi.KNF || modus == Modi.TABLEAUX || modus == Modi.RESOLUTION ){
            buttonRechenweg.setVisibility(view.VISIBLE);
            textIhreFormelErgebnis.setText("Lösung");
            resultText.setEnabled(false);
            resultText.setText("");
            resultText.setHint("Lösung");
            inputText.setFocusedByDefault(true);
        }
        else if(modus == Modi.FORMELN){
            buttonRechenweg.setVisibility(view.INVISIBLE);
            textIhreFormelErgebnis.setText("2. Formel");
            resultText.setEnabled(true);
            resultText.setText("");
            resultText.setHint("Bitte geben Sie hier ihre zweite Formel ein.");
            inputText.setFocusedByDefault(true);
        }
        buttonRechenweg.setEnabled(false);

    }

    public void setInputFormula(String inputFormula){
        this.firstFormula = inputFormula;
    }

    public void setResultFormula(String resultFormula){
        this.secondFormula = resultFormula;
    }


    private String getModiText(Modi modus){
        switch (modus){
            case DNF:
                return "DNF";
            case KNF:
                return "KNF";
            case RESOLUTION:
                return "R";
            case FORMELN:
                return "2 F";
            case TABLEAUX:
                return "T";
        }
        return "";
    }

    private Modi getModusByString(String modus){
        switch (modus){
            case "KNF":
                return Modi.KNF;
            case "DNF":
                return Modi.DNF;
            case "2 F":
                return Modi.FORMELN;
            case "R":
                return Modi.RESOLUTION;
            case "T":
                return Modi.TABLEAUX;
        }
        return null;
    }

    public History getNewHistoryElement(){
        return this.newHistoryElement;
    }
}