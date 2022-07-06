package com.dhbw.app_zur_aussagenlogik.parser;

import com.dhbw.app_zur_aussagenlogik.Modi;

public class Parser {

    private static Parser parser = new Parser();

    public static Parser getInstance() {
        return Parser.parser;
    }

    private Modi modus;

    private String formula;
    private char[] formulaArray;

    // Methode zum setzen

    public void setParserParameter(Modi modus, String formula){
        setModus(modus);
        parseFormula(formula);
    }


    public int parseFormula(String formula){
        String unparsedFormula = formula;

        // Formel parsen
        formulaArray = new char[formula.length()];
        for(int i = 0; i<formula.length(); i++){
            formulaArray[i] = formula.charAt(i);
        }

        /*
        Fehlercodes
        -1 = Ungerade Anzahl der Klammern
        42 = alles in Ordnung
         */
        int fehlercode = checkUserInput();

        String parsedFormula = unparsedFormula;
        setFormula(parsedFormula);
        return fehlercode;
    }

    private int checkUserInput(){
        // Zähler für Klamern
        int countOpen = 0;
        int countClose = 0;

        for(int i = 0; i < formulaArray.length; i++){
            char c = formulaArray[i];

            /*
            Character.compare vergleicht die Character nach der Ascii Tabelle
            Größer = 1
            Kleiner = -1
            Gleich = 0
             */
            if(Character.compare(c, '(')==0){
                countOpen++;
            }else if(Character.compare(c, ')')==0){
                countClose++;
            }
            if(Character.toString(c).matches("[a-e]]")){
                if((i+1)<formulaArray.length) {
                    if (Character.toString(formulaArray[i + 1]).matches("[a-e( \\u00AC]")){
                        // Fehler: Nach Buchstabe muss ein Operator kommen
                        return -2;
                    }
                }
            }

        }
        if(countOpen != countClose){
            // Fehlermeldung
            return -1;
        }
        return 42;
    }


    // Getter und Setter

    public void setModus(Modi modus){
        this.modus = modus;
    }

    public Modi getModus(){
        return modus;
    }

    private void setFormula(String formula){
        this.formula = formula;
    }

    public String getFormula(){
        return formula;
    }




}
