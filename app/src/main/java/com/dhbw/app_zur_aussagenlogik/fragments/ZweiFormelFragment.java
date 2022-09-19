package com.dhbw.app_zur_aussagenlogik.fragments;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.dhbw.app_zur_aussagenlogik.MainActivity;
import com.dhbw.app_zur_aussagenlogik.R;
import com.dhbw.app_zur_aussagenlogik.interfaces.IOnBackPressed;
import com.dhbw.app_zur_aussagenlogik.sql.dataObjects.History;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ZweiFormelFragment extends Fragment implements IOnBackPressed {

    private MainActivity mainActivity;

    private Button homeButton;

    private TableLayout truthTable;
    private TextView textResult;

    private int[][] truthTableByInt;
    private int fehlercode = 0;
    private ArrayList<Character> variables;

    private History history;

    public ZweiFormelFragment(MainActivity mainActivity, int fehlercode, History history) {
        this.mainActivity = mainActivity;
        this.fehlercode = fehlercode;
        this.history = history;
    }

    public ZweiFormelFragment(MainActivity mainActivity, int[][] truthTableByInt, ArrayList<Character> variables, History history) {
        this.mainActivity = mainActivity;
        this.truthTableByInt = truthTableByInt;
        this.variables = variables;
        this.history = history;
    }

    public ZweiFormelFragment(MainActivity mainActivity, int[][] truthTableByInt, ArrayList<Character> variables, int fehlercode, History history) {
        this.mainActivity = mainActivity;
        this.truthTableByInt = truthTableByInt;
        this.variables = variables;
        this.fehlercode = fehlercode;
        this.history = history;
    }

    public ZweiFormelFragment(MainActivity mainActivity, int[][] truthTableByInt, int fehlercode, History history) {
        this.mainActivity = mainActivity;
        this.truthTableByInt = truthTableByInt;
        this.fehlercode = fehlercode;
        this.history = history;
    }


    /**
     * Die Methode onCreate ruft lediglich die super Methode onCreate auf und gibt die
     * Übergabeparameter direkt weiter.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_zwei_formel, container, false);
        mainActivity.setActiveFragment(this);
        homeButton = view.findViewById(R.id.buttonHome);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBackToMainFragment();
            }
        });

        if(fehlercode == 0 || fehlercode == -20){
            this.truthTable = view.findViewById(R.id.truthTable);
        }
        this.textResult = view.findViewById(R.id.textResult);

        if(fehlercode==-20){
            this.textResult.setText("Die Formeln stimmen nicht überein.");
            this.textResult.setTextSize(20);
        }else if(fehlercode==-10){
            this.textResult.setText("Golden bug!");
            this.textResult.setTextSize(20);
        }else if(fehlercode==-30){
            this.textResult.setText("Die Variablen der Formeln stimmen nicht überein.");
            this.textResult.setTextSize(20);
        }else{
            this.textResult.setText("Die Formeln stimmen überein.");
            this.textResult.setTextSize(20);
        }

        if(fehlercode == 0 || fehlercode == -20) {
            TableRow header = new TableRow(truthTable.getContext());
            for (int i = 0; i < variables.size() + 2; i++) {
                TextView textView = new TextView(header.getContext());

                if (i < variables.size()) {
                    textView.setText(" " + Character.toString(variables.get(i)) + " ");
                } else if (i == variables.size()) {
                    textView.setText("F1");
                } else if (i == variables.size() + 1) {
                    textView.setText("F2");
                }

                textView.setTextSize(25);
                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                //textView.setPadding(10, 5, 10, 5);
                textView.setBackground(ContextCompat.getDrawable(truthTable.getContext(), R.drawable.table_border));
                header.addView(textView);
            }
            truthTable.addView(header, 0);

            for (int i = 0; i < truthTableByInt[0].length; i++) {
                TableRow tableRow = new TableRow(truthTable.getContext());
                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                tableRow.setLayoutParams(layoutParams);

                for (int j = 0; j < truthTableByInt.length; j++) {
                    TextView textView = new TextView(tableRow.getContext());
                    textView.setText(Integer.toString(truthTableByInt[j][i]));
                    textView.setTextSize(25);
                    textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    //textView.setPadding(10, 5, 10, 5);
                    textView.setBackground(ContextCompat.getDrawable(truthTable.getContext(), R.drawable.table_border));
                    tableRow.addView(textView);
                }
                truthTable.addView(tableRow, i + 1);
            }
        }
        return view;
    }

    @Override
    public void goBackToMainFragment() {
        mainActivity.replaceFragment(new MainFragment(mainActivity, history));
    }
}