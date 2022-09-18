package com.dhbw.app_zur_aussagenlogik.core;

public class Formel {

    private char[] formel = new char[0];

    public Formel(String formel){
        this.formel = new char[formel.length()];
        for(int i = 0; i<formel.length(); i++){
            this.formel[i] = formel.charAt(i);
        }
    }

    public Formel(char[] formel){
        this.formel = formel;
    }

    public Formel(){

    }

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

    /*
    public Formel negationPruefen(){

        Formel newFormel = new Formel();

        for(int i = 0; i<formel.length; i++){
            if(i<formel.length-1){
                if(getChar(i)=='n' && getChar(i+1) == 'n'){
                    i = i + 2;
                }
            }

            newFormel.zeichenHinzufügen(getChar(i));
        }
        return newFormel;
    }
    */

    public Formel klammernPrüfen(){
        for (int j = 0; j<formel.length; j++) {
            if (getChar(j) == '(') {
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

    public void blockEinsetzen(Formel newArray, int anfang, int ende) {
        Formel neueFormel = new Formel();
        for (int i = 0; i < anfang; i++) {
            neueFormel.zeichenHinzufügen(getChar(i));
        }
        for (int i = 0; i < newArray.length(); i++) {
            neueFormel.zeichenHinzufügen(newArray.getChar(i));
        }
        for (int i = ende+1; i < this.formel.length; i++) {
            neueFormel.zeichenHinzufügen(getChar(i));
        }
        this.formel = neueFormel.formel;
    }

    public void zeichenHinzufügen(char z) {
        char[] newZeichenSatz = new char[this.formel.length+1];
        for (int i = 0; i < this.formel.length; i++) {
            newZeichenSatz[i] = this.formel[i];
        }
        newZeichenSatz[this.formel.length] = z;
        this.formel = newZeichenSatz;
    }

}
