package com.dhbw.app_zur_aussagenlogik.core;

import com.dhbw.app_zur_aussagenlogik.Modi;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * Die Klasse <b>Parser</b> steuert die Logik der Aussagenlogik.
 * Sie wird aus dem Main Fragment heraus aufgerufen und löst die Formel je nach Modus.
 */
public class Parser {

    /**
     * Statische private Instanz eines Parser Ojektes
     */
    private static Parser parser = new Parser();

    /**
     * Die Mehtode getInstance gibt die Instanz des Parser Objektes zurück.
     * @return Es wird die Parser Instanz zurück gegeben.
     */
    public static Parser getInstance() {
        return Parser.parser;
    }

    /**
     * Über den modus wird das jeweilige Verfahren angesteuert und im späteren Veralauf die Formel anders bearbeitet.
     */
    private Modi modus;

    /**
     * Die Formel, mit welcher der Parser gestartet wird.
     */
    private Formel formula;

    /**
     * Die Ergebnis Formel
     */
    private Formel resultFormula;

    /**
     * Um den Rechenweg festzuhalten wird eine Liste mit Formeln verwendet, in der die Ergebnisse
     * der Zwischenschritte eingetragen werden.
     */
    private List<Formel> rechenweg;

    /**
     * Mit der Mehtode parseFormula wird eine Formel als String eingegeben und im jeweiligen Modus
     * in die KNF oder DNF Überführt.
     * @param formula Übergabeparameter vom Typ String - Eingegebene Formel
     * @return Gibt eine Formel als String in KNF oder DNF zurück
     */
    public String parseFormula(String formula) throws ParserException {

        // Formel übernehmen
        this.formula = new Formel(formula);

        // Liste für den Rechenweg deklarieren
        rechenweg = new ArrayList<>();

        // Orginale Formel im Rechenweg festhalten
        rechenweg.add(new Formel(formula));

        // Userinput auf Fehler prüfen
        checkUserInput();

        // Pfeile der übergebenen Formel auflösen und überflüssige negationen Streichen
        Formel pfeileAufgeloest = pfeileAufloesen(this.formula);
        pfeileAufgeloest = negationenStreichen(pfeileAufgeloest);

        // Für den Rechenweg muss die Formel kopiert und separat gespeichert werden.
        Formel rPA = pfeileAufgeloest.copy();

        // DeMorgan auf die Formel pfeileAufgeloest anwenden
        Formel deMorgan = deMorgan(pfeileAufgeloest);

        // Für den Rechenweg muss die Fromel kopiert nd separat gespeichert werden.
        Formel rDM = deMorgan.copy();

        // Je nach Modus wird die Formel ausaddiert oder ausmultipiliziert
        switch (getModus()) {
            case KNF:
                // Ausaddierungs - Verfahren anwenden
                resultFormula = Ausaddieren.ausaddieren(deMorgan);
                break;
            case DNF:
                // Ausmultipilizierungs - Verfahren anwenden
                resultFormula = Ausmultiplizieren.ausmultiplizieren(deMorgan);
                break;
        }
        // Ergebnissformel als String aufbauen
        String resultString = "";

        // Die ErgebnissFormel wird zurück in die Listenschreibweise überführt und überflüssige
        // zeichen gestrichen, um die Formel zu verkürzen
        List<char[]> resultList = zeichenErsetzen(resultFormula);

        // Es wird geprüft, ob durch das Kürzen eine Leere Menge entstanden ist
        // und ob es sich in KNF um ein Verum oder in DNF um Falsum handelt.
        int fall = 0;
        if(resultList.size()==0){
            if(getModus()==Modi.KNF){
                resultString="Verum";
                fall = 1;
            }else if(getModus()==Modi.DNF){
                resultString="Falsum";
                fall = 2;
            }
        }

        // Es wird geprüft, ob Teilmengen / Blöcke doppelt vorkommen -> Wenn ja, werden sie gestrichen
        resultList = teilmengenErsetzten(resultList);

        /*
        //Diese Abprüfung wird noch nicht ziehen.
        //Hierfür müssen die übrigen Teilmengen noch auf Gegensätzlichkeit geprüft werden (z.B. (a)und(nicht a)).
        //Bisher wird nur geprüft, ob es gleiche Teilmengen gibt und davon bleibt nur eine stehen.
        //Die IF in Zeile 196 muss dann auch noch angepasst werden
        if(resultList.size()==0 && resultString.equals("")){
            if(getModus()==Modi.KNF){
                resultString="Falsum";
            }else if(getModus()==Modi.DNF){
                resultString="Verum";
            }
        }
        */

        // Die Liste wird zurück in das Formel - Objekt konvertiert
        resultFormula = parseListToFormel(resultList);
        // Die Rechenzeichen werden zurück gesetzt in Unicode Rechenzeichen
        resultFormula = zeichenersetzungZurueck(resultFormula);

        // Der Rechenweg wird aufgebaut
        rechenweg.add(zeichenersetzungZurueck(rPA)); // Zustand nach Pfeile Auflösen
        rechenweg.add(zeichenersetzungZurueck(rDM)); // Zustand nach DeMorgan
        if(resultFormula.getFormel().length==0){
            if(fall == 1){
                rechenweg.add(new Formel("Verum")); // Wenn es sich um Verum handelt
            }else if(fall == 2){
                rechenweg.add(new Formel("Falsum")); // Wenn es sich um Falsum handelt
            }
        }else{
            rechenweg.add(resultFormula); // Ergebnisformel
        }

        // Formel in einen String schreiben
        for (int i = 0; i < resultFormula.length(); i++) {
            resultString = resultString + resultFormula.getChar(i);
        }
        // Formel zurückgeben.
        return resultString;
    }

