package com.dhbw.app_zur_aussagenlogik.core;

public class Formel {

    private Zeichen[] formel = new Zeichen[0];

    public Formel(String formel){
        this.formel = new Zeichen[formel.length()];
        for(int i = 0; i<formel.length(); i++){
            this.formel[i] = new Zeichen(formel.charAt(i));
        }
    }

    public Formel(char[] formel){
        this.formel = new Zeichen[formel.length];
        for (int i = 0; i < formel.length; i++){
            this.formel[i] = new Zeichen(formel[i]);
        }
    }

    public Formel(Zeichen[] formel){
        this.formel = new Zeichen[formel.length];
        this.formel = formel;
    }

    public Formel(){

    }

    public Formel(int length){
        this.formel = new Zeichen[length];
    }


    public char getChar(int index){
        return this.formel[index].getZeichen();
    }

    public void setChar(int index, char c){
        this.formel[index].setZeichen(c);
    }

    public Zeichen[] getFormel(){
        return this.formel;
    }

    public int length(){
        return this.formel.length;
    }

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
            int indexEndklammer = indexÖffnedeKlammer+1;
            int anzahlKlammern = 1;
            boolean malInKlammer = false;
            boolean plusInKlammer = false;
            while (endklammerNichtGefunden) {
                if (getChar(indexEndklammer) == '(') {
                    anzahlKlammern++;
                } else if (getChar(indexEndklammer) == ')') {
                    anzahlKlammern--;
                }else if(getChar(indexEndklammer)=='*' && anzahlKlammern==1){
                    malInKlammer = true;
                } else if(getChar(indexEndklammer)=='+'&& anzahlKlammern==1){
                    plusInKlammer = true;
                }
                if (anzahlKlammern == 0) {
                    endklammerNichtGefunden = false;
                    break;
                }
                indexEndklammer++;
            }
            if(indexÖffnedeKlammer>0 && (indexEndklammer+1)==formel.length){
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
            }else if(indexÖffnedeKlammer==0){
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
        Zeichen[] newZeichenSatz = new Zeichen[this.formel.length+1];
        for (int i = 0; i < this.formel.length; i++) {
            newZeichenSatz[i] = this.formel[i];
        }
        newZeichenSatz[this.formel.length] = new Zeichen(z);
    }

    class Zeichen{
        char zeichen;
        boolean negiert;

        Zeichen(char zeichen){
            this.zeichen = zeichen;
            this.negiert = false;
        }

        Zeichen(char zeichen, boolean negiert){
            this.zeichen = zeichen;
            this.negiert = negiert;
        }

        public char getZeichen() {
            return zeichen;
        }

        public boolean isNegiert() {
            return negiert;
        }

        public void setZeichen(char zeichen) {
            this.zeichen = zeichen;
        }

        public void setNegiert(boolean negiert) {
            this.negiert = negiert;
        }
    }

}
