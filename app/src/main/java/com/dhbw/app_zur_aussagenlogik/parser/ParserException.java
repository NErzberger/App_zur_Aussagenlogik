package com.dhbw.app_zur_aussagenlogik.parser;

public class ParserException extends Exception{

    private int fehlercode;

    public ParserException(int fehlercode){
        this.fehlercode = fehlercode;
    }

    public int getFehlercode(){
        return this.fehlercode;
    }

    public void setFehlercode(int fehlercode){
        this.fehlercode = fehlercode;
    }
}