    /**
     * Mit der Methode parseTwoFormula werden zwei Formeln miteinander verglichen und eine Wertetabelle aufgebaut.
     * @param formulaOne
     * @param formulaTwo
     * @return
     * @throws ParserException
     */
    public int[][] parseTwoFormula(String formulaOne, String formulaTwo) throws ParserException {

        // Da mit Fehlercodes gearbeitet wird, wird die Variable fehlercode gesetzt.
        int fehlercode;

        /*
        * Formel 1
        */
        this.formula = new Formel(formulaOne);
        // Prüfung der ersten Formel und auflösung der Pfeile, anwenden von DeMorgen und überführung in KNF
        checkUserInput();
        Formel f1 = pfeileAufloesen(this.formula).copy();
        f1 = negationenStreichen(f1);
        Formel f1DeMorgan = deMorgan(f1);
        Formel f1KNF = Ausaddieren.ausaddieren(f1DeMorgan);
        f1KNF = negationenStreichen(f1KNF);

        /*
        Formel 2
         */
        this.formula = new Formel(formulaTwo);
        // Prüfung der ersten Formel und auflösung der Pfeile, anwenden von DeMorgen und überführung in KNF
        checkUserInput();
        Formel f2 = pfeileAufloesen(this.formula).copy();
        f2 = negationenStreichen(f2);
        Formel f2DeMorgan = deMorgan(f2);
        Formel f2KNF = Ausaddieren.ausaddieren(f2DeMorgan);
        f2KNF = negationenStreichen(f2KNF);

        // Erstellung eines Objektes der Klasse ZweiFormeln
        ZweiFormeln zweiFormelnParser = new ZweiFormeln();
        //Die zwei Formeln haben unterschiedliche Variablen
        if (zweiFormelnParser.compareVariables(f1KNF.getFormel(), f2KNF.getFormel()) == false) {
            // Zwei Formeln haben unterschiedliche Variablen. So wird ein Fehler mit dem Fehlercode -30 geworfen
            fehlercode = -30;
            throw new ParserException(fehlercode);
        }
        // Es wird geprüft, ob die eingegebenen zwei Formeln übereinstimmen
        else if (zweiFormelnParser.compareFormulas(f1KNF.getFormel(), f2KNF.getFormel(), zweiFormelnParser.checkVariables(f1KNF.getFormel()))) {
            //Die zwei Formeln stimmen überein
            return zweiFormelnParser.getTruthTable();
        }
        // Die Formeln stimmen nicht überein
        else {
            // Es wird ein Fehler erzeugt mit dem Fehlercode -20, um zu signalisieren, dass die Formeln nicht überienstimmen
            fehlercode = -20;
            ParserException pe = new ParserException(fehlercode);
            pe.setTruthTable(zweiFormelnParser.getTruthTable());
            pe.setVariables(zweiFormelnParser.checkVariables(f1KNF.getFormel()));
            throw pe;
        }
    }

