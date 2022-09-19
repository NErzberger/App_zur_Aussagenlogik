package com.dhbw.app_zur_aussagenlogik.fragments;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dhbw.app_zur_aussagenlogik.MainActivity;
import com.dhbw.app_zur_aussagenlogik.Modi;
import com.dhbw.app_zur_aussagenlogik.R;
import com.dhbw.app_zur_aussagenlogik.core.ErrorHandler;
import com.dhbw.app_zur_aussagenlogik.core.Parser;
import com.dhbw.app_zur_aussagenlogik.core.ParserException;
import com.dhbw.app_zur_aussagenlogik.interfaces.IOnBackPressed;
import com.dhbw.app_zur_aussagenlogik.sql.dataObjects.History;
import com.dhbw.app_zur_aussagenlogik.sql.dbHelper.HistoryDataSource;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

/**
 * Das MainFragment erbt von der Klasse {@link Fragment} und implementiert das Interface {@link IOnBackPressed}.
 * Das MainFragment dient als primäres Userinterface. Es ist eine eigene Tastatur implementiert, welche im MainFragment
 * gehalten und gesteuert wird. Vom MainFragment aus wird der User in die Unterfragments
 * <ul>
 *     <li>{@link NormalformFragment}</li>
 *     <li>{@link ResolutionFragment}</li>
 *     <li>{@link TableauxFragment}</li>
 *     <li>{@link TruthTableFragment}</li>
 *     <li>{@link ZweiFormelFragment}</li>
 * </ul>
 * geleitet. Betätigt der User in einem aller anderer Fragments, einschließlich der aufgeführten, den Home Button, so
 * wird er zurück in das MainFragment geleitet.
 * Das MainFragment kann sich in mehreren Modis befinden, wofür das Enumeration {@link Modi} verwendet wird.
 * Das MainFragment kann in die Modis
 * <ul>
 *     <li>DNF</li>
 *     <li>KNF</li>
 *     <li>Resolution</li>
 *     <li>Zwei Formel</li>
 *     <li>Tableaux</li>
 *     <li>Wertetabelle</li>
 * </ul>
 * gehen. Dies ist für die jeweilige Funktionen wichtig, in welchen der {@link Parser} ausgeführt werden soll.
 */
public class MainFragment extends Fragment implements IOnBackPressed {

    /**
     * Das Klassenattribut mainActivity der Klasse {@link MainActivity} ist notewendig,
     * um auf den Kontext zugreifen zu können.
     */
    private MainActivity mainActivity;

    /**
     * Die view beinhaltet alle grafischen Komponenten.
     */
    private View view;

    /**
     * Das TabLayout ist für die Modus Auswahl für den User zuständig.
     * Je nach Auswahl werden die Modis in das Klassenattribut geschrieben und im weiteren Verlauf verwendet.
     */
    private TabLayout layout;

    /**
     * Das Textfeld textIhreFormelErgebnis wird im Modus 2 Formeln abgeändert,
     * weshalb die Nutzung als Klassenattribut notwendig ist.
     */
    private TextView textIhreFormelErgebnis;

    /**
     * Die EditTexte inputText und resultText werden für die User Eingaben und Ausgaben verwendet.
     */
    private EditText inputText;
    private EditText resultText;

    /**
     * Mittels dem Klassenattribut textFieldFocus wird festgestellt, welches Textfeld aktuell über den Fokus des Users
     * verfügt und in welches Feld geschrieben werden soll. Dies wird mit den statischen Variablen FIRST_FORMULA_FOCUS
     * und SECOND_FORMULA_FOCUS gesteuert.
     */
    private int textFieldFocus;
    private final static int FIRST_FORMULA_FOCUS = 0, SECOND_FORMULA_FOCUS = 1;

    /**
     * Das Klassenattribut modus des Enumeration {@link Modi} ist elementar für die Steuerung des Parsers der Klasse {@link Parser}.
     * Es können die Modis
     * <ul>
     *     <li>DNF</li>
     *     <li>KNF</li>
     *     <li>Resolution</li>
     *     <li>Zwei Formel</li>
     *     <li>Tableaux</li>
     *     <li>Wertetabelle</li>
     * </ul>
     * gewählt werden. Die Einstellung des Modi wird dem Parser übergeben und dieser entsprechend ausgeführt.
     * Per Default ist der Modus auf <b>DNF</b> eingestellt, da der dazugehörige Reiter ganz links ist und
     * dementsprechend auch der Modus eingestellt sein muss.
     */
    private Modi modus = Modi.DNF;

