package com.dhbw.app_zur_aussagenlogik.core;

/**
 * Die Klasse <b>Formel</b> ist die zentrale Klasse, mit welcher neue Instanzen einer Formel
 * erstellt werden können.
 *
 * @author Nico Erzberger, Daniel Miller
 * @version 1.0
 */
public class Formel {

    private char[] formel = new char[0];

    //Konstruktor
    public Formel(String formel){
        this.formel = new char[formel.length()];
        for(int i = 0; i<formel.length(); i++){
            this.formel[i] = formel.charAt(i);
        }
    }

    //Konstruktor
    public Formel(char[] formel){
        this.formel = formel;
    }

    //Konstruktor
    public Formel(){ }

    //Konstruktor
    public Formel(int length){
        this.formel = new char[length];
    }

    public char getChar(int index){
        return this.formel[index];
    }

    public void setChar(int index, char c){
        this.formel[index] = c;
    }

    public char[] getFormel(){
        return this.formel;
    }

    public String toString(){
        String s = "";
        for(int i = 0; i < formel.length; i++) {
            s = s + formel[i];
        }
        return s;
    }

    public int length(){
        return this.formel.length;
    }

    public Formel copy(){
        return new Formel(this.formel);
    }


    /**
     * In dieser Methode werden unnötige Klammern aus der Formel genommen
     * Hier wird die Methode klammernNotwendig() aufgerufen, um zu wissen, ob die Klammern notwendig
     * sind oder nicht.
     * @return
     */
    public Formel klammernPrüfen(){
        //Hier wird durch die Formel iteriert
        for (int j = 0; j<formel.length; j++) {
            if (getChar(j) == '(') {
                //Wenn die Klammer notwendig ist, wird zum nächsten Zeichen gesprungen
                if (klammerNotwendig(j)) {
                    continue;
                } else {
                    Formel teilInKlammer = new Formel();
                    int klammern = 1;
                    int count = j + 1;
                    while (klammern > 0) {
                        if (getChar(count) == ')') {
                            klammern--;
                            if (klammern == 0) {
                                break;
                            }
                        } else if (getChar(count) == '(') {
                            klammern++;
                        }
                        teilInKlammer.zeichenHinzufügen(getChar(count));
                        count++;
                    }
                    this.blockEinsetzen(teilInKlammer, j, count);
                }
            }
        }
        return this;
    }

    /**
     * In dieser Methode wird überprüft, ob die gefundene Klammer in der Formel notwendig ist oder
     * weg kann.
     * @param indexÖffnedeKlammer der Index der gefunden öffnenden Klammer
     * @return boolean wahr oder falsch ob die Klammer notwendig ist oder nicht
     */
    public boolean klammerNotwendig(int indexÖffnedeKlammer){
        if(indexÖffnedeKlammer>=0 && indexÖffnedeKlammer+1 <= formel.length) {
            boolean endklammerNichtGefunden = true;
            int indexEndklammer = indexÖffnedeKlammer + 1;
            int anzahlKlammern = 1;
            boolean malInKlammer = false;
            boolean plusInKlammer = false;
            while (endklammerNichtGefunden) {
                if (getChar(indexEndklammer) == '(') {
                    anzahlKlammern++;
                } else if (getChar(indexEndklammer) == ')') {
                    anzahlKlammern--;
                } else if (getChar(indexEndklammer) == '*' && anzahlKlammern == 1) {
                    malInKlammer = true;
                } else if (getChar(indexEndklammer) == '+' && anzahlKlammern == 1) {
                    plusInKlammer = true;
                }
                if (anzahlKlammern == 0) {
                    endklammerNichtGefunden = false;
                    break;
                }
                indexEndklammer++;
            }

            if((indexEndklammer == indexÖffnedeKlammer+2 && getChar(indexÖffnedeKlammer+1)!='n')
                    || (indexEndklammer == indexÖffnedeKlammer+3 && getChar(indexÖffnedeKlammer+1)=='n')){
                  return false;
            }else if(indexÖffnedeKlammer==0 && (indexEndklammer)==formel.length){
                  return false;
            }else if(indexÖffnedeKlammer>0 && (indexEndklammer+1)==formel.length){
                if ((getChar(indexÖffnedeKlammer - 1) == '*' && plusInKlammer )
                        ||(getChar(indexÖffnedeKlammer - 1) == '+' && malInKlammer)
                        || getChar(indexÖffnedeKlammer - 1) == '1'
                        || getChar(indexÖffnedeKlammer - 1) == '2'
                        || getChar(indexÖffnedeKlammer - 1) == 'n') {
                    return true;
                }
            }else if(indexÖffnedeKlammer>0 && (indexEndklammer+1)<=formel.length) {
                if (((getChar(indexÖffnedeKlammer - 1) == '*' && plusInKlammer )
                        ||(getChar(indexÖffnedeKlammer - 1) == '+' && malInKlammer)
                        || getChar(indexÖffnedeKlammer - 1) == '1'
                        || getChar(indexÖffnedeKlammer - 1) == '2'
                        || getChar(indexÖffnedeKlammer - 1) == 'n') ||
                        ((getChar(indexEndklammer + 1) == '*' && plusInKlammer)
                                || (getChar(indexEndklammer + 1) == '+' && malInKlammer)
                                || getChar(indexEndklammer + 1) == '1'
                                || getChar(indexEndklammer + 1) == '2'
                                || getChar(indexEndklammer + 1) == 'n')) {
                    return true;
                }
            }else if(indexÖffnedeKlammer==0 && (indexEndklammer+1)<formel.length){
                if ((getChar(indexEndklammer + 1) == '*' && plusInKlammer)
                        || (getChar(indexEndklammer + 1) == '+' && malInKlammer)
                        || getChar(indexEndklammer + 1) == '1'
                        || getChar(indexEndklammer + 1) == '2'
                        || getChar(indexEndklammer + 1) == 'n') {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * In dieser Methode wird die neue Formel aufgebaut, indem der neue Formelteil in die bestehende
     * Formel überführt wird.
     * @param newArray übergebener Formelteil
     * @param anfang an welcher Stelle soll der Formelteil ausgefüllt werden
     * @param ende bis zu welcher Stelle soll der Formelteil ausgefüllt werden
     */
    public void blockEinsetzen(Formel newArray, int anfang, int ende) {
        Formel neueFormel = new Formel();
        //Anfang originale Formel
        for (int i = 0; i < anfang; i++) {
            neueFormel.zeichenHinzufügen(getChar(i));
        }
        //Neuer Formelteil
        for (int i = 0; i < newArray.length(); i++) {
            neueFormel.zeichenHinzufügen(newArray.getChar(i));
        }
        //Ende der originalen Formel
        for (int i = ende+1; i < this.formel.length; i++) {
            neueFormel.zeichenHinzufügen(getChar(i));
        }
        this.formel = neueFormel.formel;
    }

    /**
     * Mit dieser Methode können Zeichen einem char[] hinzugefügt werden.
     * @param z das Zeichen, welches hinzugefügt werden soll.
     * @return neues char[]
     */
    public void zeichenHinzufügen(char z) {
        char[] newZeichenSatz = new char[this.formel.length+1];
        for (int i = 0; i < this.formel.length; i++) {
            newZeichenSatz[i] = this.formel[i];
        }
        newZeichenSatz[this.formel.length] = z;
        this.formel = newZeichenSatz;
    }

}
