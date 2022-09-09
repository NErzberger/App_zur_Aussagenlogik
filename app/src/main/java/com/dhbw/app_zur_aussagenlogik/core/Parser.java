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
                                break;
                            }
                        } else if (formelChar == '+' && innereKlammerLinks == 0) {
                            break;
                        }
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
                            if (klammernRechts == 0) {
                                break;
                            }
                            klammernRechts--;
                        } else if ((formelChar == '+' || formelChar == '1' || formelChar == '2') && klammernRechts == 0) {
                            break;
                        }
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
        return bFormel;
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
        result.zeichenHinzufügen('(');
        result.zeichenHinzufügen('n');
        for (int i = 0; i < b1.length; i++) {
            result.zeichenHinzufügen(b1[i]);
        }
        result.zeichenHinzufügen('+');
        for (int i = 0; i < b2.length; i++) {
            result.zeichenHinzufügen(b2[i]);
        }
        result.zeichenHinzufügen(')');
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

    private char[] zeichenHinzufügen(char[] origin, char z) {
        char[] newZeichenSatz = new char[origin.length + 1];
        for (int i = 0; i < origin.length; i++) {
            newZeichenSatz[i] = origin[i];
        }
        newZeichenSatz[origin.length] = z;
        return newZeichenSatz;
    }


        /*
    #############################################################
    Anfang: 2 Formeln vergleichen
    #############################################################
     */

    //Wertetabelle erstellen ohne Ergebnisspalte
    private int[][] createTruthTable(int n) {

        int rows = (int) Math.pow(2,n);
        //n+2 weil wir 2 weitere Ergebnisspalten für 2 Formeln brauchen
        int[][] truthTable = new int[n+2][rows];


        for (int i=0; i<rows; i++) {
            for (int j=0; j<n; j++) {
                truthTable[j][i]= (i/(int) Math.pow(2, j))%2;
            }
        }

        return truthTable;

    }

    //Checken, welche Variablen verwendet werden
    private ArrayList<Character> checkVariables(char[] formel) {

        int foundA = 0;
        int foundB = 0;
        int foundC = 0;
        int foundD = 0;
        int foundE = 0;

        ArrayList<Character> variables = new ArrayList<Character>();

        for(int i=0; i<formel.length; i++) {
            if(formel[i]=='a' && foundA==0) {
                foundA = 1;
                variables.add('a');
            }
            if(formel[i]=='b' && foundB==0) {
                foundB = 1;
                variables.add('b');
            }
            if(formel[i]=='c' && foundC==0) {
                foundC = 1;
                variables.add('c');
            }
            if(formel[i]=='d' && foundD==0) {
                foundD = 1;
                variables.add('d');
            }
            if(formel[i]=='e' && foundE==0) {
                foundE = 1;
                variables.add('e');
            }
        }

        Collections.sort(variables);

        return variables;
    }


    /**
     * Diese Methode soll zwei Formeln miteinander anhand einer Wertetabelle vergleichen.
     *
     * @param formel1 erste Formel
     * @param formel2 zweite Formel
     * @param variables Liste mit den Variablen aus den beiden Formeln
     */
    private boolean compareFormulas(char[] formel1, char[] formel2, ArrayList<Character> variables) {

        boolean result = true;

        //Aus der Anzahl der Variablen, ergibt sich die Anzahl der Reihen der Wertetabelle 2^n.
        //Die Anzahl der Reihen ist die maximale Anzahl an Durchläufen, um die beiden Formeln zu vergleichen.
        int rows = (int) Math.pow(2,variables.size());

        //Hier wird eine Wertetabelle ohne Ergebnisspalte erstellt
        int[][] truthTable = createTruthTable(variables.size());


        //In der List row soll die Werte der aktuellen Zeile der Wertetabelle zwischengespeichert werden, damit
        //die beiden Formeln anhand dieser Zeile verglichen werden können
        int[] row = new int[variables.size()];

        /*
         * Die Schleife wird maximal so häufig ausgeführt, wie es Zeilen in der Wertetabelle gibt. Sie kann früher abbrechen,
         * wenn die beiden Formel schon früher nicht übereinstimmen.
         * In der Liste row wird Zeile für Zeile zwischengespeichert, damit die Werte aus der betroffenen Zeile
         * in die beiden Formeln eingefuegt werden können.
         */
        for(int i=0; i<rows; i++) {

            //Mit diesen beiden Arrays wird gerechnet
            char[] formelForWork1 = formel1.clone();
            char[] formelForWork2 = formel2.clone();


            //Wir benötigen nach und nach die Werte aus jeder Zeile der Wertetabelle
            //Hier werden die Werte für eine Zeile aus der Wertetabelle geholt
            for(int j=0; j<variables.size(); j++) {
                row[j] = truthTable[j][i];
            }


            //Die Variablen aus beiden Formeln werden durch die Werte aus der aktuellen Zeile der Wertetabelle ersetzt.
            formelForWork1 = werteInFormelnEinfuegen(formel1, row, variables);
            formelForWork2 = werteInFormelnEinfuegen(formel2, row, variables);


            //Negative Werte werden umgedreht (-0 zu 1 und -1 zu 0)
            ArrayList<Character> formel11 = negativeZeichenErsetzen(formelForWork1);
            ArrayList<Character> formel22 = negativeZeichenErsetzen(formelForWork2);


            //In diese beiden Integer kommt das Ergebnis der beiden Formeln 0 (falsch) oder 1 (richtig)
            int formel111 = ausrechnen(formel11);
            int formel222 = ausrechnen(formel22);


            //Wenn die beiden Ergebnisse nicht übereinstimmen für die eingesetzten Werte aus der Zeile der Wertetabelle,
            //dann stimmen die beiden Formeln nicht überein
            if(formel111 != formel222) {
                result = false;
            }


            //Hier werden die Ergebnisfelder für die beiden Formeln der Wertetabelle nach und nach gefüllt
            truthTable[variables.size()][i] = formel111;
            truthTable[variables.size()+1][i] = formel222;

        }

        return result;

    }


    private int ausrechnen(ArrayList<Character> formel) {

        char charResult = '2';
        int integerResult = 1;
        int counterUnd = 0;

        //Hier wird gecheckt, ob es überhaupt eine logische UND Verknüpfung gibt und wenn ja, wie viele
        for(char c : formel) {
            if(c == '*') {
                counterUnd++;
            }
        }

        //Falls die übergebene Formel nur aus einer Variablen besteht
        if(counterUnd == 0){
            charResult = formel.get(0);
            if(charResult == '0') {
                integerResult = 0;
                return integerResult;
            }else if(charResult == '1') {
                integerResult = 1;
                return integerResult;
            }
        }


        /*
         * Alles ab hier:
         * Falls die übergebene Formel aus mehreren Blöcken besteht, welche UND verknüpft sind
         */
        int blockstart = 0;
        int blockende = 0;

        //Ein Block geht immer von einem logischem UND bis zum nächsten logischen UND
        for(int i=0; i<=counterUnd; i++) {

            //Hier wird das Blockende gefunden
            if(i<counterUnd) {
                for(int j=blockstart; j<formel.size(); j++) {

                    if(formel.get(j) == '*') {
                        blockende = j;
                        break;
                    }

                }
            } else {
                //Den letzten Block kann man nicht mehr an einem logischen UND erkennen
                blockende = formel.size()-1;
            }

            List<Character> block;

            if(blockende == formel.size()-1) {
                //Returns a view of the portion of this list between the specified fromIndex, inclusive, and toIndex, exclusive.
                block = formel.subList(blockstart, blockende + 1);
            } else {
                block = formel.subList(blockstart, blockende);
            }


            //Wenn der Block keine 1 enthält, ist das Ergebnis der übergebene Formel 0, da alle Blöcke mit einem
            //logischen UND verknüpft sind. Die Schleife kann dann sofort abgebrochen werden.
            if(block.contains('1') == false) {
                integerResult = 0;
                break;
            } else {
                blockstart = blockende + 1;
            }


        }

        return integerResult;
    }



    /**
     * Hie werden die Werte aus der betroffenen Zeile der Wertetabelle in die betroffene Formel eingefügt
     *
     * @param originalFormel ist die betroffene Formel
     * @param values dies sind die Werte, welche in die Formel eingefügt werden
     * @param variables die sind die Variablen, damit man weiß, welche Werte in welche Variable gehören
     * @return das Ergebnis ist die Formel, bei der die Variablen durch 0 und 1 ersetzt wurden
     */
    private char[] werteInFormelnEinfuegen(char[] originalFormel, int[] values, ArrayList<Character> variables) {

        //Somit wird die eigentliche Formel nicht verändert
        char[] formel = originalFormel.clone();

        //Dieser Index wird immer wieder hochiteriert und dann wieder auf 0 gesetzt.
        //Damit weiß man später, welchen Wert man aus der values-Liste nehmen muss für die bestimmte Variable.
        int index = 0;

        //Hier geht man durch die Formel Zeichen für Zeichen
        for(int i=0; i<formel.length; i++) {

            index = 0;

            //Hier checkt man, ob das aktuelle Zeichen aus der Formel eine Variable ist.
            //Wenn dies der Fall ist, wird ihr ihr Wert zugewiesen (0 oder 1).
            for(char c : variables) {

                if(formel[i] == c) {

                    formel[i] = Character.forDigit(values[index],2);
                    break;
                }
                index++;
            }
        }

        return formel;
    }


    /**
     * Hier wird geprüft, ob vor einer 1 oder einer 0 eine Negation steht. Wenn die der Fall ist, wird aus der negierten
     * 1 eine 0 oder aus der negierten 0 eine 1.
     * @param formel die Formel, welcher schon Werte zugewiesen wurden.
     * @return Das Ergebnis ist eine fertige mit Werten eingesetzte Formel ohne Negationen
     */
    private ArrayList<Character> negativeZeichenErsetzen(char[] formel) {

        //Hier wird lediglich durch die Formel mit eingesetzten 1ern und 0ern durchiteriert und geprüft, ob vor der 1
        //oder 0 eine Negation steht. Wenn die der Fall ist wird eine 1 zu einer 0 und eine 0 zu einer 1.
        //Die Negationszeichen fallen ein paar Zeilen weiter unten raus.
        for(int i=0; i<formel.length; i++) {

            if(formel[i] == '-' && formel[i+1] == '1') {

                formel[i+1] = '0';

            } else if(formel[i] == '-' && formel[i+1] == '0') {

                formel[i+1] = '1';

            }

        }

        //Liste ohne Negationen mehr
        ArrayList<Character> changedFormel = new ArrayList<Character>();

        //Hier werden alle Zeichen, ausser Negationszeichen, in die neue Liste kopiert
        for(char c : formel) {
            if(c != '-') {
                changedFormel.add(c);
            }
        }

        return changedFormel;
    }


    /*
    #############################################################
    Ende: 2 Formeln vergleichen
    #############################################################
     */

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
