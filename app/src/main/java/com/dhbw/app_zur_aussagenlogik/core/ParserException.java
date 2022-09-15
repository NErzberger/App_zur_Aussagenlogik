package com.dhbw.app_zur_aussagenlogik.core;

import java.util.ArrayList;

public class ParserException extends Exception{

    private int fehlercode;
    private int[][] truthTable;
    private ArrayList<Character> variables;


    public ParserException(int fehlercode){
        this.fehlercode = fehlercode;
    }

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
