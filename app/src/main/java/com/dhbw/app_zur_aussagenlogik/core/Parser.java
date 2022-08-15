package com.dhbw.app_zur_aussagenlogik.core;

import com.dhbw.app_zur_aussagenlogik.Modi;

public class Parser {

    private static Parser parser = new Parser();

    public static Parser getInstance() {
        return Parser.parser;
    }

    private Modi modus;

    private Formel formula;

    private Formel resultFormula;

    // Methode zum setzen

    public void setParserParameter(Modi modus, String formula) throws ParserException {
        setModus(modus);
        parseFormula(formula);
    }

    public String parseFormula(String formula) throws ParserException{


        // Formel übernehmen
        this.formula = new Formel(formula);

        /*
        Fehlercodes
        -1 = Ungerade Anzahl der Klammern
        42 = alles in Ordnung
         */
        int fehlercode = 0;
        try {
            fehlercode = checkUserInput();
        }catch (Exception e){
            throw new ParserException(fehlercode);
        }

        //char[] knfNormalform = ausaddieren(deMorgan(pfeileAufloesen(this.formulaArray)));
        Formel pfeileAufgeloest = pfeileAufloesen(this.formula);
        Formel deMorgan = deMorgan(pfeileAufgeloest);

        switch (getModus()){
            case KNF:
                resultFormula = Ausaddieren.ausaddieren(deMorgan);
                break;
            case DNF:
                resultFormula = Ausmultiplizieren.ausmultiplizieren(deMorgan);
                break;
            case RESOLUTION:
                break;
            case FORMELN:
                break;
        }
        zeichenersetzungZurueck();
        String resultString = "";
        for(int i = 0; i < resultFormula.length(); i++){
            resultString = resultString + resultFormula.getChar(i);
        }
        return resultString;
    }

    private int checkUserInput(){
        // Zähler für Klamern
        int countOpen = 0;
        int countClose = 0;

        for(int i = 0; i < this.formula.length(); i++){
            char c = this.formula.getChar(i);

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
            /*
            \\u00AC = Negation
            \\u2192 = Pfeil
            \\u2194 = Pfeil beidseitig
            \\u22C1 = Oder
            \\u2227 = Und
             */
            if((i+1)<this.formula.length()) {
                if(Character.toString(c).matches("[a-n]")){
                    if (Character.toString(this.formula.getChar(i + 1)).matches("[a-e(\\u00AC]")){
                        // Fehler: Nach Buchstabe muss ein Operator kommen
                        return -2;
                    }
                }
                else if(Character.compare(c, '(')==0){
                    if(Character.toString(this.formula.getChar(i + 1)).matches("[\\u22C1\\u2227\\u2194\\u2194]")){
                        return -3;
                    }
                }
                else if(Character.compare(c, ')')==0){
                    if(Character.toString(this.formula.getChar(i + 1)).matches("[a-e\\u00AC(]")){
                        return -4;
                    }
                }
                else if(Character.toString(c).matches("[\\u00AC]")){
                    if(Character.toString(this.formula.getChar(i + 1)).matches("[\\u22C1\\u2227\\u2192\\u2194)\\u00AC]")){
                        return -5;
                    }
                }
                else if(Character.toString(c).matches("[\\u22C1\\u2227\\u2192\\u2194]")){
                    if(Character.toString(this.formula.getChar(i + 1)).matches("[\\u2192\\u2194\\u22C1\\u2227)]")){
                        return -6;
                    }
                }
            }

        }
        if(countOpen != countClose){
            // Fehlermeldung
            return -1;
        }

        /*
        Zeichenersetzung
        oder wird +
        und wird *
        -> wird 1
        <-> wird 2
        negation wird n
         */
        for(int i = 0; i < this.formula.length(); i++){

            //Negation
            if(Character.toString(this.formula.getChar(i)).matches("\\u00AC")){
                this.formula.setChar(i, 'n');
            }// Oder
            else if(Character.toString(this.formula.getChar(i)).matches("\\u22C1")){
                this.formula.setChar(i, '+');
            }// Und
            else if(Character.toString(this.formula.getChar(i)).matches("\\u2227")){
                this.formula.setChar(i, '*');
            }// ->
            else if(Character.toString(this.formula.getChar(i)).matches("\\u2192")){
                this.formula.setChar(i, '1');
            }// <->
            else if(Character.toString(this.formula.getChar(i)).matches("\\u2194")){
                this.formula.setChar(i, '2');
            }
        }
        return 42;
    }


