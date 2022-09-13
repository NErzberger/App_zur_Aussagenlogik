package com.dhbw.app_zur_aussagenlogik.core;

public class ParserException extends Exception{

    private int fehlercode;
    private int[][] truthTable;


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
}