    /**
     * Die App verfügt über eine eigene Tastatur, da eine Reihe von Sonderzeichen verwendet werden.
     * Es werden die Buttons
     * <ul>
     *     <li>buttonA</li>
     *     <li>buttonB</li>
     *     <li>buttonC</li>
     *     <li>buttonD</li>
     *     <li>buttonE</li>
     *     <li>buttonNegation</li>
     *     <li>buttonDelete</li>
     *     <li>buttonCE</li>
     *     <li>buttonAnd</li>
     *     <li>buttonOr</li>
     *     <li>buttonImplikation</li>
     *     <li>buttonImplikationBeidseitig</li>
     *     <li>buttonReturn</li>
     *     <li>buttonKlammerAuf</li>
     *     <li>buttonKlammerZu</li>
     *     <li>buttonRechenweg</li>
     *     <li>buttonOneBack</li>
     * </ul>
     * Alle Buttons sind von der Klasse {@link Button}.
     */
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

    /**
     * Um einen Verlauf zu gewährleisten, wird eine DataSource der Klasse {@link HistoryDataSource} verwendet und
     * die historyElemente der Klasse {@link History} genutzt. Auf eine Rückmeldung des Parsers wird
     * das Element newHistoryElement erzeugt und in die Datenbank geschrieben. Dort wird das
     * Element gespeichert und eine neue ID erzeugt, woraufhin das Element zurückgegeben wird und in das
     * Element historyElement geschrieben wird. Dieses wird für die OneBack - Funktion benötigt, um das direkt
     * vorgehende Element abzufragen.
     */
    private HistoryDataSource dataSource;
    private History historyElement;
    private History newHistoryElement;


    /**
     * Dies ist der erste von zweie Konstruktoren. Er benötigt lediglich die mainActivity und erzeugt daraufhin
     * ein Objekt des MainFragments. Dieser wird in der MainActivity benötigt oder in Fällen, wenn kein History Element
     * verfügbar ist.
     *
     * @param mainActivity
     */
    public MainFragment(AppCompatActivity mainActivity) {
        this.mainActivity = (MainActivity) mainActivity;
    }

    /**
     * Dies ist der zweite Konstruktor. Er benötigt die mainActivity und ein History Element. So wird er in den Fällen benötigt,
     * wenn ein History Element verfügbar ist und das mainFragment direkt in einem bestimmten Modus gestartet werden soll.
     *
     * @param mainActivity   Übergabeparameter der Klasse {@link MainActivity}
     * @param historyElement Übergabeparamenter der Klasse {@link History}
     */
    public MainFragment(AppCompatActivity mainActivity, History historyElement) {
        this.mainActivity = (MainActivity) mainActivity;
        this.historyElement = historyElement;
    }

