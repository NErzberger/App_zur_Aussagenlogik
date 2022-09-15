package com.dhbw.app_zur_aussagenlogik.core;

import com.dhbw.app_zur_aussagenlogik.Modi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Parser {

    private static Parser parser = new Parser();

    public static Parser getInstance() {
        return Parser.parser;
    }

    private Modi modus;

    private Formel formula;

    private Formel resultFormula;

    private List<Formel> rechenweg;

    // Methode zum setzen

    public void setParserParameter(Modi modus, String formula) throws ParserException {
        setModus(modus);
        parseFormula(formula);
    }

    public ArrayList<Character> getVariables(String formulaOne){
        ZweiFormeln zweiFormelnParser = new ZweiFormeln();
        this.formula = new Formel(formulaOne);

        return zweiFormelnParser.checkVariables(formula.getFormel());
    }

    public int[][] parseTwoFormula(String formulaOne, String formulaTwo) throws ParserException{
        int fehlercode = 0;
        try {

            // Formel 1
            this.formula = new Formel(formulaOne);
            fehlercode = checkUserInput();
            Formel f1 = pfeileAufloesen(this.formula).copy();
            Formel f1DeMorgan = deMorgan(f1);
            Formel f1KNF = Ausmultiplizieren.ausmultiplizieren(f1DeMorgan);

            // Formel 2
            this.formula = new Formel(formulaTwo);
            fehlercode = checkUserInput();
            Formel f2 = pfeileAufloesen(this.formula).copy();
            Formel f2DeMorgan = deMorgan(f2);
            Formel f2KNF = Ausmultiplizieren.ausmultiplizieren(f2DeMorgan);

            ZweiFormeln zweiFormelnParser = new ZweiFormeln();
            //Die zwei Formeln haben unterschiedliche Variablen
            if (zweiFormelnParser.compareVariables(f1KNF.getFormel(), f2KNF.getFormel())==false){
                fehlercode = -30;
                ParserException pe = new ParserException(fehlercode);
                throw pe;
            } else if(zweiFormelnParser.compareFormulas(f1KNF.getFormel(), f2KNF.getFormel(), zweiFormelnParser.checkVariables(f1KNF.getFormel()))){
                //Die zwei Formeln stimmen überein
                return zweiFormelnParser.getTruthTable();
            }else{
                // Zwei Formel stimmen nicht über ein
                fehlercode = -20;
                ParserException pe = new ParserException(fehlercode);
                pe.setTruthTable(zweiFormelnParser.getTruthTable());
                pe.setVariables(zweiFormelnParser.checkVariables(f1KNF.getFormel()));
                throw pe;
            }

        }catch (ParserException pe){
            // Bei 2 Formeln ist etwas schief gelaufen -> checkUserInput
            if(fehlercode==0){
                // nicht durch checkUserInput
                fehlercode = -10;
                throw new ParserException(fehlercode);
            }else{
                throw pe;
            }

        }
    }


    public String parseFormula(String formula) throws ParserException{


        // Formel übernehmen
        this.formula = new Formel(formula);

        rechenweg = new ArrayList<>();

        rechenweg.add(new Formel(formula));


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
        Formel rPA = pfeileAufgeloest.copy();

        Formel deMorgan = deMorgan(pfeileAufgeloest);
        Formel rDM = deMorgan.copy();


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
        resultFormula = zeichenersetzungZurueck(resultFormula);
        rechenweg.add(zeichenersetzungZurueck(rPA));
        rechenweg.add(zeichenersetzungZurueck(rDM));
        rechenweg.add(resultFormula);
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




    private Formel zeichenersetzungZurueck(Formel formel){
        /*
        Zeichenersetzung
        oder wird +
        und wird *
        -> wird 1
        <-> wird 2
        negation wird n
         */

        for(int i = 0; i < formel.length(); i++){

            //Negation
            if(formel.getChar(i)=='n'){
                formel.setChar(i,'\u00AC');
            }// Oder
            else if(formel.getChar(i) == '+'){
                formel.setChar(i,'\u22C1');
            }// Und
            else if(formel.getChar(i)=='*'){
                formel.setChar(i, '\u2227');
            }// ->
            else if(formel.getChar(i)=='1'){
                formel.setChar(i, '\u2192');
            }// <->
            else if(formel.getChar(i)=='2'){
                formel.setChar(i, '\u2194');
            }
        }
        return formel;
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
                String vordererBlock = "";
                // vorderer Block bei Klammer

                int innereKlammerLinks = 0;
                int counterLinks = 1;
                while (true) {
                    if(i-counterLinks>=0) {
                        char formelChar = formel.getChar(i - counterLinks);
                        if (formelChar == ')') {
                            innereKlammerLinks++;
                        } else if (formelChar == '(') {
                            innereKlammerLinks--;
                            if (innereKlammerLinks == 0) {
                                vordererBlock = vordererBlock + formelChar;
                                if(i-counterLinks>=1&&formel.getChar(i-counterLinks-1)=='n'){
                                    vordererBlock = vordererBlock + 'n';
                                }
                                break;
                            }else if(innereKlammerLinks==-1){
                                break;
                            }

                        } /*else if (formelChar == '+' && innereKlammerLinks == 0) {
                            break;
                        }*/
                        vordererBlock = vordererBlock + formelChar;
                        counterLinks++;
                    }else{
                        break;
                    }
                }
                vordererBlock = new StringBuilder(vordererBlock).reverse().toString();
                // Block hinten
                String hintererBlock = "";
                int counterRechts = 1;
                int klammernRechts = 0;
                while (true) {
                    if(i+counterRechts<formel.length()) {
                        char formelChar = formel.getChar(i + counterRechts);
                        if (formelChar == '(') {
                            klammernRechts++;
                        } else if (formelChar == ')') {
                            klammernRechts--;
                            if (klammernRechts == 0) {
                                hintererBlock = hintererBlock + formelChar;
                                break;
                            }else if(klammernRechts==-1){
                                break;
                            }
                        } /*else if ((formelChar == '+' || formelChar == '1' || formelChar == '2') && klammernRechts == 0) {
                            break;
                        }*/
                        hintererBlock = hintererBlock + formelChar;
                        counterRechts++;
                    }else{
                        break;
                    }
                }

                if(c=='1') {
                    bFormel.blockEinsetzen(einseitigeImplikation(new Formel(vordererBlock).getFormel(), new Formel(hintererBlock).getFormel()), i-vordererBlock.length(), i+hintererBlock.length());
                    return pfeileAufloesen(bFormel);
                }else if(c=='2') {
                    bFormel.blockEinsetzen(beidseitigeImplikation(new Formel(vordererBlock).getFormel(), new Formel(hintererBlock).getFormel()), i-vordererBlock.length(), i+hintererBlock.length());
                    return pfeileAufloesen(bFormel);
                }
            }
        }
        bFormel.klammernPrüfen();
        bFormel.negationPruefen();
        return bFormel;
    }

    public Formel deMorgan(Formel formel) {

        Formel bFormel = formel;
        Formel fDeMorgan = new Formel();
        boolean deMorganGefunden = false;

        for (int i = 0; i < bFormel.length(); i++) {
            char c = bFormel.getChar(i);

            // Zeichen umdrehen und Buchstaben negieren
            if(c=='n' && bFormel.getChar(i+1)=='('){
                deMorganGefunden = true;
                //int count = i+2;
                int klammern = 0;
                //boolean eineKlammerÜberspringen = false;
                fDeMorgan.zeichenHinzufügen('(');
                i=i+2;
                while(i<formel.length()){
                    if(klammern != 0){
                        fDeMorgan.zeichenHinzufügen(bFormel.getChar(i));
                    }else if(Character.toString(bFormel.getChar(i)).matches("[a-e]")&&bFormel.getChar(i-1)=='n'){
                        fDeMorgan.zeichenHinzufügen(bFormel.getChar(i));
                    }else if(Character.toString(bFormel.getChar(i)).matches("[a-e]")&&bFormel.getChar(i-1)!='n'){
                        fDeMorgan.zeichenHinzufügen('n');
                        fDeMorgan.zeichenHinzufügen(bFormel.getChar(i));
                    }else if(bFormel.getChar(i)=='+'){
                        fDeMorgan.zeichenHinzufügen('*');
                    }else if(bFormel.getChar(i)=='*'){
                        fDeMorgan.zeichenHinzufügen('+');
                    }else if(bFormel.getChar(i)=='(' && bFormel.getChar(i-1)!='n'){
                        fDeMorgan.zeichenHinzufügen('n');
                        fDeMorgan.zeichenHinzufügen('(');
                        klammern++;
                    }else if(bFormel.getChar(i)=='n'&&bFormel.getChar(i+1)=='('){
                        fDeMorgan.zeichenHinzufügen('(');
                        i++;
                        klammern++;
                    }else if(bFormel.getChar(i)==')'){
                        fDeMorgan.zeichenHinzufügen(')');
                        klammern--;
                    }
                    i++;
                }

                /*if(i-1>0 && count+1 <= bFormel.length()){
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
                }*/
            } else {
                fDeMorgan.zeichenHinzufügen(bFormel.getChar(i));
                continue;
            }
            if(deMorganGefunden){
                deMorgan(fDeMorgan);
            }
        }
        fDeMorgan.klammernPrüfen();
        return fDeMorgan;
    }


    /*
    Helfer - Methoden
     */

    private Formel einseitigeImplikation(char[] b1, char[] b2) {
        Formel result = new Formel();
        //result.zeichenHinzufügen('(');
        result.zeichenHinzufügen('n');
        //boolean zusätzlicheKlammer = false;
        //if(b1[0]!='('&&b1.length>1){
        //    zusätzlicheKlammer=true;
            result.zeichenHinzufügen('(');
        //}
        for (int i = 0; i < b1.length; i++) {
            result.zeichenHinzufügen(b1[i]);
        }
        //if(zusätzlicheKlammer){
            result.zeichenHinzufügen(')');
        //}
        result.zeichenHinzufügen('+');
        for (int i = 0; i < b2.length; i++) {
            result.zeichenHinzufügen(b2[i]);
        }
        //result.zeichenHinzufügen(')');
        return result;
    }

    private Formel beidseitigeImplikation(char[] b1, char[] b2) {
        Formel r1 = einseitigeImplikation(b1, b2);
        Formel r2 = einseitigeImplikation(b2, b1);
        Formel result = new Formel();
        //result.zeichenHinzufügen('(');
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
        //result.zeichenHinzufügen(')');
        return result;
    }

    public List<Formel> getRechenweg() {
        return rechenweg;
    }

    public void setRechenweg(List<Formel> rechenweg) {
        this.rechenweg = rechenweg;
    }

    public void setModus(Modi modus){
        this.modus = modus;
    }

    public Modi getModus(){
        return modus;
    }
}
