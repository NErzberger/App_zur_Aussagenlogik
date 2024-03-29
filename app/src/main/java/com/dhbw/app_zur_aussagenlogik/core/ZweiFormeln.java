package com.dhbw.app_zur_aussagenlogik.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Die Klasse <b>ZweiFormeln</b> ist für das Erstellen einer Wertetabelle mit zwei Ergebnisspalten.
 * Hier werden zwei Formeln miteinander verglichen. Wichtig ist, dass die KNF (Klasse
 * "Ausaddieren") für diese Klasse die Grundlage bildet.
 *
 * Diese Klasse ist genauso aufgebaut wie die Klasse "Wertetabelle". Nur das es hier zwei
 * Ergebnisspalten gibt und die Methode compareVariables().
 *
 * @author Daniel Miller
 * @version 1.0
 */
public class ZweiFormeln {

    private int[][] truthTable;


    /**
     * Hier wird die Struktur der Wertetabellen mit Nullen und Einsen gefüllt. Die Ergebnisspalten
     * werden auch schon hinzugefügt, damit sie später befüllt werden können.
     * @param n Anzahl der verwendeten Variablen
     * @return fertige Wertetabelle mit leeren Spalten für das Ergebnis
     */
    private int[][] createTruthTable(int n) {

        int rows = (int) Math.pow(2,n);
        //n+2 weil wir 2 weitere Ergebnisspalten für 2 Formeln brauchen
        int[][] truthTable = new int[n+2][rows];

        //Befüllen der Wertetabelle mit Nullen und Einsen für die Grundstruktur
        for (int i=0; i<rows; i++) {
            for (int j=0; j<n; j++) {
                truthTable[j][i]= (i/(int) Math.pow(2, j))%2;
            }
        }

        return truthTable;

    }

    /**
     * In dieser Methode wird überprüft, ob die beiden Formeln die selben Variablen verwenden.
     * @param formel1 erste Formel
     * @param formel2 zweite Formel
     * @return boolean wahr oder falsch, ob die Formeln die selben Variablen verwenden
     */
    public Boolean compareVariables(char[] formel1, char[] formel2){

        ArrayList<Character> variablesFormula1 = checkVariables(formel1);
        ArrayList<Character> variablesFormula2 = checkVariables(formel2);

        if(variablesFormula1.size() != variablesFormula2.size()){
            return false;
        }else{
            for(int i = 0;i<variablesFormula2.size(); i++){
                if(variablesFormula1.get(i)!=variablesFormula2.get(i)){
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * In dieser Methode wird überprüft, welche Variablen in der Formel verwendet wurden, um später
     * die Wertetabelle genau aufbauen zu können und zu überprüfen, ob die zwei Formeln, welche
     * verglichen werden von den Variablen übereinstimmen.
     * @param formel übergebene Formel
     * @return Liste mit den verwendeten Variablen
     */
    public ArrayList<Character> checkVariables(char[] formel) {

        //Wenn eine der Variablen gefunden wurde, wird die Variable auf 1 gesetzt, damit jede
        //Variable nur einmal in die Liste "variables" eingefügt wird
        int foundA = 0;
        int foundB = 0;
        int foundC = 0;
        int foundD = 0;
        int foundE = 0;

        //Die Ergebnisliste, welche gegen Ende zurückgegeben wird.
        ArrayList<Character> variables = new ArrayList<Character>();

        //Hier wird durch die Formel iteriert und für jede neue Variable, welche gefunden wird,
        //wird die zugehörige Methodenvariable auf 1 gesetzt und die Variable an sich wird der
        //Liste "variables" hinzugefügt
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

        //Die Variablen werden noch sortiert, um später eine sortierte Wertetabelle erstellen zu können
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
    public boolean compareFormulas(char[] formel1, char[] formel2, ArrayList<Character> variables) {

        boolean result = true;

        //Aus der Anzahl der Variablen, ergibt sich die Anzahl der Reihen der Wertetabelle 2^n.
        //Die Anzahl der Reihen ist die maximale Anzahl an Durchläufen, um die beiden Formeln zu vergleichen.
        int rows = (int) Math.pow(2,variables.size());

        //Hier wird die Grundstruktur einer Wertetabelle erstellt mit leeren Ergebnisspalten.
        //Die Ergebnisspalten werden im folgenden Verlauf befüllt.
        truthTable = createTruthTable(variables.size());


        //In der List row soll die Werte der aktuellen Zeile der Wertetabelle zwischengespeichert werden, damit
        //die beiden Formeln anhand dieser Zeile verglichen werden können
        int[] row = new int[variables.size()];

        /*
         * Die Schleife wird maximal so häufig ausgeführt, wie es Zeilen in der Wertetabelle gibt.
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

    /**
     * In dieser Methode wird ermittelt, ob die Formel für eine bestimmte Zeile in der Wertetabelle
     * 0 oder 1 ist. Das die Formel in der Form der KNF übergeben wird ist hier die Grundlage.
     * @param formel
     * @return
     */
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

        //Falls die übergebene Formel keine UND Verknüpfungen hat
        if(counterUnd == 0){
            if(formel.contains('1') == false){
                integerResult = 0;
                return integerResult;
            }else{
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

            if(formel[i] == 'n' && formel[i+1] == '1') {

                formel[i+1] = '0';

            } else if(formel[i] == 'n' && formel[i+1] == '0') {

                formel[i+1] = '1';

            }

        }

        //Liste ohne Negationen mehr
        ArrayList<Character> changedFormel = new ArrayList<Character>();

        //Hier werden alle Zeichen, ausser Negationszeichen, in die neue Liste kopiert
        for(char c : formel) {
            if(c != 'n') {
                changedFormel.add(c);
            }
        }

        return changedFormel;
    }



    public int[][] getTruthTable() {
        return truthTable;
    }

    public void setTruthTable(int[][] truthTable) {
        this.truthTable = truthTable;
    }
}
