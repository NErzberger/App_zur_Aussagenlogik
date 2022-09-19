package com.dhbw.app_zur_aussagenlogik.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dhbw.app_zur_aussagenlogik.MainActivity;
import com.dhbw.app_zur_aussagenlogik.Modi;
import com.dhbw.app_zur_aussagenlogik.R;
import com.dhbw.app_zur_aussagenlogik.core.Formel;
import com.dhbw.app_zur_aussagenlogik.core.Parser;
import com.dhbw.app_zur_aussagenlogik.interfaces.IOnBackPressed;
import com.dhbw.app_zur_aussagenlogik.sql.dataObjects.History;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NormalformFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NormalformFragment extends Fragment implements IOnBackPressed {


    private MainActivity mainActivity;

    private View view;

    private Button homeButton;
    private TextView pfeileErgebnis;
    private TextView deMorganErgebnis;
    private TextView normalformErgebnis;
    private TextView normalformText;
    private TextView orginalformel;

    private Modi modi;
    private List<Formel> rechenweg;

    private History historyElement;

    public NormalformFragment(MainActivity mainActivity) {
        super(R.layout.fragment_normalform);
        this.mainActivity = mainActivity;
    }


    public NormalformFragment(MainActivity mainActivity, History historyElement) {
        super(R.layout.fragment_normalform);
        this.mainActivity = mainActivity;
        this.historyElement = historyElement;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Parser p = Parser.getInstance();
        rechenweg = new ArrayList<>();
        rechenweg = p.getRechenweg();
        modi = p.getModus();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_normalform, container, false);
        mainActivity.setActiveFragment(this);
        homeButton = view.findViewById(R.id.buttonHome);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBackToMainFragment();
            }
        });

        pfeileErgebnis = view.findViewById(R.id.pfeileErgebnis);
        deMorganErgebnis = view.findViewById(R.id.deMorganErgebnis);
        normalformErgebnis = view.findViewById(R.id.normalformErgebnis);
        normalformText = view.findViewById(R.id.normalformText);
        orginalformel = view.findViewById(R.id.orginalformel);

        if(modi == Modi.DNF){
            normalformText.setText("DNF");
        }else if(modi == Modi.KNF){
            normalformText.setText("KNF");
        }

        /**
         * Rechenweg 0 = Orginale Formel
         * Rechenweg 1 = Pfeile Auflösen Ergebnis
         * Rechenweg 2 = DeMorgan Ergebnis
         * Rechenweg 3 = Normalform
         */
        orginalformel.setText(rechenweg.get(0).toString());
        pfeileErgebnis.setText(rechenweg.get(1).toString());
        deMorganErgebnis.setText(rechenweg.get(2).toString());
        normalformErgebnis.setText(rechenweg.get(3).toString());

        return view;
    }

    @Override
    public void goBackToMainFragment() {
        mainActivity.replaceFragment(new MainFragment(mainActivity, historyElement));
    }
}