    private void zeichenersetzungZurueck(){
        /*
        Zeichenersetzung
        oder wird +
        und wird *
        -> wird 1
        <-> wird 2
        negation wird n
         */

        for(int i = 0; i < this.resultFormula.length(); i++){

            //Negation
            if(this.resultFormula.getChar(i)=='n'){
                this.resultFormula.setChar(i,'\u00AC');
            }// Oder
            else if(this.resultFormula.getChar(i) == '+'){
                this.resultFormula.setChar(i,'\u22C1');
            }// Und
            else if(this.resultFormula.getChar(i)=='*'){
                this.resultFormula.setChar(i, '\u2227');
            }// ->
            else if(this.resultFormula.getChar(i)=='1'){
                this.resultFormula.setChar(i, '\u2192');
            }// <->
            else if(this.resultFormula.getChar(i)=='2'){
                this.resultFormula.setChar(i, '\u2194');
            }
        }
    }

    // Prozedur

    public Formel pfeileAufloesen(Formel formel) {
        Formel bFormel = formel;
        for (int i = 0; i < bFormel.length(); i++) {

            // Block vorne
            char c = bFormel.getChar(i);
            /* 1 = einseitige Implikation
             * 2 = beidseitige Implikation
             */
            if(c == '1' || c == '2') {
                // vorderer Block bei Klammer
                int laengeVordererBlock = 1;
                if(bFormel.getChar(i-1)==')') {
                    int counter = 1;
                    for(int j = 2; counter>0; j++) {
                        char zeichenDavor = bFormel.getChar(i-j);
                        if(zeichenDavor == ')') {
                            counter++;
                        }else if(zeichenDavor=='(') {
                            counter--;
                        }
                        laengeVordererBlock++;
                    }
                }
                // Per Regex
                if(Character.toString(bFormel.getChar(i-1)).matches("[a-n]")) {
                    laengeVordererBlock = 1;
                }
                char[] blockVorne = new char[laengeVordererBlock];
                for (int j = 0; j < blockVorne.length; j++) {
                    blockVorne[j] = bFormel.getChar(i-laengeVordererBlock+j);
                }
                // Block hinten
                int laengeHintererBlock = 1;
                if(bFormel.getChar(i+1)=='(') {
                    int counter = 1;
                    for(int j = 2; counter>0; j++) {
                        char zeichenDanach = bFormel.getChar(i+j);
                        if(zeichenDanach == '(') {
                            counter++;
                        }else if(zeichenDanach==')') {
                            counter--;
                        }
                        laengeHintererBlock++;
                    }
                }
                // Per Regex
                if(Character.toString(bFormel.getChar(i+1)).matches("[a-n]")) {
                    laengeHintererBlock = 1;
                }
                char[] blockHinten = new char[laengeHintererBlock];
                for (int j = 0; j < blockHinten.length; j++) {
                    blockHinten[j] = bFormel.getChar(i+j+1);
                }
                if(c=='1') {
                    bFormel.blockEinsetzen(einseitigeImplikation(blockVorne, blockHinten), i-laengeVordererBlock, i+laengeHintererBlock);
                }else if(c=='2') {
                    bFormel.blockEinsetzen(beidseitigeImplikation(blockVorne, blockHinten), i-laengeVordererBlock, i+laengeHintererBlock);
                }
            }
        }
        return bFormel.klammernPrüfen();
    }