    /**
     * Methode getVariables ist eine Hilfsmethode für den Bereich 2 Formeln, um
     * eine ArrayListe mit Characten zu erstellen, welche die Variablen in einer Formel wiedergeben
     * @param formulaOne
     * @return
     */
    public ArrayList<Character> getVariables(String formulaOne) {
        ZweiFormeln zweiFormelnParser = new ZweiFormeln();
        this.formula = new Formel(formulaOne);

        return zweiFormelnParser.checkVariables(formula.getFormel());
    }


    /**
     * Die Methode buildTruthTable wird dazu verwendet, um die Wertetabelle zu einer Formel aufzubauen.
     * @param formula
     * @return
     * @throws ParserException
     */
    public int[][] buildTruthTable(String formula) throws ParserException {

        // Formel übernehmen
        this.formula = new Formel(formula);

        // Es wird die Formel auf Eingabefehler geprüft
        checkUserInput();

        // Es werden die Pfeile der Formel aufgelöst, daraufhin deMorgen angewendet und anschließend die KNF gebildet.
        Formel pfeileAufgeloest = pfeileAufloesen(this.formula);
        Formel deMorgan = deMorgan(pfeileAufgeloest);
        Formel knf = Ausaddieren.ausaddieren(deMorgan);

        // Es wird ein Objekt der Wertetabelle erstellt.
        Wertetabelle wertetabelle = new Wertetabelle();

        // Wertetabelle erstellen und zurück geben.
        int[][] truthTable = wertetabelle.createFinishedTruthTable(knf.getFormel(), wertetabelle.checkVariables(knf.getFormel()));
        return truthTable;
    }

    /**
     * <b>Unused!</b>
     * isTautologie kann in einer späteren Entwicklungsphase als Einstiegsunkt für die Resolution dienen.
     * @param formula
     * @return
     */
    public boolean isTautologie(String formula) {
        return false;
    }

