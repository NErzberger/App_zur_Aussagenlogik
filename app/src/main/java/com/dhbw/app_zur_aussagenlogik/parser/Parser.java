package com.dhbw.app_zur_aussagenlogik.parser;

import com.dhbw.app_zur_aussagenlogik.Modi;

public class Parser {

    private static Parser parser = new Parser();

    public static Parser getInstance() {
        return Parser.parser;
    }

    private Modi modus;

    private String formula;



    // Methode zum setzen

    public void setParserParameter(Modi modus, String formula){
        setModus(modus);
        parseFormula(formula);
    }


    public void parseFormula(String formula){
        String unparsedFormula = formula;
        // Formel parsen

        String parsedFormula = unparsedFormula;
        setFormula(parsedFormula);
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