    public Formel deMorgan(Formel formel) {
        Formel bFormel = formel;
        for (int i = 0; i < bFormel.length(); i++) {
            char c = bFormel.getChar(i);
            Formel fDeMorgan = new Formel();
            // Zeichen umdrehen und Buchstaben negieren
            if(c=='n' && bFormel.getChar(i+1)=='('){
                boolean weiterMachen = true;
                int count = i+2;
                int klammern = 1;
                boolean eineKlammerÜberspringen = false;
                fDeMorgan.zeichenHinzufügen('(');
                while(weiterMachen){
                    if(Character.toString(bFormel.getChar(count)).matches("[a-e]")&&bFormel.getChar(count-1)=='n'){
                        fDeMorgan.zeichenHinzufügen(bFormel.getChar(count));
                    }else if(Character.toString(bFormel.getChar(count)).matches("[a-e]")&&bFormel.getChar(count-1)!='n'){
                        fDeMorgan.zeichenHinzufügen('n');
                        fDeMorgan.zeichenHinzufügen(bFormel.getChar(count));
                    }else if(bFormel.getChar(count)=='+'){
                        fDeMorgan.zeichenHinzufügen('*');
                    }else if(bFormel.getChar(count)=='*'){
                        fDeMorgan.zeichenHinzufügen('+');
                    }else if(bFormel.getChar(count)=='('){
                        if(bFormel.klammerNotwendig(count)) {
                            fDeMorgan.zeichenHinzufügen('(');
                            klammern++;
                        }else{
                            eineKlammerÜberspringen=true;
                        }
                    }else if(bFormel.getChar(count)=='n'&&bFormel.getChar(count+1)=='('){
                        count++;
                        continue;
                    }else if(bFormel.getChar(count)==')'){
                        if(!eineKlammerÜberspringen) {
                            fDeMorgan.zeichenHinzufügen(')');
                            klammern--;
                        }else{
                            eineKlammerÜberspringen=false;
                        }
                    }
                    if(klammern==0){
                        break;
                    }
                    count++;
                }

                if(i-1>0 && count+1 <= bFormel.length()){
                    if((bFormel.getChar(i-1)=='1'||bFormel.getChar(i-1)=='2'||bFormel.getChar(i-1)=='*')||
                            (bFormel.getChar(count+1)=='1'||bFormel.getChar(count+1)=='2'||bFormel.getChar(count+1)=='*')){
                        // Klammer muss bestehen bleiben
                        bFormel.blockEinsetzen(fDeMorgan, i, count);
                    }else{
                        // Klammer kann weg gemacht werden
                        Formel neueDeMorgan = new Formel();
                        for (int j = 1; j < fDeMorgan.length()-1; j++){
                            neueDeMorgan.zeichenHinzufügen(fDeMorgan.getChar(j));
                        }
                        bFormel.blockEinsetzen(neueDeMorgan, i, count);
                    }
                }else if(i==0 && count+1 == bFormel.length()){
                    // Klammer unnötig
                    Formel neueDeMorgan = new Formel();
                    for (int j = 1; j < fDeMorgan.length()-1; j++){
                        neueDeMorgan.zeichenHinzufügen(fDeMorgan.getChar(j));
                    }
                    return neueDeMorgan;
                }else if(i>=0 && count+1 < bFormel.length()){
                    Formel neueDeMorgan = new Formel();
                    for (int j = 0; j < fDeMorgan.length(); j++){
                        neueDeMorgan.zeichenHinzufügen(fDeMorgan.getChar(j));
                    }
                    bFormel.blockEinsetzen(neueDeMorgan, i, count);
                }
            }
        }
        return bFormel.klammernPrüfen();
    }


    /*
    Helfer - Methoden
     */

    private Formel einseitigeImplikation(char[] b1, char[] b2) {
        Formel result = new Formel();
        result.zeichenHinzufügen('n');
        for (int i = 0; i < b1.length; i++) {
            result.zeichenHinzufügen(b1[i]);
        }
        result.zeichenHinzufügen('+');
        for (int i = 0; i < b2.length; i++) {
            result.zeichenHinzufügen(b2[i]);
        }
        return result;
    }

    private Formel beidseitigeImplikation(char[] b1, char[] b2) {
        Formel r1 = einseitigeImplikation(b1, b2);
        Formel r2 = einseitigeImplikation(b2, b1);
        Formel result = new Formel();
        result.zeichenHinzufügen('(');
        for (int i = 0; i < r1.length(); i++){
            result.zeichenHinzufügen(r1.getChar(i));
        }
        result.zeichenHinzufügen(')');
        result.zeichenHinzufügen('*');
        result.zeichenHinzufügen('(');
        for (int i = 0; i < r2.length(); i++) {
            result.zeichenHinzufügen(r2.getChar(i));
        }
        result.zeichenHinzufügen(')');
        return result;
    }

    public void setModus(Modi modus){
        this.modus = modus;
    }

    public Modi getModus(){
        return modus;
    }
}
