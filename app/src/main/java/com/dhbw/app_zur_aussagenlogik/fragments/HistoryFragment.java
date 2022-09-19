package com.dhbw.app_zur_aussagenlogik.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dhbw.app_zur_aussagenlogik.MainActivity;
import com.dhbw.app_zur_aussagenlogik.R;
import com.dhbw.app_zur_aussagenlogik.interfaces.IOnBackPressed;
import com.dhbw.app_zur_aussagenlogik.sql.dataObjects.History;
import com.dhbw.app_zur_aussagenlogik.sql.dbHelper.HistoryDataSource;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment implements IOnBackPressed {

    private List<History> historyList;

    private MainActivity mainActivity;

    private View view;

    private Button homeButton;
    private RecyclerView history_view;

    public HistoryFragment(MainActivity mainActivity) {
       super(R.layout.fragment_history);
       this.mainActivity = mainActivity;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment Resolution.
     */
    public static HistoryFragment newInstance(MainActivity mainActivity) {
        HistoryFragment fragment = new HistoryFragment(mainActivity);
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
        view =  inflater.inflate(R.layout.fragment_history, container, false);
        mainActivity.setActiveFragment(this);
        homeButton = view.findViewById(R.id.buttonHome);
        history_view = view.findViewById(R.id.List_History);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBackToMainFragment();
            }
        });

        // Liste Laden
        HistoryDataSource dataSource = new HistoryDataSource(getContext());
        List<History> historyList = dataSource.getAllHistoryEntries();

        historyList.add(0, new History(-1, "Modi", "Orginalformel", "Ergebnisformel/Zweitformel"));

        // Liste darstellen
        history_view.setLayoutManager(new LinearLayoutManager(getContext()));
        RecyclerView.Adapter adapter = new Adapter_for_HistroyView(historyList);
        history_view.setAdapter(adapter);

        // onClick listener
        ((Adapter_for_HistroyView) adapter).setOnItemClickListener(new Adapter_for_HistroyView.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Log.d(HistoryFragment.class.getSimpleName(), itemView.getId()+ " an der Stelle " + position + " wurde angeklickt.");
                Log.d(HistoryFragment.class.getSimpleName(), position + " hat die ID "+ history_view.getAdapter().getItemId(position));


                TextView inputFormula = itemView.findViewById(R.id.firstFormula);
                TextView resultFormula = itemView.findViewById(R.id.secondFormula);
                TextView formulaId = itemView.findViewById(R.id.formulaId);
                TextView formulaModi = itemView.findViewById(R.id.formulaModi);
                History historyElement = new History(Integer.parseInt(formulaId.getText().toString()), formulaModi.getText().toString(), inputFormula.getText().toString(), resultFormula.getText().toString());


                MainFragment mainFragment = new MainFragment(mainActivity, historyElement);

                mainActivity.replaceFragment(mainFragment);

            }
        });

        return view;
    }


    @Override
    public void goBackToMainFragment() {
        mainActivity.replaceFragment(new MainFragment(mainActivity));
    }
}