    /**
     * Die Methode checkUserInput prüft die Formel im Klassenattribut formula auf Eingabefehler.
     * @return
     * @throws ParserException
     */
    private int checkUserInput() throws ParserException {
        // Zähler für Klamern
        int countOpen = 0;
        int countClose = 0;

        // Falls keine Formel eingegeben sein sollte und sich der Parser im Modus Wertetabelle oder Modus befindet,
        // soll ein Fehler mit -13 ausgelöst werden.
        if (this.formula.length()==0 && (getModus()==Modi.WERTETABELLE || getModus()==Modi.FORMELN)) {
            // Fehlermeldung
            throw new ParserException(-13);
        }

        // Es wird die gesamte Formel durchgegangen
        for (int i = 0; i < this.formula.length(); i++) {
            char c = this.formula.getChar(i);

            // Klammern zählen
            if (c=='(') {
                countOpen++;
            } else if (c==')') {
                countClose++;
            }

            /*
            \\u00AC = Negation
            \\u2192 = Pfeil
            \\u2194 = Pfeil beidseitig
            \\u22C1 = Oder
            \\u2227 = Und
             */
            // Es wird geprüft, ob i auf dem letzten Char steht
            if ((i + 1) < this.formula.length()) {
                // Handelt es sich um einen Buchstaben und folgt eine Negation direkt auf den Buchstaben -> Fehler mit -2
                if (Character.toString(c).matches("[a-n]")) {
                    if (Character.toString(this.formula.getChar(i + 1)).matches("[a-e(\\u00AC]")) {
                        // Fehler: Nach Buchstabe muss ein Operator kommen
                        throw new ParserException(-2);
                    }
                }
                // Handlet es sich um eine öffnende Klammer
                else if (c == '(') { // Folgen Operatoren auf die öffnende Klammer -> Fehler mit -3
                    if (Character.toString(this.formula.getChar(i + 1)).matches("[\\u22C1\\u2227\\u2194\\u2192]")) {
                        throw new ParserException(-3);
                    } // Steht die Klammer an der letzten Stelle -> Fehler -8
                    else if (i + 1 == formula.length()) {
                        throw new ParserException(-8);
                    }
                }
                // Handelt es sich um eine schließende Klammer
                else if (c == ')') { // Folgt ein Buchstabe, Negation oder öffnende Klammer -> Fehler -4
                    if (Character.toString(this.formula.getChar(i + 1)).matches("[a-e\\u00AC(]")) {
                        throw new ParserException(-4);
                    } // Steht die schließende Klammer an erster Stelle -> Fehler -9
                    else if (i == 0) {
                        throw new ParserException(-9);
                    }
                }
                // Handelt es sich um eine Negation
                else if (Character.toString(c).matches("[\\u00AC]")) { // Folgt ein Operator oder eine schließende Klammer -> Fehler -5
                    if (Character.toString(this.formula.getChar(i + 1)).matches("[\\u22C1\\u2227\\u2192\\u2194)]")) {
                        throw new ParserException(-5);
                    }
                }
                // Handelt es sich um ein Operator
                else if (Character.toString(c).matches("[\\u22C1\\u2227\\u2192\\u2194]")) {
                    // Folgt ein Operator oder eine schießende Klammer -> Fehler -6
                    if (Character.toString(this.formula.getChar(i + 1)).matches("[\\u2192\\u2194\\u22C1\\u2227)]")) {
                        throw new ParserException(-6);
                    } // Steht der Operator an erster Stelle -> Fehler -7
                    else if (i == 0) {
                        throw new ParserException(-7);
                    }
                }
            }
            // Steht i auf der letzten Stelle
            else if (i + 1 == formula.length()) {
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
        // liegen unterschiedlich viele öffnende und schließende Klammern vor -> Fehler -1
        if (countOpen != countClose) {
            // Fehlermeldung
            throw new ParserException(-1);
        }

        /*
        Zeichenersetzung durchführen
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

        // erste negationen streichen
        this.formula = negationenStreichen(this.formula);

        // Erfolgsfall ist 42
        return 42;
    }


    /**
     * Mit der Mehtode zeichenersetzungZurück sollen alle Zeichen wieder in Unicode Zeichen zurück gesetzt werden
     * @param formel
     * @return
     */
    public Formel zeichenersetzungZurueck(Formel formel) {
        /*
        Zeichenersetzung
        oder wird +
        und wird *
        -> wird 1
        <-> wird 2
        negation wird n
         */

        //
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


    /**
     * Diese Methode führt den ersten Schritt für die Bildung einer KNF oder DNF aus und zwar
     * das Pfeile auflösen.
     * Die Zahl 1 steht für eine einseitige Implikation
     * Die Zahl 2 steht für eine beidseitige Implikation
     * @param formel übergebene Formel
     * @return bearbeitete Formel mit aufgelösten Pfeilen
     */
    public Formel pfeileAufloesen(Formel formel) {
        //Formel, welche zurückgegeben wird
        Formel bFormel = formel;

        //Es wird jedes Zeichen der übergebenen Formel überprüft, ob es eine 1 oder 2 ist
        for (int i = 0; i < bFormel.length(); i++) {

            // Block vorne
            char c = bFormel.getChar(i);
            /* 1 = einseitige Implikation
             * 2 = beidseitige Implikation
             */
            if (c == '1' || c == '2') {

                //Mit dieser While-Schleife wird der linke Teil des gefundenen Pfeils ermittelt
                //und in "vordererBlock" geschrieben
                String vordererBlock = "";
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
                        }
                        vordererBlock = vordererBlock + formelChar;
                        counterLinks++;
                    } else {
                        break;
                    }
                }
                //Da wir den vorderen Teil falschherum eingelesen haben, müssen wir ihn nun umdrehen
                vordererBlock = new StringBuilder(vordererBlock).reverse().toString();

                //Mit dieser While-Schleife wird der rechte Teil des gefundenen Pfeils ermittelt
                //und in "hintererBlock" geschrieben
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
                        }
                        hintererBlock = hintererBlock + formelChar;
                        counterRechts++;
                    } else {
                        break;
                    }
                }

                //Die Unterscheidung ob wir eine einseitige oder beidseitige Implikation haben, da
                //die beiden ermittelten Blöcke dementsprechen anders verarbeitet werden
                if (c == '1') {
                    bFormel.blockEinsetzen(einseitigeImplikation(new Formel(vordererBlock).getFormel(), new Formel(hintererBlock).getFormel()), i - vordererBlock.length(), i + hintererBlock.length());
                    return pfeileAufloesen(bFormel);
                } else if (c == '2') {
                    bFormel.blockEinsetzen(beidseitigeImplikation(new Formel(vordererBlock).getFormel(), new Formel(hintererBlock).getFormel()), i - vordererBlock.length(), i + hintererBlock.length());
                    return pfeileAufloesen(bFormel);
                }
            }
        }
        //Hier werden unnötige Klammern gelöscht
        bFormel.klammernPrüfen();

        return bFormel;
    }

    /**
     * Diese Methode führt den zweiten Schritt für die Bildung einer KNF oder DNF aus und zwar DeMorgan.
     * Diese Mehtode ruft sich rekursiv auf, falls ein Fall für DeMorgan gefunden wurde.
     * @param formel übergebene Formel, wo die Pfeile schon aufgelöst wurden
     * @return bearbeitete Formel mit DeMorgan
     */
    public Formel deMorgan(Formel formel) {

        //Durch "bformel" wird iteriert und "fDeMorgan" wird befüllt und schlussendlich ausgegeben
        Formel bFormel = formel;
        Formel fDeMorgan = new Formel();

        //Falls in der FOR-Schleife eine Fall für DeMorgan gefunden wird, ruft die Methode sich
        //nochmal rekursiv auf
        boolean deMorganGefunden = false;

        for (int i = 0; i < bFormel.length(); i++) {
            char c = bFormel.getChar(i);

            // Falls wir einen Fall für DeMorgan finden, müssen wir nun die betroffenen Operatoren
            // umdrehen und Buchstaben negieren
            if (c == 'n' && bFormel.getChar(i + 1) == '(') {
                deMorganGefunden = true;

                //Wenn die Klammern ungleich 0 sind, bedeutet dies, dass es Zeichen gibt, welche
                //von diesem DeMorgan-Fall nicht betroffen sind und einfach normal übernommen werden
                //sollen
                int klammern = 0;
                fDeMorgan.zeichenHinzufügen('(');
                i = i + 2;
                while (i < formel.length()) {
                    //Klammern kleiner Null --> weitere Zeichen sollen ohne Abänderung übernommen werden
                    if(klammern<0){
                        fDeMorgan.zeichenHinzufügen(bFormel.getChar(i));
                        i++;
                        continue;
                    }
                    //Klammern ungleich 0 --> weitere Zeichen sollen ohne Abänderungen übernommen werden,
                    //aber die Klammer kann wieder zu gehen und die Zeichen danach sind wieder betroffen
                    if (klammern != 0) {
                        fDeMorgan.zeichenHinzufügen(bFormel.getChar(i));
                        if(bFormel.getChar(i) == '('){
                            klammern++;
                        }else if(bFormel.getChar(i) == ')'){
                            klammern--;
                        }
                        //negativer Buchstabe wird zu positivem Buchstabe
                    } else if (Character.toString(bFormel.getChar(i)).matches("[a-e]") && bFormel.getChar(i - 1) == 'n') {
                        fDeMorgan.zeichenHinzufügen(bFormel.getChar(i));
                        //positiver Buchstabe wird zu negativem Buchstabe
                    } else if (Character.toString(bFormel.getChar(i)).matches("[a-e]") && bFormel.getChar(i - 1) != 'n') {
                        fDeMorgan.zeichenHinzufügen('n');
                        fDeMorgan.zeichenHinzufügen(bFormel.getChar(i));
                        //+ wird zu *
                    } else if (bFormel.getChar(i) == '+') {
                        fDeMorgan.zeichenHinzufügen('*');
                        //* wird zu +
                    } else if (bFormel.getChar(i) == '*') {
                        fDeMorgan.zeichenHinzufügen('+');
                        //positive Klammer wird zu negativer Klammer
                    } else if (bFormel.getChar(i) == '(' && bFormel.getChar(i - 1) != 'n') {
                        fDeMorgan.zeichenHinzufügen('n');
                        fDeMorgan.zeichenHinzufügen('(');
                        klammern++;
                        //negative Klammer wird zu positiver Klammer
                    } else if (bFormel.getChar(i) == 'n' && bFormel.getChar(i + 1) == '(') {
                        fDeMorgan.zeichenHinzufügen('(');
                        i++;
                        klammern++;
                        //Geschlossene Klammer hinzufügen und runterzählen
                    } else if (bFormel.getChar(i) == ')') {
                        fDeMorgan.zeichenHinzufügen(')');
                        klammern--;
                    }
                    i++;
                }


            } else {
                fDeMorgan.zeichenHinzufügen(bFormel.getChar(i));
                continue;
            }
            if (deMorganGefunden) {
                //Die Methode ruft sich rekurisiv auf, da durch das Durchführen von DeMorgan neue
                //DeMorgan-Fälle enstehen konnten.
                return deMorgan(fDeMorgan);
            }
        }

        //Hier werden unnötige Klammern gelöscht
        fDeMorgan.klammernPrüfen();
        return fDeMorgan;
    }


    /*
    --------------------------------------------------------------
    Helfer - Methoden
    --------------------------------------------------------------
     */

    /**
     * In dieser Methode wird die einseitige Implikation durchgeführt.
     * @param b1 Teil vor der beidseitigen Implikation
     * @param b2 Teil nach der beidseitigen Implikation
     * @return neue abgeänderte Formel
     */
    private Formel einseitigeImplikation(char[] b1, char[] b2) {

        Formel result = new Formel();

        result.zeichenHinzufügen('n');
        result.zeichenHinzufügen('(');

        for (int i = 0; i < b1.length; i++) {
            result.zeichenHinzufügen(b1[i]);
        }

        result.zeichenHinzufügen(')');
        result.zeichenHinzufügen('+');

        for (int i = 0; i < b2.length; i++) {
            result.zeichenHinzufügen(b2[i]);
        }

        return result;
    }

    /**
     * In dieser Methode wird die beidseitige Implikation durchgeführt, in dem zwei Mal die einseitige
     * Implikation aufgerufen wird und die beiden Teile dann zusammengesetzt werden.
     * @param b1 Teil vor der beidseitigen Implikation
     * @param b2 Teil nach der beidseitigen Implikation
     * @return neue abgeänderte Formel
     */
    private Formel beidseitigeImplikation(char[] b1, char[] b2) {

        Formel r1 = einseitigeImplikation(b1, b2);
        Formel r2 = einseitigeImplikation(b2, b1);

        Formel result = new Formel();

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
     * @return neue Formel in der Form von List<char[]> mit ersetzten Zeichen
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

    /**
     * Zweite Methode für das Zeichen ersetzten, wo die Logik drin ist zum ersetzten der Zeichen.
     * Die zweite Methode wird benötigt, da wir manchmal Formeln und manchmal List<char[]> übergeben.
     * @param formel
     * @return neue Formel in der Form von List<char[]> mit ersetzten Zeichen
     */
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

    /**
     * In dieser Methode ist die übergebene Liste schon in KNF oder DNF und die Zeichen wurden schon
     * gekürzt/ersetzt. Hier wird nun geprüft, ob es doppelte Teilmengen gibt und wenn ja, wir nur eine
     * davon benötigt.
     * @param list
     * @return eine List<char[]> wo es keine doppelten Teilmengen mehr gibt
     */
    public List<char[]> teilmengenErsetzten(List<char[]> list){

        List<char[]> provedFormula = new ArrayList<>();

        //Die Formel wird von hinten nach vorne durchlaufen, damit die vordere Teilmenge stehen bleibt
        //und die hintere nicht in provedFormula geschrieben wird
        for(int i = list.size()-1; i>0; i--){
            boolean match = false;
            for(int j = 0; j<list.size(); j++){
                //Man darf sich nicht mit sich selbst vergleichen
                if(i!=j){
                    //Wenn die Teilmengen gleich sein sollen, müssen sie auch die selbe Größe haben
                    if(list.get(i).length == list.get(j).length){
                        //Nun wird jedes einzelne Zeichen miteinander verglichen und wenn es eine
                        //Unstimmigkeit gibt, stimmen die Teilmengen nicht überein
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
        if(list.size()>0){
            provedFormula.add(list.get(0));
        }
        //Da wir von hinten nach vorne durch die List esind, muss diese nun nochmal umgedreht werden
        Collections.reverse(provedFormula);

        return provedFormula;
    }

    /**
     * Diese Methode macht aus einer List<char[]> eine Formel.
     * @param list übergebene Formel in der Form einer List<char[]>
     * @return Formel
     */
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

    /**
     * Diese Methode macht aus einer Formel eine List<char[]>.
     * @param formel
     * @return übergebene Formel in der Form einer List<char[]>
     */
    public List<char[]> parseFormelToList(Formel formel){

        List<char[]> gesamtmenge = new ArrayList<>();
        // neue Teilmenge
        Formel teilmenge = new Formel();

        // Es wird die Formel ausgelesen und die Teilmengen gebildet und die Teilmengen in die
        // Gesamtmenge hinzugefügt
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

    public void setModus(Modi modus) {
        this.modus = modus;
    }

    public Modi getModus() {
        return modus;
    }
}
