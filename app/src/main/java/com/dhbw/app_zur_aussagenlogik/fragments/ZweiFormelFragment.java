package com.dhbw.app_zur_aussagenlogik.fragments;

import android.graphics.drawable.Drawable;
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

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ZweiFormelFragment extends Fragment {

    private MainActivity mainActivity;

    private Button homeButton;

    private TableLayout truthTable;
    private TextView textResult;

    private int[][] truthTableByInt;
    private int fehlercode;
    private ArrayList<Character> variables;

    public ZweiFormelFragment(MainActivity mainActivity, int[][] truthTableByInt, ArrayList<Character> variables) {
        this.mainActivity = mainActivity;
        this.truthTableByInt = truthTableByInt;
        this.variables = variables;
    }

    public ZweiFormelFragment(MainActivity mainActivity, int[][] truthTableByInt, int fehlercode) {
        this.mainActivity = mainActivity;
        this.truthTableByInt = truthTableByInt;
        this.fehlercode = fehlercode;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_zwei_formel, container, false);

        homeButton = view.findViewById(R.id.buttonHome);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.replaceFragment(new MainFragment(mainActivity));
            }
        });

        this.truthTable = view.findViewById(R.id.truthTable);
        this.textResult = view.findViewById(R.id.textResult);

        if(fehlercode==-20){
            this.textResult.setText("Die Formeln stimmen "+String.format("<b>%s</b>","nicht")+ " überein.");
            this.textResult.setTextSize(20);
        }else{
            this.textResult.setText("Die Formeln stimmen überein.");
            this.textResult.setTextSize(20);
        }

        TableRow header = new TableRow(truthTable.getContext());
        for(int i = 0; i< variables.size()+2; i++){
            TextView textView = new TextView(header.getContext());

            if(i<variables.size()){
                textView.setText(" " + Character.toString(variables.get(i)) + " ");
            } else if(i==variables.size()){
                textView.setText("F1");
            } else if(i== variables.size()+1){
                textView.setText("F2");
            }

            textView.setTextSize(25);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            //textView.setPadding(10, 5, 10, 5);
            textView.setBackground(ContextCompat.getDrawable(truthTable.getContext(), R.drawable.table_border));
            header.addView(textView);
        }
        truthTable.addView(header, 0);

        for(int i = 0; i < truthTableByInt[0].length; i++){
            TableRow tableRow = new TableRow(truthTable.getContext());
            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            tableRow.setLayoutParams(layoutParams);

            for(int j = 0; j < truthTableByInt.length; j++){
                TextView textView = new TextView(tableRow.getContext());
                textView.setText(Integer.toString(truthTableByInt[j][i]));
                textView.setTextSize(25);
                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                //textView.setPadding(10, 5, 10, 5);
                textView.setBackground(ContextCompat.getDrawable(truthTable.getContext(), R.drawable.table_border));
                tableRow.addView(textView);
            }
            truthTable.addView(tableRow, i+1);
        }
        return view;
    }
}