    /**
     * Die Methode onCreate ruft lediglich die super Methode onCreate auf und gibt die
     * Übergabeparameter direkt weiter.
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_main, container, false);
        mainActivity.setActiveFragment(this);
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


        inputText = view.findViewById(R.id.input);
        resultText = view.findViewById(R.id.solution);

        inputText.setShowSoftInputOnFocus(false);
        resultText.setShowSoftInputOnFocus(false);

        textIhreFormelErgebnis = view.findViewById(R.id.textIhreFormelErgebnis);

        /*
        OnFocusChangeListener wird gebraucht, damit der Fokus direkt in dem EditText liegt, in
        welches geklickt wurde. Ansonsten benötigt man zwei Klicks in das EditText, wenn man von
        einem EditText in das andere wechselt.
         */
        inputText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                textFieldFocus = FIRST_FORMULA_FOCUS;
            }
        });

        resultText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                textFieldFocus = SECOND_FORMULA_FOCUS;
            }
        });

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
                    case "Resolution":
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
                    case "Tabelle":
                        modus = Modi.WERTETABELLE;
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
                writeToTextField("a");
            }
        });

        buttonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeToTextField("b");
            }
        });

        buttonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeToTextField("c");
            }
        });

        buttonD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeToTextField("d");
            }
        });

        buttonE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeToTextField("e");
            }
        });

        buttonNegation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeToTextField("\u00AC");
            }
        });
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteCharacter();
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
                writeToTextField("\u2227");
            }
        });

        buttonOr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeToTextField("\u22C1");
            }
        });

        buttonImplikation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeToTextField("\u2192");
            }
        });

        buttonImplikationBeidseitig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeToTextField("\u2194");
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
                    case WERTETABELLE:
                        launchParser(Modi.WERTETABELLE);
                }
            }
        });

        buttonKlammerAuf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeToTextField("(");
            }
        });

        buttonKlammerZu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeToTextField(")");
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
                if (historyElement != null) {
                    try {
                        HistoryDataSource dataSource = new HistoryDataSource(getContext());
                        History h = dataSource.getOneBeforeHistory(historyElement.getId());
                        historyElement = h;
                        switchModi();
                        setFormulas();
                    } catch (Exception e) {

                    }
                }
            }
        });

        switchModi();

        setFormulas();

        return this.view;
    }

    /**
     *
     */
    private void switchModi() {
        /*
         * Selected Tab wählen
         */

        if (historyElement != null) {
            modus = getModusByString(historyElement.getModi());
        }
        switch (modus) {
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
          /*  case TABLEAUX:
                TabLayout.Tab tab4 = layout.getTabAt(4);
                tab4.select();
                break;*/
            case WERTETABELLE:
                TabLayout.Tab tab5 = layout.getTabAt(4);
                tab5.select();
                break;
        }
    }

    /**
     *
     */
    private void setFormulas() {
        if (historyElement != null) {
            inputText.setText(historyElement.getFormula());
            resultText.setText(historyElement.getSecondFormula());
        }
    }

    /**
     *
     * @param modus
     */
    private void launchParser(Modi modus) {
        Parser parser = Parser.getInstance();
        String eingabeFormel = inputText.getText().toString();
        resultText.setTextColor(Color.BLACK);
        try {
            if (modus == Modi.FORMELN) {
                String zweiteFormel = resultText.getText().toString();
                try {
                    int[][] truthTable = parser.parseTwoFormula(eingabeFormel, zweiteFormel);
                    ArrayList<Character> variables = parser.getVariables(eingabeFormel);
                    this.newHistoryElement = new History(0, getModiText(modus), eingabeFormel, zweiteFormel);
                    this.historyElement = dataSource.addHistoryEntry(this.newHistoryElement);
                    mainActivity.replaceFragment(new ZweiFormelFragment(mainActivity, truthTable, variables, this.historyElement));
                } catch (ParserException pe) {
                    // Formeln stimmen nicht über ein
                    if (pe.getFehlercode() == -20) {
                        int[][] truthTable = pe.getTruthTable();
                        ArrayList<Character> variables = pe.getVariables();
                        this.newHistoryElement = new History(0, getModiText(modus), eingabeFormel, zweiteFormel);
                        this.historyElement = dataSource.addHistoryEntry(this.newHistoryElement);
                        mainActivity.replaceFragment(new ZweiFormelFragment(mainActivity, truthTable, variables, -20, this.historyElement));
                        // Falsche Eingabe
                    } else if (pe.getFehlercode() == -10) {
                        this.newHistoryElement = new History(0, getModiText(modus), eingabeFormel, zweiteFormel);
                        this.historyElement = dataSource.addHistoryEntry(this.newHistoryElement);
                        mainActivity.replaceFragment(new ZweiFormelFragment(mainActivity, -10, this.historyElement));
                        // Falsche Eingabe
                    } else if (pe.getFehlercode() == -30) {
                        this.newHistoryElement = new History(0, getModiText(modus), eingabeFormel, zweiteFormel);
                        this.historyElement = dataSource.addHistoryEntry(this.newHistoryElement);
                        mainActivity.replaceFragment(new ZweiFormelFragment(mainActivity, -30, this.historyElement));
                        // Falsche Eingabe
                    } else {
                        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getContext());
                        dlgAlert.setMessage(ErrorHandler.getErrorMessage(pe.getFehlercode()));
                        dlgAlert.setTitle("Fehleingabe");
                        dlgAlert.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        //dismiss the dialog
                                    }
                                });
                        dlgAlert.setCancelable(true);
                        dlgAlert.create().show();

                    }
                }
            } else if (modus == Modi.WERTETABELLE) {
                try {
                    int[][] truthTable = parser.buildTruthTable(eingabeFormel);
                    ArrayList<Character> variables = parser.getVariables(eingabeFormel);
                    this.newHistoryElement = new History(0, getModiText(modus), eingabeFormel, null);
                    this.historyElement = dataSource.addHistoryEntry(this.newHistoryElement);
                    mainActivity.replaceFragment(new TruthTableFragment(mainActivity, truthTable, variables, this.historyElement));
                } catch (ParserException pe) {
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getContext());
                    dlgAlert.setMessage(ErrorHandler.getErrorMessage(pe.getFehlercode()));
                    dlgAlert.setTitle("Fehleingabe");
                    dlgAlert.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //dismiss the dialog
                                }
                            });
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();
                }
            } else if (modus == Modi.KNF || modus == Modi.DNF) {
                parser.setModus(modus);
                String resultFormel = parser.parseFormula(eingabeFormel);
                resultText.setText(resultFormel);
                this.newHistoryElement = new History(0, getModiText(modus), eingabeFormel, resultFormel);
                this.historyElement = dataSource.addHistoryEntry(this.newHistoryElement);
            } else if (modus == Modi.RESOLUTION) {
                // Hier würde die Resolution gestartet werden
                mainActivity.replaceFragment(new ResolutionFragment(mainActivity));
            }
            this.buttonRechenweg.setEnabled(true);
        } catch (ParserException pe) {
            // Falsche Eingabe
            int fehlercode = pe.getFehlercode();
            resultText.setText(ErrorHandler.getErrorMessage(fehlercode));
            resultText.setTextColor(Color.RED);
        }
    }

    /**
     *
     * @param modus
     */
    private void changeLayout(Modi modus) {

        if (modus == Modi.DNF || modus == Modi.KNF || modus == Modi.TABLEAUX || modus == Modi.RESOLUTION) {
            buttonRechenweg.setVisibility(view.VISIBLE);
            resultText.setVisibility(view.VISIBLE);
            textIhreFormelErgebnis.setVisibility(view.VISIBLE);
            textIhreFormelErgebnis.setText("Lösung");
            resultText.setEnabled(false);
            resultText.setText("");
            resultText.setHint("Lösung");
            inputText.setFocusedByDefault(true);
        } else if (modus == Modi.FORMELN) {
            buttonRechenweg.setVisibility(view.INVISIBLE);
            resultText.setVisibility(view.VISIBLE);
            textIhreFormelErgebnis.setVisibility(view.VISIBLE);
            textIhreFormelErgebnis.setText("2. Formel");
            resultText.setEnabled(true);
            resultText.setText("");
            resultText.setHint("Bitte geben Sie hier ihre zweite Formel ein.");
            inputText.setFocusedByDefault(true);
        } else if (modus == Modi.WERTETABELLE) {
            buttonRechenweg.setVisibility(view.INVISIBLE);
            resultText.setVisibility(view.INVISIBLE);
            textIhreFormelErgebnis.setVisibility(view.INVISIBLE);

        }
        buttonRechenweg.setEnabled(false);

    }

    /**
     *
     * @param modus
     * @return
     */
    private String getModiText(Modi modus) {
        switch (modus) {
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
            case WERTETABELLE:
                return "W";
        }
        return "";
    }

    /**
     *
     * @param modus
     * @return
     */
    private Modi getModusByString(String modus) {
        switch (modus) {
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
            case "W":
                return Modi.WERTETABELLE;
        }
        return null;
    }

    /**
     *
     * @param s
     */
    private void writeToTextField(String s) {
        if (textFieldFocus == FIRST_FORMULA_FOCUS) {
            int cursorPos = inputText.getSelectionEnd();
            inputText.setText(inputText.getText().toString().substring(0, cursorPos) + s +
                    inputText.getText().toString().substring(cursorPos, inputText.getText().toString().length()));
            inputText.setSelection(cursorPos + 1);
        } else if (textFieldFocus == SECOND_FORMULA_FOCUS) {
            int cursorPos = resultText.getSelectionEnd();
            resultText.setText(resultText.getText().toString().substring(0, cursorPos) + s +
                    resultText.getText().toString().substring(cursorPos, resultText.getText().toString().length()));
            resultText.setSelection(cursorPos + 1);
        }
    }

    /**
     *
     */
    private void deleteCharacter() {
        if (textFieldFocus == FIRST_FORMULA_FOCUS) {
            int cursorPos = inputText.getSelectionEnd();
            if (cursorPos > 0) {
                inputText.setText(inputText.getText().toString().substring(0, cursorPos - 1) +
                        inputText.getText().toString().substring(cursorPos, inputText.getText().toString().length()));
                inputText.setSelection(cursorPos - 1);
            }
        } else if (textFieldFocus == SECOND_FORMULA_FOCUS) {
            int cursorPos = resultText.getSelectionEnd();
            if (cursorPos > 0) {
                resultText.setText(resultText.getText().toString().substring(0, cursorPos - 1) +
                        resultText.getText().toString().substring(cursorPos, resultText.getText().toString().length()));
                resultText.setSelection(cursorPos - 1);
            }
        }
    }

    /**
     * 
     */
    @Override
    public void goBackToMainFragment() {
// do nothing
    }
}