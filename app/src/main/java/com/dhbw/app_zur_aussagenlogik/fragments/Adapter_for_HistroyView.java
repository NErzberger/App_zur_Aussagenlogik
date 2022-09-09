package com.dhbw.app_zur_aussagenlogik.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dhbw.app_zur_aussagenlogik.R;
import com.dhbw.app_zur_aussagenlogik.sql.dataObjects.History;

import java.util.List;

public class Adapter_for_HistroyView extends RecyclerView.Adapter<Adapter_for_HistroyView.MyViewHolder> {

    private List<History> historyList;

    private OnItemClickListener listener;

    public Adapter_for_HistroyView(List<History> historyList){
        this.historyList = historyList;
    }

    public interface OnItemClickListener{
        void onItemClick(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View rowLayout = inflater.inflate(R.layout.history_row_layout, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(rowLayout);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        History history = historyList.get(position);

        TextView inputText = holder.formulaText;
        inputText.setText(history.getFormula());
        TextView solutionText = holder.resultFormulaText;
        solutionText.setText(history.getSecondFormula());
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView formulaText;
        public TextView resultFormulaText;

        public MyViewHolder(final View itemView) {
            super(itemView);

            this.formulaText=itemView.findViewById(R.id.firstFormula);
            this.resultFormulaText=itemView.findViewById(R.id.secondFormula);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(itemView, position);
                        }
                    }
                }
            });
        }

        @Override
        public void onClick(View view) {

        }
    }
}
