package com.dhbw.app_zur_aussagenlogik.fragments;

import android.os.Bundle;

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

    public ZweiFormelFragment(MainActivity mainActivity, int[][] truthTableByInt) {
        this.mainActivity = mainActivity;
        this.truthTableByInt = truthTableByInt;
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
        }else{
            this.textResult.setText("Die Formeln stimmen überein.");
        }

        for(int i = 0; i < truthTableByInt[0].length; i++){
            TableRow tableRow = new TableRow(truthTable.getContext());
            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            tableRow.setLayoutParams(layoutParams);
            for(int j = 0; j < truthTableByInt.length; j++){
                TextView textView = new TextView(tableRow.getContext());
                textView.setText(Integer.toString(truthTableByInt[j][i]));
                tableRow.addView(textView);
            }
            truthTable.addView(tableRow, i);
        }

        return view;
    }
}