package com.dhbw.app_zur_aussagenlogik.core;

import com.dhbw.app_zur_aussagenlogik.Modi;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
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

    public ArrayList<Character> getVariables(String formulaOne) {
        ZweiFormeln zweiFormelnParser = new ZweiFormeln();
        this.formula = new Formel(formulaOne);

        return zweiFormelnParser.checkVariables(formula.getFormel());
    }

    public int[][] parseTwoFormula(String formulaOne, String formulaTwo) throws ParserException {
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
            if (zweiFormelnParser.compareVariables(f1KNF.getFormel(), f2KNF.getFormel()) == false) {
                fehlercode = -30;
                ParserException pe = new ParserException(fehlercode);
                throw pe;
            } else if (zweiFormelnParser.compareFormulas(f1KNF.getFormel(), f2KNF.getFormel(), zweiFormelnParser.checkVariables(f1KNF.getFormel()))) {
                //Die zwei Formeln stimmen überein
                return zweiFormelnParser.getTruthTable();
            } else {
                // Zwei Formel stimmen nicht über ein
                fehlercode = -20;
                ParserException pe = new ParserException(fehlercode);
                pe.setTruthTable(zweiFormelnParser.getTruthTable());
                pe.setVariables(zweiFormelnParser.checkVariables(f1KNF.getFormel()));
                throw pe;
            }

        } catch (ParserException pe) {
          /*  // Bei 2 Formeln ist etwas schief gelaufen -> checkUserInput
            if(fehlercode==0){
                // nicht durch checkUserInput
                fehlercode = -10;
                throw new ParserException(fehlercode);
            }else{*/
            throw pe;
            //}

        }
    }

    public int[][] buildTruthTable(String formula) throws ParserException {
        this.formula = new Formel(formula);

        rechenweg = new ArrayList<>();

        try {
            checkUserInput();
        } catch (ParserException e) {
            throw e;
        }

        //char[] knfNormalform = ausaddieren(deMorgan(pfeileAufloesen(this.formulaArray)));
        Formel pfeileAufgeloest = pfeileAufloesen(this.formula);
        Formel rPA = pfeileAufgeloest.copy();

        Formel deMorgan = deMorgan(pfeileAufgeloest);
        Formel rDM = deMorgan.copy();

        Formel knf = Ausaddieren.ausaddieren(rDM);

        Wertetabelle wertetabelle = new Wertetabelle();
        int[][] truthTable = wertetabelle.createFinishedTruthTable(knf.getFormel(), wertetabelle.checkVariables(knf.getFormel()));
        return truthTable;
    }


    public boolean isTautologie(String formula) {
        return false;
    }


    public String parseFormula(String formula) throws ParserException {


        // Formel übernehmen
        this.formula = new Formel(formula);

        rechenweg = new ArrayList<>();

        rechenweg.add(new Formel(formula));


        /*
        Fehlercodes
        -1 = Ungerade Anzahl der Klammern
        42 = alles in Ordnung
         */

        try {
            checkUserInput();
        } catch (ParserException e) {
            throw e;
        }

        //char[] knfNormalform = ausaddieren(deMorgan(pfeileAufloesen(this.formulaArray)));

        //Formel test = this.formula.copy();
        Formel pfeileAufgeloest = pfeileAufloesen(this.formula);
        pfeileAufgeloest = negationenStreichen(pfeileAufgeloest);
        Formel rPA = pfeileAufgeloest.copy();

        Formel deMorgan = deMorgan(pfeileAufgeloest);
        Formel rDM = deMorgan.copy();


        switch (getModus()) {
            case KNF:
                //resultFormula = Ausaddieren.ausaddieren(test);
                resultFormula = Ausaddieren.ausaddieren(deMorgan);
                break;
            case DNF:
                //resultFormula = Ausmultiplizieren.ausmultiplizieren(test);
                resultFormula = Ausmultiplizieren.ausmultiplizieren(deMorgan);
                break;
        }

        List<char[]> resultList = new ArrayList<>();
        resultList = zeichenErsetzen(resultFormula);
        resultList = teilmengenErsetzten(resultList);
        resultFormula = parseListToFormel(resultList);
        resultFormula = zeichenersetzungZurueck(resultFormula);
        rechenweg.add(zeichenersetzungZurueck(rPA));
        rechenweg.add(zeichenersetzungZurueck(rDM));
        rechenweg.add(resultFormula);
        String resultString = "";
        for (int i = 0; i < resultFormula.length(); i++) {
            resultString = resultString + resultFormula.getChar(i);
        }
        return resultString;
    }

    private int checkUserInput() throws ParserException {
        // Zähler für Klamern
        int countOpen = 0;
        int countClose = 0;

        for (int i = 0; i < this.formula.length(); i++) {
            char c = this.formula.getChar(i);

            /*
            Character.compare vergleicht die Character nach der Ascii Tabelle
            Größer = 1
            Kleiner = -1
            Gleich = 0
             */
            if (Character.compare(c, '(') == 0) {
                countOpen++;
            } else if (Character.compare(c, ')') == 0) {
                countClose++;
            }
            /*
            \\u00AC = Negation
            \\u2192 = Pfeil
            \\u2194 = Pfeil beidseitig
            \\u22C1 = Oder
            \\u2227 = Und
             */
            if ((i + 1) < this.formula.length()) {
                if (Character.toString(c).matches("[a-n]")) {
                    if (Character.toString(this.formula.getChar(i + 1)).matches("[a-e(\\u00AC]")) {
                        // Fehler: Nach Buchstabe muss ein Operator kommen
                        throw new ParserException(-2);
                    }
                } else if (c == '(') {
                    if (Character.toString(this.formula.getChar(i + 1)).matches("[\\u22C1\\u2227\\u2194\\u2192]")) {
                        throw new ParserException(-3);
                    } else if (i + 1 == formula.length()) {
                        throw new ParserException(-8);
                    }
                } else if (c == ')') {
                    if (Character.toString(this.formula.getChar(i + 1)).matches("[a-e\\u00AC(]")) {
                        throw new ParserException(-4);
                    } else if (i == 0) {
                        throw new ParserException(-9);
                    }
                }
                // Es darf kein Operator auf eine negation folgen
                else if (Character.toString(c).matches("[\\u00AC]")) {
                    if (Character.toString(this.formula.getChar(i + 1)).matches("[\\u22C1\\u2227\\u2192\\u2194)]")) {
                        throw new ParserException(-5);
                    }
                } else if (Character.toString(c).matches("[\\u22C1\\u2227\\u2192\\u2194]")) {
                    if (Character.toString(this.formula.getChar(i + 1)).matches("[\\u2192\\u2194\\u22C1\\u2227)]")) {
                        throw new ParserException(-6);
                    } else if (i == 0) {
                        throw new ParserException(-7);
                    }
                }
            } else if (i + 1 == formula.length()) {
                // keine öffnende Klammer an letzter Stelle
                if (c == '(') {
                    throw new ParserException(-8);
                }
                // keine Negation an letzter Stelle
                else if (Character.toString(c).matches("[\\u00AC]")) {
                    throw new ParserException(-11);
                }
                // Kein Operator an letzter Stelle
                else if (Character.toString(c).matches("[\\u22C1\\u2227\\u2192\\u2194]")) {
                    throw new ParserException(-12);
                }
            }

        }
        if (countOpen != countClose) {
            // Fehlermeldung
            throw new ParserException(-1);
        }

        /*
        Zeichenersetzung
        oder wird +
        und wird *
        -> wird 1
        <-> wird 2
        negation wird n
         */
        for (int i = 0; i < this.formula.length(); i++) {

            //Negation
            if (Character.toString(this.formula.getChar(i)).matches("\\u00AC")) {
                this.formula.setChar(i, 'n');
            }// Oder
            else if (Character.toString(this.formula.getChar(i)).matches("\\u22C1")) {
                this.formula.setChar(i, '+');
            }// Und
            else if (Character.toString(this.formula.getChar(i)).matches("\\u2227")) {
                this.formula.setChar(i, '*');
            }// ->
            else if (Character.toString(this.formula.getChar(i)).matches("\\u2192")) {
                this.formula.setChar(i, '1');
            }// <->
            else if (Character.toString(this.formula.getChar(i)).matches("\\u2194")) {
                this.formula.setChar(i, '2');
            }
        }

        this.formula = negationenStreichen(this.formula);

        return 42;
    }


    public Formel zeichenersetzungZurueck(Formel formel) {
        /*
        Zeichenersetzung
        oder wird +
        und wird *
        -> wird 1
        <-> wird 2
        negation wird n
         */

        for (int i = 0; i < formel.length(); i++) {

            //Negation
            if (formel.getChar(i) == 'n') {
                formel.setChar(i, '\u00AC');
            }// Oder
            else if (formel.getChar(i) == '+') {
                formel.setChar(i, '\u22C1');
            }// Und
            else if (formel.getChar(i) == '*') {
                formel.setChar(i, '\u2227');
            }// ->
            else if (formel.getChar(i) == '1') {
                formel.setChar(i, '\u2192');
            }// <->
            else if (formel.getChar(i) == '2') {
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
            if (c == '1' || c == '2') {
                String vordererBlock = "";
                // vorderer Block bei Klammer

                int innereKlammerLinks = 0;
                int counterLinks = 1;
                while (true) {
                    if (i - counterLinks >= 0) {
                        char formelChar = formel.getChar(i - counterLinks);
                        if (formelChar == ')') {
                            innereKlammerLinks++;
                        } else if (formelChar == '(') {
                            innereKlammerLinks--;
                            if (innereKlammerLinks == 0) {
                                vordererBlock = vordererBlock + formelChar;
                                if (i - counterLinks >= 1 && formel.getChar(i - counterLinks - 1) == 'n') {
                                    vordererBlock = vordererBlock + 'n';
                                }
                                break;
                            } else if (innereKlammerLinks == -1) {
                                break;
                            }

                        } /*else if (formelChar == '+' && innereKlammerLinks == 0) {
                            break;
                        }*/
                        vordererBlock = vordererBlock + formelChar;
                        counterLinks++;
                    } else {
                        break;
                    }
                }
                vordererBlock = new StringBuilder(vordererBlock).reverse().toString();
                // Block hinten
                String hintererBlock = "";
                int counterRechts = 1;
                int klammernRechts = 0;
                while (true) {
                    if (i + counterRechts < formel.length()) {
                        char formelChar = formel.getChar(i + counterRechts);
                        if (formelChar == '(') {
                            klammernRechts++;
                        } else if (formelChar == ')') {
                            klammernRechts--;
                            if (klammernRechts == 0) {
                                hintererBlock = hintererBlock + formelChar;
                                break;
                            } else if (klammernRechts == -1) {
                                break;
                            }
                        } /*else if ((formelChar == '+' || formelChar == '1' || formelChar == '2') && klammernRechts == 0) {
                            break;
                        }*/
                        hintererBlock = hintererBlock + formelChar;
                        counterRechts++;
                    } else {
                        break;
                    }
                }

                if (c == '1') {
                    bFormel.blockEinsetzen(einseitigeImplikation(new Formel(vordererBlock).getFormel(), new Formel(hintererBlock).getFormel()), i - vordererBlock.length(), i + hintererBlock.length());
                    return pfeileAufloesen(bFormel);
                } else if (c == '2') {
                    bFormel.blockEinsetzen(beidseitigeImplikation(new Formel(vordererBlock).getFormel(), new Formel(hintererBlock).getFormel()), i - vordererBlock.length(), i + hintererBlock.length());
                    return pfeileAufloesen(bFormel);
                }
            }
        }
        bFormel.klammernPrüfen();

        return bFormel;
    }

    public Formel deMorgan(Formel formel) {

        Formel bFormel = formel;
        Formel fDeMorgan = new Formel();
        boolean deMorganGefunden = false;

        for (int i = 0; i < bFormel.length(); i++) {
            char c = bFormel.getChar(i);

            // Zeichen umdrehen und Buchstaben negieren
            if (c == 'n' && bFormel.getChar(i + 1) == '(') {
                deMorganGefunden = true;
                //int count = i+2;
                int klammern = 0;
                //boolean eineKlammerÜberspringen = false;
                fDeMorgan.zeichenHinzufügen('(');
                i = i + 2;
                while (i < formel.length()) {
                    if (klammern != 0) {
                        fDeMorgan.zeichenHinzufügen(bFormel.getChar(i));
                    } else if (Character.toString(bFormel.getChar(i)).matches("[a-e]") && bFormel.getChar(i - 1) == 'n') {
                        fDeMorgan.zeichenHinzufügen(bFormel.getChar(i));
                    } else if (Character.toString(bFormel.getChar(i)).matches("[a-e]") && bFormel.getChar(i - 1) != 'n') {
                        fDeMorgan.zeichenHinzufügen('n');
                        fDeMorgan.zeichenHinzufügen(bFormel.getChar(i));
                    } else if (bFormel.getChar(i) == '+') {
                        fDeMorgan.zeichenHinzufügen('*');
                    } else if (bFormel.getChar(i) == '*') {
                        fDeMorgan.zeichenHinzufügen('+');
                    } else if (bFormel.getChar(i) == '(' && bFormel.getChar(i - 1) != 'n') {
                        fDeMorgan.zeichenHinzufügen('n');
                        fDeMorgan.zeichenHinzufügen('(');
                        klammern++;
                    } else if (bFormel.getChar(i) == 'n' && bFormel.getChar(i + 1) == '(') {
                        fDeMorgan.zeichenHinzufügen('(');
                        i++;
                        klammern++;
                    } else if (bFormel.getChar(i) == ')') {
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
            if (deMorganGefunden) {
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
        for (int i = 0; i < r1.length(); i++) {
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


    /**
     * Diese Mehtode streicht unnötige Negationen aus der Formel. Z.B. zwei Negationen hintereinander
     * gleichen sich aus (gerade Anzahl) und bei drei Neagationen hintereinander braucht man nur
     * eine Negation (ungerade Anzahl)
     *
     * @param formel
     * @return Neue überprüfte Formel ohne unnötige Negationen
     */
    public Formel negationenStreichen(Formel formel) {

        //Neue überprüfte Formel ohne unnötige Negationen
        Formel provedFormula = new Formel();

        //Wir gehen durch unsere Formel durch und streichen unnötige Negationen
        for (int i = 0; i < formel.length(); i++) {

            /*
            Wenn wir eine Negation finden, prüfen wir das nächste Zeichen. Wenn dieses auch eine
            Negation ist, überspringen wir einen Index und schreiben nichts in unsere provedFormula.
            Wenn das nächste Zeichen keine Negation mehr ist, ist die Negation notwendig und wir
             schreiben die Negation in unsere proved Formula.
            */
            if (i < formel.length() - 1) {
                if (formel.getChar(i) == 'n' && formel.getChar(i + 1) == 'n') {
                    i++;
                } else {
                    provedFormula.zeichenHinzufügen(formel.getChar(i));
                }
            } else {
                provedFormula.zeichenHinzufügen(formel.getChar(i));
            }


        }

        return provedFormula;
    }

    /**
     * In dieser Methode sollen doppelte Variablen und gegensätzliche Variablen in der Lösungsmenge
     * gestrichen werden (z.b. nicht a und a; nicht a oder a).
     *
     * @param formel
     * @return
     */
    public List<char[]> zeichenErsetzen(Formel formel) {

        //Hier werden unnötige Negationen gestrichen
        formel = negationenStreichen(formel);

        List<char[]> gesamtmenge = new ArrayList<>();
        // neue Teilmenge
        Formel teilmenge = new Formel();

        // Es wird die Formel ausgelesen und die Teilmengen gebildet und die Teilmengen in die Gesamtmenge hinzugefügt
        for (int i = 0; i < formel.length(); i++) {
            if (Character.toString(formel.getChar(i)).matches("[a-n]")) {
                teilmenge.zeichenHinzufügen(formel.getChar(i));
            } else if (modus==Modi.KNF&&formel.getChar(i) == '*'
                    ||modus==Modi.DNF&&formel.getChar(i)=='+'
                    ||modus==Modi.RESOLUTION&&formel.getChar(i)=='*') {
                gesamtmenge.add(teilmenge.getFormel());
                teilmenge = new Formel();
            }
        }
        gesamtmenge.add(teilmenge.getFormel());

        return zeichenErsetzen(gesamtmenge);

    }

    public List<char[]> zeichenErsetzen(List<char[]> formel) {
        // Es wird eine Gesamtmenge gebildet mit allen Teilmengen drin
        List<Formel> gesamtmenge = new ArrayList<>();
        formel.stream().forEach(f -> gesamtmenge.add(new Formel(f)));
        //Unsere Ergebnismenge
        List<Formel> endmenge = new ArrayList<>();

        for (int i = 0; i < gesamtmenge.size(); i++) {

            //Hier holen wir unsere Teilmenge
            Formel menge = gesamtmenge.get(i);
            Formel newMenge = new Formel();

            //Hier überprüfen wir die Teilmenge und schreiben nur die Variablen in newMenge (neue
            //Teilmenge), welche nicht mehrfach vorkommen
            for (int j = 0; j < menge.length(); j++) {
                if (menge.getChar(j) == 'n') {
                    continue;
                }
                boolean match = false;

                //j+1, damit wir nicht das Zeichen mit sich selbst vergleichen
                for (int k = j + 1; k < menge.length(); k++) {

                    // wenn j ungleich k (darf nicht mit sich selbst verglichen werden)
                    // und der Buchstabe an der Stelle j dem Buchstabe an Stelle k entspricht und k kein n ist
                    // dann ist der Buchstabe an der Stelle j doppelt
                    if (j != k && menge.getChar(j) == menge.getChar(k) && menge.getChar(k) != 'n') {
                        if (j > 0) {
                            if (menge.getChar(j - 1) == 'n' && menge.getChar(k - 1) == 'n' ||
                                    menge.getChar(j - 1) != 'n' && menge.getChar(k - 1) != 'n') {
                                match = true;
                            }
                        } else if (j == 0) {
                            // j kann nur positiv sein, daher muss nur geprüft werden, ob das k positiv ist
                            if (menge.getChar(k - 1) != 'n') {
                                match = true;
                            }
                        }
                    }
                }
                if (!match) {
                    if (j > 0 && menge.getChar(j - 1) == 'n') {
                        newMenge.zeichenHinzufügen('n');
                    }
                    newMenge.zeichenHinzufügen(menge.getChar(j));
                }
            }

            // Hier wird die neue Teilmenge sortiert für eine schönere Darstellung.
            // Zur Sortierung konvertieren wir unsere möglichen Zeichen in die Zahlen 1 - 9.
            String convertedFormel = "";
            for (int j = 0; j < newMenge.length(); j++) {
                if (newMenge.getChar(j) == 'a') {
                    if (j > 0 && newMenge.getChar(j - 1) == 'n') {
                        convertedFormel = convertedFormel + "1";
                    } else {
                        convertedFormel = convertedFormel + "0";
                    }
                }
                if (newMenge.getChar(j) == 'b') {
                    if (j > 0 && newMenge.getChar(j - 1) == 'n') {
                        convertedFormel = convertedFormel + "3";
                    } else {
                        convertedFormel = convertedFormel + "2";
                    }
                }
                if (newMenge.getChar(j) == 'c') {
                    if (j > 0 && newMenge.getChar(j - 1) == 'n') {
                        convertedFormel = convertedFormel + "5";
                    } else {
                        convertedFormel = convertedFormel + "4";
                    }
                }
                if (newMenge.getChar(j) == 'd') {
                    if (j > 0 && newMenge.getChar(j - 1) == 'n') {
                        convertedFormel = convertedFormel + "7";
                    } else {
                        convertedFormel = convertedFormel + "6";
                    }
                }
                if (newMenge.getChar(j) == 'e') {
                    if (j > 0 && newMenge.getChar(j - 1) == 'n') {
                        convertedFormel = convertedFormel + "9";
                    } else {
                        convertedFormel = convertedFormel + "8";
                    }
                }
            }

            // Die konvertierte Teilmenge sortieren
            char[] c = convertedFormel.toCharArray();
            Arrays.sort(c);
            String sortedString = new String(c);

            // Prüfen, ob die Teilmenge "unnötig" ist. Teilmenge ist unnötig, wenn z.B.
            // nicht a und a drin steht
            if (sortedString.contains("0") && sortedString.contains("1")) {
                continue;
            } else if (sortedString.contains("2") && sortedString.contains("3")) {
                continue;
            } else if (sortedString.contains("4") && sortedString.contains("5")) {
                continue;
            } else if (sortedString.contains("6") && sortedString.contains("7")) {
                continue;
            } else if (sortedString.contains("8") && sortedString.contains("9")) {
                continue;
            }


            // Die konvertierte sortierte Teilmenge zurück in Buchstaben konvertieren
            Formel sortedFormel = new Formel();
            for (int j = 0; j < sortedString.length(); j++) {
                int charValue = Integer.parseInt(String.valueOf(sortedString.charAt(j)));
                switch (charValue) {
                    case 0:
                        sortedFormel.zeichenHinzufügen('a');
                        break;
                    case 1:
                        sortedFormel.zeichenHinzufügen('n');
                        sortedFormel.zeichenHinzufügen('a');
                        break;
                    case 2:
                        sortedFormel.zeichenHinzufügen('b');
                        break;
                    case 3:
                        sortedFormel.zeichenHinzufügen('n');
                        sortedFormel.zeichenHinzufügen('b');
                        break;
                    case 4:
                        sortedFormel.zeichenHinzufügen('c');
                        break;
                    case 5:
                        sortedFormel.zeichenHinzufügen('n');
                        sortedFormel.zeichenHinzufügen('c');
                        break;
                    case 6:
                        sortedFormel.zeichenHinzufügen('d');
                        break;
                    case 7:
                        sortedFormel.zeichenHinzufügen('n');
                        sortedFormel.zeichenHinzufügen('d');
                        break;
                    case 8:
                        sortedFormel.zeichenHinzufügen('e');
                        break;
                    case 9:
                        sortedFormel.zeichenHinzufügen('n');
                        sortedFormel.zeichenHinzufügen('e');
                        break;
                }
            }
            endmenge.add(sortedFormel);
        }

        ArrayList<char[]> provedFormel = new ArrayList<>();
        endmenge.stream().forEach(a -> provedFormel.add(a.getFormel()));

        return provedFormel;
    }

    public List<char[]> teilmengenErsetzten(List<char[]> list){

        List<char[]> provedFormula = new ArrayList<>();

        for(int i = list.size()-1; i>0; i--){
            boolean match = false;
            for(int j = 0; j<list.size(); j++){
                if(i!=j){
                    if(list.get(i).length == list.get(j).length){

                        for(int k = 0; k<list.get(i).length; k++){
                            if(list.get(i)[k]!=list.get(j)[k]){
                                match = false;
                                break;
                            }else{
                                match = true;
                            }
                        }
                        if(match){
                            break;
                        }
                    }
                }
            }
            if(match==false){
                provedFormula.add(list.get(i));
            }
        }
        provedFormula.add(list.get(0));
        Collections.reverse(provedFormula);

        return provedFormula;
    }


    public Formel parseListToFormel(List<char[]> list){

        Formel formel = new Formel();

        for (int i = 0; i < list.size(); i++) {
            formel.zeichenHinzufügen('(');
            for (int j = 0; j < list.get(i).length; j++) {
                formel.zeichenHinzufügen(list.get(i)[j]);
                if(list.get(i)[j]!='n') {
                    if (j < list.get(i).length - 1) {
                        if(modus==Modi.DNF){
                            formel.zeichenHinzufügen('*');
                        }else if(modus==Modi.KNF || modus==Modi.RESOLUTION){
                            formel.zeichenHinzufügen('+');
                        }
                    }
                }
            }
            formel.zeichenHinzufügen(')');
            if (i < list.size() - 1) {
                if(modus==Modi.DNF){
                    formel.zeichenHinzufügen('+');
                }else if(modus==Modi.KNF || modus==Modi.RESOLUTION){
                    formel.zeichenHinzufügen('*');
                }
            }
        }

        return formel;
    }

    public List<char[]> parseFormelToList(Formel formel){

        List<char[]> gesamtmenge = new ArrayList<>();
        // neue Teilmenge
        Formel teilmenge = new Formel();

        // Es wird die Formel ausgelesen und die Teilmengen gebildet und die Teilmengen in die Gesamtmenge hinzugefügt
        for (int i = 0; i < formel.length(); i++) {
            if (Character.toString(formel.getChar(i)).matches("[a-n]")) {
                teilmenge.zeichenHinzufügen(formel.getChar(i));
                } else if (modus==Modi.KNF&&formel.getChar(i) == '*'
                        ||modus==Modi.DNF&&formel.getChar(i)=='+'
                        ||modus==Modi.RESOLUTION&&formel.getChar(i)=='*') {
                gesamtmenge.add(teilmenge.getFormel());
                teilmenge = new Formel();
            }
        }
        gesamtmenge.add(teilmenge.getFormel());

        return gesamtmenge;
    }

    public List<Formel> getRechenweg() {
        return rechenweg;
    }

    public void setRechenweg(List<Formel> rechenweg) {
        this.rechenweg = rechenweg;
    }

    public void setModus(Modi modus) {
        this.modus = modus;
    }

    public Modi getModus() {
        return modus;
    }
}
