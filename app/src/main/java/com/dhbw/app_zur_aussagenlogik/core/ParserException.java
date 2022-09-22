package com.dhbw.app_zur_aussagenlogik.core;

import java.util.ArrayList;

/**
 * Die Klasse <b>ParserException</b> ist eine Exception. Entsprechend erbt sie von der Klasse {@link Exception} und wird für die
 * Fehlerbehandlung verwendet.
 */
public class ParserException extends Exception{

    /**
     * Das Klassenattribut fehlercode ist vom Typ int. Es wird verwendet, um eine dazugehörende Fehlermeldung
     * aus der Klasse {@link ErrorHandler} abfragen zu können.
     */
    private int fehlercode;

    /**
     * Das Klassenattribut truthTable ist vom Typ ein zweidimensionales int Array.
     * Das Attribut wird verwendet, um eine Wertetabelle mitgeben zu können, wenn im Bereich der
     * 2 Formeln oder Wertetabelle ein Fehler auftreten sollte.
     */
    private int[][] truthTable;

    /**
     * Zum Klassenattribut truthTable gehört das Klassenattribut variables.
     */
    private ArrayList<Character> variables;

    /**
     * Zum werfen einer <b>ParserException</b> ist ein int für den Fehlercode notwendig.
     * @param fehlercode
     */
    public ParserException(int fehlercode){
        this.fehlercode = fehlercode;
    }

    /*
    **********************************************************************************
    *
    *  Getter und Setter
    *
    **********************************************************************************
     */

    public int getFehlercode(){
        return this.fehlercode;
    }

    public void setFehlercode(int fehlercode){
        this.fehlercode = fehlercode;
    }

    public int[][] getTruthTable() {
        return truthTable;
    }

    public void setTruthTable(int[][] truthTable) {
        this.truthTable = truthTable;
    }

    public ArrayList<Character> getVariables() { return variables; }

    public void setVariables(ArrayList<Character> variables) { this.variables = variables; }
}
