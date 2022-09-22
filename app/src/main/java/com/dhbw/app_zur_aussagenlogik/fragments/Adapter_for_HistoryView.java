package com.dhbw.app_zur_aussagenlogik.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dhbw.app_zur_aussagenlogik.R;
import com.dhbw.app_zur_aussagenlogik.sql.dataObjects.History;

import java.util.List;

/**
 * Die Klasse Adapter_for_HistoryView wird in der Klasse {@link HistoryFragment} verwendet, um eien Verbindung zwischen der {@link RecyclerView} history_view
 * und den darin liegenden Elementen zu halten. Auf die Listen Elemente soll gedrückt werden können. Hierfür ist ein OnClickListener von nöten.
 * Um die Funktionalitäten erfüllen zu können, muss der Adapter der RecyclerView geerbt werden, welcher ein Generic benötigt,
 * welches die anonyme Klasse {@link MyViewHolder} erfüllt.
 * @author Nico Erzberger
 * @version 1.0
 * @see HistoryFragment
 */
public class Adapter_for_HistoryView extends RecyclerView.Adapter<Adapter_for_HistoryView.MyViewHolder> {

    /**
     * Es wird eine Liste mit historischen Elementen der Klasse {@link History} als Klassenvariablen deklariert.
     */
    private final List<History> historyList;

    /**
     * Mit der Klassenvariable listener des Interfaces {@link OnItemClickListener} soll ein Klicken auf die Listeneiträge ermöglicht werden.
     */
    private OnItemClickListener listener;

    /**
     * Zum Erstellen eines Adapters ist eine Liste mit historischen Elementen notwendig.
     * @param historyList Als Übergabeparameter ist eine Liste mit dem Generik {@link History} erwartet.
     */
    public Adapter_for_HistoryView(List<History> historyList){
        this.historyList = historyList;
    }

    /**
     * Anonymes Interface, um die Möglichkeit einer Implementierung eines OnClickEvents zu bieten.
     */
    public interface OnItemClickListener{
        void onItemClick(View itemView, int position);
    }

    /**
     * Setter für den OnItemClickListener
     * @param listener Übergabeparameter des Interfaces {@link OnItemClickListener}
     */
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    /**
     * Die Methode onCreateViewHolder generiert ein Objekt der Klasse {@link MyViewHolder} und gibt deses zurück.
     * Hierfür ist der Context notwendig, um einen LayoutInflater zu erzeugen. Mittels diesem wird die XML Ressource
     * history_row_layout geladen und dem Konstrukter der Klasse MyViewHolder mitgegeben.
     *
     * @param parent Übergabeparamter der Klasse {@link ViewGroup}
     * @param viewType Übergabeparameter des Datentyps int
     * @return Es wird ein Objekt der Klasse {@link MyViewHolder} zurückgegeben.
     */
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View rowLayout = inflater.inflate(R.layout.history_row_layout, parent, false);
        return new MyViewHolder(rowLayout);
    }

    /**
     * Die Methode onBindViewHolder bindet die Daten der historyList an die View des MyViewHolder.
     * Zur Orientierung wird die Position in der Liste genommen.
     * Es wird zunächst ein historisches Element erstellt, welches den Index der übergebenen Position hat.
     * Sofern die ID nicht -1 ist, wird die ID des Elements in die TextView formulaId geschrieben.
     * Andernfalls wird der TextView der String 'ID' mitgegeben, da es sich in diesem Fall um die Überschrift handelt.
     * Des weiteren werden die TextViews formulaModi, inputText und solutionText erzeugt und mit entsprechenden Werten belegt.
     *
     * @param holder Übergabeparameter der Klasse {@link MyViewHolder}.
     * @param position finaler int position
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        History history = historyList.get(position);

        TextView formulaId = holder.formulaId;
        if(history.getId()!=-1) {
            formulaId.setText(String.valueOf(history.getId()));
        }else{
            formulaId.setText("ID");
        }
        TextView formulaModi = holder.formulaModi;
        formulaModi.setText(history.getModi());
        TextView inputText = holder.formulaText;
        inputText.setText(history.getFormula());
        TextView solutionText = holder.resultFormulaText;
        solutionText.setText(history.getSecondFormula());
    }

    /**
     * Die Methode getItemCount gibt die länge der Liste historyList zurück.
     * @return Rückgabewert ist vom Typ int
     */
    @Override
    public int getItemCount() {
        return historyList.size();
    }

    /**
     * Die anonyme Klasse MyViewHolder baut die View history_row_layout auf und belegt das itemView mit einem OnClickListener.
     * Die Klasse erbt von der anonymen Klasse {@link RecyclerView.ViewHolder}.
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {

        /**
         * Die TextView formulaId bindet die ID des historischen Elements.
         */
        public TextView formulaId;
        /**
         * Die TextView formulaModi bindet den Modus des historischen Elements.
         */
        public TextView formulaModi;
        /**
         * Die TextView formulaText bindet die erste Formel des historischen Elements.
         */
        public TextView formulaText;
        /**
         * Die TextView resultFormulaText bindet die zweite Formel beziehungsweise das Ergebnis des historischen Elements.
         */
        public TextView resultFormulaText;


        /**
         * Der Konstruktor benötigt die View history_row_layout und belegt diese mit einem OnClickListener, um die aktuelle Position,
         * auf welche geklickt wurde, zu ermitteln und den listener des Adapters auszulösen. Diesem wird das itemView mitgegeben und die Position, auf
         * welche geklickt wurde. Der Effekt ist, dass die Implementierung in {@link HistoryFragment} angestoßend wird und dort mit dem itemView und der Position
         * weitergearbeitet wird.
         * @param itemView Übergabeparameter der Klasse View
         */
        public MyViewHolder(final View itemView) {
            super(itemView);

            this.formulaId=itemView.findViewById(R.id.formulaId);
            this.formulaModi=itemView.findViewById(R.id.formulaModi);
            this.formulaText=itemView.findViewById(R.id.firstFormula);
            this.resultFormulaText=itemView.findViewById(R.id.secondFormula);

            itemView.setOnClickListener(view -> {
                if(listener != null){
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        listener.onItemClick(itemView, position);
                    }
                }
            });
        }
    }
}
