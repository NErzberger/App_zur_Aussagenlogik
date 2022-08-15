package com.dhbw.app_zur_aussagenlogik.parser;

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

    private String formula;
    private char[] formulaArray;

    private char[] resultArray;

    // Methode zum setzen

    public void setParserParameter(Modi modus, String formula) throws ParserException {
        setModus(modus);
        parseFormula(formula);
    }

    public String parseFormula(String formula) throws ParserException{
        String unparsedFormula = formula;

        // Formel parsen
        this.formulaArray = new char[formula.length()];
        for(int i = 0; i<formula.length(); i++){
            this.formulaArray[i] = formula.charAt(i);
        }

        /*
        Fehlercodes
        -1 = Ungerade Anzahl der Klammern
        42 = alles in Ordnung
         */
        int fehlercode = 0;
        String parsedFormula = "";
        try {
            fehlercode = checkUserInput();
            parsedFormula = unparsedFormula;
        }catch (Exception e){
            throw new ParserException(fehlercode);
        }
        setFormula(parsedFormula);

        //char[] knfNormalform = ausaddieren(deMorgan(pfeileAufloesen(this.formulaArray)));
        char[] pfeileAufgeloest = pfeileAufloesen(this.formulaArray);
        char[] deMorgan = deMorgan(pfeileAufgeloest);

        switch (getModus()){
            case KNF:
                resultArray = ausaddieren(deMorgan);
                break;
            case DNF:
                resultArray = ausmultiplizieren(deMorgan);
                break;
            case RESOLUTION:
                break;
            case FORMELN:
                break;
        }
        zeichenersetzungZurueck();
        String resultString = "";
        for(int i = 0; i < resultArray.length; i++){
            resultString = resultString + resultArray[i];
        }
        return resultString;
    }

    private int checkUserInput(){
        // Zähler für Klamern
        int countOpen = 0;
        int countClose = 0;

        for(int i = 0; i < this.formulaArray.length; i++){
            char c = this.formulaArray[i];

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
            if((i+1)<this.formulaArray.length) {
                if(Character.toString(c).matches("[a-n]")){
                    if (Character.toString(this.formulaArray[i + 1]).matches("[a-e(\\u00AC]")){
                        // Fehler: Nach Buchstabe muss ein Operator kommen
                        return -2;
                    }
                }
                else if(Character.compare(c, '(')==0){
                    if(Character.toString(this.formulaArray[i + 1]).matches("[\\u22C1\\u2227\\u2194\\u2194]")){
                        return -3;
                    }
                }
                else if(Character.compare(c, ')')==0){
                    if(Character.toString(this.formulaArray[i+1]).matches("[a-e\\u00AC(]")){
                        return -4;
                    }
                }
                else if(Character.toString(c).matches("[\\u00AC]")){
                    if(Character.toString(this.formulaArray[i+1]).matches("[\\u22C1\\u2227\\u2192\\u2194)\\u00AC]")){
                        return -5;
                    }
                }
                else if(Character.toString(c).matches("[\\u22C1\\u2227\\u2192\\u2194]")){
                    if(Character.toString(this.formulaArray[i+1]).matches("[\\u2192\\u2194\\u22C1\\u2227)]")){
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
        for(int i = 0; i < this.formulaArray.length; i++){

            //Negation
            if(Character.toString(this.formulaArray[i]).matches("\\u00AC")){
                this.formulaArray[i]='n';
            }// Oder
            else if(Character.toString(this.formulaArray[i]).matches("\\u22C1")){
                this.formulaArray[i]='+';
            }// Und
            else if(Character.toString(this.formulaArray[i]).matches("\\u2227")){
                this.formulaArray[i]='*';
            }// ->
            else if(Character.toString(this.formulaArray[i]).matches("\\u2192")){
                this.formulaArray[i]='1';
            }// <->
            else if(Character.toString(this.formulaArray[i]).matches("\\u2194")){
                this.formulaArray[i]='2';
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

        for(int i = 0; i < this.resultArray.length; i++){

            //Negation
            if(this.resultArray[i]=='n'){
                this.resultArray[i]='\u00AC';
            }// Oder
            else if(this.resultArray[i] == '+'){
                this.resultArray[i]='\u22C1';
            }// Und
            else if(this.resultArray[i]=='*'){
                this.resultArray[i]='\u2227';
            }// ->
            else if(this.resultArray[i]=='1'){
                this.resultArray[i]='\u2192';
            }// <->
            else if(this.resultArray[i]=='2'){
                this.resultArray[i]='\u2194';
            }
        }
    }

    public char[] pfeileAufloesen(char[] formel) {
        char[] bFormel = formel;
        for (int i = 0; i < bFormel.length; i++) {

            // Block vorne
            char c = bFormel[i];
            /* 1 = einseitige Implikation
             * 2 = beidseitige Implikation
             */
            if(c == '1' || c == '2') {
                // vorderer Block bei Klammer
                int laengeVordererBlock = 1;
                if(bFormel[i-1]==')') {
                    int counter = 1;
                    for(int j = 2; counter>0; j++) {
                        char zeichenDavor = bFormel[i-j];
                        if(zeichenDavor == ')') {
                            counter++;
                        }else if(zeichenDavor=='(') {
                            counter--;
                        }
                        laengeVordererBlock++;
                    }
                }
                // Per Regex
                if(Character.toString(bFormel[i-1]).matches("[a-n]")) {
                    laengeVordererBlock = 1;
                }
                char[] blockVorne = new char[laengeVordererBlock];
                for (int j = 0; j < blockVorne.length; j++) {
                    blockVorne[j] = bFormel[i-laengeVordererBlock+j];
                }
                // Block hinten
                int laengeHintererBlock = 1;
                if(bFormel[i+1]=='(') {
                    int counter = 1;
                    for(int j = 2; counter>0; j++) {
                        char zeichenDanach = bFormel[i+j];
                        if(zeichenDanach == '(') {
                            counter++;
                        }else if(zeichenDanach==')') {
                            counter--;
                        }
                        laengeHintererBlock++;
                    }
                }
                // Per Regex
                if(Character.toString(bFormel[i+1]).matches("[a-n]")) {
                    laengeHintererBlock = 1;
                }
                char[] blockHinten = new char[laengeHintererBlock];
                for (int j = 0; j < blockHinten.length; j++) {
                    blockHinten[j] = bFormel[i+j+1];
                }
                if(c=='1') {
                    bFormel = blockEinsetzen(bFormel, einseitigeImplikation(blockVorne, blockHinten), i-laengeVordererBlock, i+laengeHintererBlock);
                }else if(c=='2') {
                    bFormel = blockEinsetzen(bFormel, beidseitigeImplikation(blockVorne, blockHinten), i-laengeVordererBlock, i+laengeHintererBlock);
                }
            }
        }
        return klammernPrüfen(bFormel);
    }

    public char[] klammernPrüfen(char[] bFormel){
        for (int j = 0; j<bFormel.length; j++) {
            if (bFormel[j] == '(') {
                if (klammerNotwendig(bFormel, j)) {
                    continue;
                } else {
                    char[] teilInKlammer = new char[0];
                    int klammern = 1;
                    int count = j + 1;
                    while (klammern > 0) {
                        if (bFormel[count] == ')') {
                            klammern--;
                            if (klammern == 0) {
                                break;
                            }
                        } else if (bFormel[count] == '(') {
                            klammern++;
                        }
                        teilInKlammer = zeichenHinzufügen(teilInKlammer, bFormel[count]);
                        count++;
                    }
                    bFormel = blockEinsetzen(bFormel, teilInKlammer, j, count);
                }
            }
        }
        return bFormel;
    }

    private char[] blockEinsetzen(char[] targetArray, char[] newArray, int anfang, int ende) {
        char[] c = new char[0];
        for (int i = 0; i < anfang; i++) {
            c = zeichenHinzufügen(c, targetArray[i]);
        }
        for (int i = 0; i < newArray.length; i++) {
            c = zeichenHinzufügen(c, newArray[i]);
        }
        for (int i = ende+1; i < targetArray.length; i++) {
            c = zeichenHinzufügen(c, targetArray[i]);
        }
        return c;
    }

    private char[] einseitigeImplikation(char[] b1, char[] b2) {
        char[] result = new char[1];
        result[0]='n';
        for (int i = 0; i < b1.length; i++) {
            result = zeichenHinzufügen(result, b1[i]);
        }
        result = zeichenHinzufügen(result, '+');
        for (int i = 0; i < b2.length; i++) {
            result = zeichenHinzufügen(result, b2[i]);
        }
        return result;
    }

    private char[] beidseitigeImplikation(char[] b1, char[] b2) {
        char[] r1 = einseitigeImplikation(b1, b2);
        char[] r2 = einseitigeImplikation(b2, b1);
        char[] result = new char[1];
        result[0] = '(';
        for (int i = 0; i < r1.length; i++){
            result = zeichenHinzufügen(result, r1[i]);
        }
        result = zeichenHinzufügen(result, ')');
        result = zeichenHinzufügen(result, '*');
        result = zeichenHinzufügen(result, '(');
        for (int i = 0; i < r2.length; i++) {

            result = zeichenHinzufügen(result, r2[i]);
        }
        result = zeichenHinzufügen(result, ')');
        return result;
    }


    public char[] deMorgan(char[] formel) {
        char[] bFormel = formel;
        for (int i = 0; i < bFormel.length; i++) {
            char c = bFormel[i];
            char[] fDeMorgan = new char[0];
            // Zeichen umdrehen und Buchstaben negieren
            if(c=='n' && bFormel[i+1]=='('){
                boolean weiterMachen = true;
                int count = i+2;
                int klammern = 1;
                boolean eineKlammerÜberspringen = false;
                fDeMorgan=zeichenHinzufügen(fDeMorgan, '(');
                while(weiterMachen){
                    if(Character.toString(bFormel[count]).matches("[a-e]")&&bFormel[count-1]=='n'){
                        fDeMorgan = zeichenHinzufügen(fDeMorgan, bFormel[count]);
                    }else if(Character.toString(bFormel[count]).matches("[a-e]")&&bFormel[count-1]!='n'){
                        fDeMorgan = zeichenHinzufügen(fDeMorgan, 'n');
                        fDeMorgan = zeichenHinzufügen(fDeMorgan, bFormel[count]);
                    }else if(bFormel[count]=='+'){
                        fDeMorgan = zeichenHinzufügen(fDeMorgan, '*');
                    }else if(bFormel[count]=='*'){
                        fDeMorgan = zeichenHinzufügen(fDeMorgan, '+');
                    }else if(bFormel[count]=='('){
                        if(klammerNotwendig(bFormel,count)) {
                            fDeMorgan = zeichenHinzufügen(fDeMorgan, '(');
                            klammern++;
                        }else{
                            eineKlammerÜberspringen=true;
                        }
                    }else if(bFormel[count]=='n'&&bFormel[count+1]=='('){
                        count++;
                        continue;
                    }else if(bFormel[count]==')'){
                        if(!eineKlammerÜberspringen) {
                            fDeMorgan = zeichenHinzufügen(fDeMorgan, ')');
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

                if(i-1>0 && count+1 <= bFormel.length){
                    if((bFormel[i-1]=='1'||bFormel[i-1]=='2'||bFormel[i-1]=='*')||
                            (bFormel[count+1]=='1'||bFormel[count+1]=='2'||bFormel[count+1]=='*')){
                        // Klammer muss bestehen bleiben
                        bFormel = blockEinsetzen(bFormel, fDeMorgan, i, count);
                    }else{
                        // Klammer kann weg gemacht werden
                        char[] neueDeMorgan = new char[0];
                        for (int j = 1; j < fDeMorgan.length-1; j++){
                            neueDeMorgan = zeichenHinzufügen(neueDeMorgan, fDeMorgan[j]);
                        }
                        bFormel = blockEinsetzen(bFormel, neueDeMorgan, i, count);
                    }
                }else if(i==0 && count+1 == bFormel.length){
                    // Klammer unnötig
                    char[] neueDeMorgan = new char[0];
                    for (int j = 1; j < fDeMorgan.length-1; j++){
                        neueDeMorgan = zeichenHinzufügen(neueDeMorgan, fDeMorgan[j]);
                    }
                    return neueDeMorgan;
                }else if(i>=0 && count+1 < bFormel.length){
                    char[] neueDeMorgan = new char[0];
                    for (int j = 0; j < fDeMorgan.length; j++){
                        neueDeMorgan = zeichenHinzufügen(neueDeMorgan, fDeMorgan[j]);
                    }
                    bFormel = blockEinsetzen(bFormel, neueDeMorgan, i, count);
                }
            }
        }
        return klammernPrüfen(bFormel);
    }


    private boolean klammerNotwendig(char[] formel, int indexÖffnedeKlammer){
        if(indexÖffnedeKlammer>=0 && indexÖffnedeKlammer+1 <= formel.length) {
            boolean endklammerNichtGefunden = true;
            int indexEndklammer = indexÖffnedeKlammer+1;
            int anzahlKlammern = 1;
            boolean malInKlammer = false;
            boolean plusInKlammer = false;
            while (endklammerNichtGefunden) {
                if (formel[indexEndklammer] == '(') {
                    anzahlKlammern++;
                } else if (formel[indexEndklammer] == ')') {
                    anzahlKlammern--;
                }else if(formel[indexEndklammer]=='*' && anzahlKlammern==1){
                    malInKlammer = true;
                } else if(formel[indexEndklammer]=='+'&& anzahlKlammern==1){
                    plusInKlammer = true;
                }
                if (anzahlKlammern == 0) {
                    endklammerNichtGefunden = false;
                    break;
                }
                indexEndklammer++;
            }
            if(indexÖffnedeKlammer>0 && (indexEndklammer+1)==formel.length){
                if ((formel[indexÖffnedeKlammer - 1] == '*' && plusInKlammer )
                        ||(formel[indexÖffnedeKlammer - 1] == '+' && malInKlammer)
                        || formel[indexÖffnedeKlammer - 1] == '1'
                        || formel[indexÖffnedeKlammer - 1] == '2'
                        || formel[indexÖffnedeKlammer - 1] == 'n') {
                    return true;
                }
            }else if(indexÖffnedeKlammer>0 && (indexEndklammer+1)<=formel.length) {
                if (((formel[indexÖffnedeKlammer - 1] == '*' && plusInKlammer )
                        ||(formel[indexÖffnedeKlammer - 1] == '+' && malInKlammer)
                        || formel[indexÖffnedeKlammer - 1] == '1'
                        || formel[indexÖffnedeKlammer - 1] == '2'
                        || formel[indexÖffnedeKlammer - 1] == 'n') ||
                        ((formel[indexEndklammer + 1] == '*' && plusInKlammer)
                                || (formel[indexEndklammer + 1] == '+' && malInKlammer)
                                || formel[indexEndklammer + 1] == '1'
                                || formel[indexEndklammer + 1] == '2'
                                || formel[indexEndklammer + 1] == 'n')) {
                    return true;
                }
            }else if(indexÖffnedeKlammer==0){
                if ((formel[indexEndklammer + 1] == '*' && plusInKlammer)
                        || (formel[indexEndklammer + 1] == '+' && malInKlammer)
                        || formel[indexEndklammer + 1] == '1'
                        || formel[indexEndklammer + 1] == '2'
                        || formel[indexEndklammer + 1] == 'n') {
                    return true;
                }
            }
        }
        return false;
    }

    public char[] ausaddieren(char[] formel) {
        boolean keineKlammmerGefunden = true;
        for (int i = 0; keineKlammmerGefunden&&i<formel.length-1; i++) {
            // Es zählen immer nur + Zeichen in Verbindung mit Klammern, sonst nix
            if((formel[i]=='+'&&formel[i+1]=='(')||(formel[i]=='+'&&formel[i-1]==')')) {
                keineKlammmerGefunden = false;
                char[] ersterTeil = new char[i+1];
                char[] zweiterTeil = new char[formel.length-1-i];
                boolean vordererTeilInKNF = true;
                for (int j = i; j >= 0; j--) {
                    ersterTeil[j] = formel[j];
                    if(j>=1){
                        if(vordererTeilInKNF&&(
                                (Character.toString(formel[j]).matches("[a-n]") && (formel[j-1] == '+' || formel[j -1] == '(')) //Auf ein Buchstabe darf nur ein + oder )
                                ||(Character.toString(formel[j]).matches("[n]")&&Character.toString(formel[j-1]).matches("[a-n]"))
                                || (formel[j] == '+' && Character.toString(formel[j-1]).matches("[a-n]")) //Auf ein + nur ein Buchstabe
                                || (formel[j] == '*' && formel[j-1] == ')') // auf ein * nur eine (
                                || (formel[j] == '(' && (formel[j-1] == '*' || formel[j-1] == '(')) // auf ein ) nur ein *
                                || (formel[j] == ')' && (Character.toString(formel[j-1]).matches("[a-n]") || formel[j-1] == ')'))
                                )&&i>2) {
                            vordererTeilInKNF=true;
                        }else if(i<2){
                            vordererTeilInKNF=false;
                        }else if(j==i){
                            continue;
                        }else{
                            vordererTeilInKNF=false;
                        }
                    }
                }
                boolean hintererTeilInKNF = true;
                for(int j = 0; j < formel.length-i-1; j++) {
                    zweiterTeil[j] = formel[j+i+1];
                    if (j<formel.length-i-2) {
                        if (hintererTeilInKNF&&(
                                (Character.toString(formel[j + i + 1]).matches("[a-n]") && (formel[j + i + 2] == '+' || formel[j + i + 2] == ')')) //Auf ein Buchstabe darf nur ein + oder )
                                ||(Character.toString(formel[j + i + 1]).matches("[n]")&&Character.toString(formel[j + i + 2]).matches("[a-n]"))
                                || (formel[j + i + 1] == '+' && Character.toString(formel[j + i + 2]).matches("[a-n]")) //Auf ein + nur ein Buchstabe
                                || (formel[j + i + 1] == '*' && formel[j + i + 2] == '(') // auf ein * nur eine (
                                || (formel[j + i + 1] == ')' && (formel[j + i + 2] == '*' || formel[j + i + 2] == ')')) // auf ein ) nur ein *
                                || (formel[j + i + 1] == '(' && (Character.toString(formel[j + i + 2]).matches("[a-n]") || formel[j + i + 2] == '('))
                                )&&formel.length-i-1>1) { // auf ein ( nur ein Buchstabe folgen
                            hintererTeilInKNF = true;
                        } else {
                            hintererTeilInKNF = false;
                        }
                    }
                }
                char[] neuerHinterTeil;

                if(weitereAufloesungNotwendig(zweiterTeil)) {
                    neuerHinterTeil = ausaddieren(zweiterTeil);
                    hintererTeilInKNF = true;
                }else {
                    neuerHinterTeil = new char[zweiterTeil.length];
                    for (int j = 0; j < zweiterTeil.length; j++) {
                        neuerHinterTeil[j] = zweiterTeil[j];
                    }
                }
                List<char[]> listVordereElemente = new ArrayList<char[]>();
                List<char[]> listHintereElemente = new ArrayList<char[]>();
                char[] zeichenErsterBlock = new char[0];
                char[] zeichenZweiterBlock = new char[0];

                /*
                Vordere Block
                 */
                boolean inBlock=true;
                int j = ersterTeil.length-2;
                int klammern = 0;
                int index = 0;
                char[] vordererKNFTeil = new char[0];
                while(inBlock) {
                    if(j == -1  // Am Ende der Reihe
                            || j>=0 // Oder noch nicht am Ende der Reihe
                            &&((ersterTeil[j]=='*'&&Character.toString(ersterTeil[j]).matches("[a-n]"))// Und 'Und' verkn�pfte Buchstaben
                            ||(klammern==0&&ersterTeil[j]=='+')
                            ||(klammern==1&&ersterTeil[j]=='('))) { // Oder es wurde bereits eine Klammer ge�ffnet und es ist eine öffnende Klammer
                        listVordereElemente.add(zeichenErsterBlock);
                        inBlock=false;

                        for (int k = 0; k < j+1; k++){
                            vordererKNFTeil = zeichenHinzufügen(vordererKNFTeil, ersterTeil[k]);
                        }

                        break;
                    }
                    if(Character.toString(ersterTeil[j]).matches("[a-e]")) {
                        zeichenErsterBlock = zeichenHinzufügen(zeichenErsterBlock, ersterTeil[j]);
                        index++;
                    }else if(ersterTeil[j]=='n'){
                        zeichenErsterBlock = zeichenHinzufügen(zeichenErsterBlock, ersterTeil[j+1]);
                        zeichenErsterBlock[index-1]='n';
                        index++;
                    }else if(ersterTeil[j]==')') {
                        klammern--;
                    }else if(ersterTeil[j]=='(') {
                        klammern++;
                    }else if(klammern==0&&ersterTeil[j]=='+') {
                        listVordereElemente.add(zeichenErsterBlock);
                        zeichenErsterBlock=new char[0];
                    }else if(ersterTeil[j]=='*'&&ersterTeil[j+1]=='('&&ersterTeil[j-1]==')'){
                        listVordereElemente.add(zeichenErsterBlock);
                        zeichenErsterBlock=new char[0];
                    }
                    j--;
                }

                /*
                Hintere Block
                 */
                int q = 0;
                int klammernHintererBlock = 0+klammern;
                boolean inHinteremBlock =true;
                char[] hintererKNFTeil = new char[0];
                while(inHinteremBlock) { //||(klammernHintererBlock==0&&neuerHinterTeil[q]=='+')
                    if(q==neuerHinterTeil.length
                            || q<neuerHinterTeil.length
                            && ((neuerHinterTeil[q]=='*'&&Character.toString(neuerHinterTeil[q]).matches("[a-n]")&&!hintererTeilInKNF)
                            ||(klammernHintererBlock==0&&neuerHinterTeil[q]==')')
                            ||(neuerHinterTeil[q]=='*'&&neuerHinterTeil[q+1]=='('&&klammernHintererBlock==0&&!hintererTeilInKNF))) {
                        listHintereElemente.add(zeichenZweiterBlock);
                        inBlock=false;
                        //Ein Teil musste aufgelöst werden, der Rest ist in KNF
                        if(!hintererTeilInKNF&&!weitereAufloesungNotwendig(neuerHinterTeil)){
                            for(int k = q; k< neuerHinterTeil.length; k++){
                                hintererKNFTeil = zeichenHinzufügen(hintererKNFTeil, neuerHinterTeil[k]);
                            }
                        }
                        break;
                    }
                    if(Character.toString(neuerHinterTeil[q]).matches("[a-n]")) {
                        zeichenZweiterBlock = zeichenHinzufügen(zeichenZweiterBlock, neuerHinterTeil[q]);
                    }else if(neuerHinterTeil[q]=='('){
                        klammernHintererBlock++;
                    }else if(neuerHinterTeil[q]==')') {
                        klammernHintererBlock--;
                    }else if(neuerHinterTeil[q]=='*'&&(neuerHinterTeil[q-1]==')'&&neuerHinterTeil[q+1]=='(')) {
                        listHintereElemente.add(zeichenZweiterBlock);
                        zeichenZweiterBlock=new char[0];
                    }
                    q++;
                }

                List<char[]> resultCharSet = new ArrayList<char[]>();
                if(vordererTeilInKNF){
                    for(int w = 0; w<listHintereElemente.size(); w++){
                        for(int e = 0; e<listHintereElemente.get(w).length; e++){
                            for(int r = 0; r<listVordereElemente.size(); r++){
                                char[] c = new char[1];
                                c[0] = '(';
                                for(int t = 0; t<listVordereElemente.get(r).length; t++){
                                    if(listVordereElemente.get(r)[t]=='n'){
                                        c = zeichenHinzufügen(c, listVordereElemente.get(r)[t]);
                                        t++;
                                        c = zeichenHinzufügen(c, listVordereElemente.get(r)[t]);
                                    }else {
                                        c = zeichenHinzufügen(c, listVordereElemente.get(r)[t]);
                                    }
                                    if(t<listVordereElemente.get(r).length-1){
                                        c = zeichenHinzufügen(c, '+');
                                    }
                                }
                                if(!existsChar(c, listHintereElemente.get(w)[e])){
                                    c = zeichenHinzufügen(c, '+');
                                    if(listHintereElemente.get(w)[e]=='n'){
                                        c = zeichenHinzufügen(c, listHintereElemente.get(w)[e]);
                                        e++;
                                        c = zeichenHinzufügen(c, listHintereElemente.get(w)[e]);
                                    }else {
                                        c = zeichenHinzufügen(c, listHintereElemente.get(w)[e]);
                                    }
                                }
                                c = zeichenHinzufügen(c, ')');
                                if(blockHinzufügen(resultCharSet, c)) {
                                    resultCharSet.add(c);
                                }
                            }
                        }
                    }
                }else if(hintererTeilInKNF){
                    for(int w = 0; w<listVordereElemente.size(); w++) {
                        for(int e = 0; e<listVordereElemente.get(w).length; e++) {
                            boolean vordereElementNegiert = false;
                            for(int r = 0; r<listHintereElemente.size(); r++) {
                                char[] c = new char[1];
                                c[0]='(';
                                for(int t = 0; t<listHintereElemente.get(r).length; t++) {
                                    if(listHintereElemente.get(r)[t]=='n'){
                                        c = zeichenHinzufügen(c, listHintereElemente.get(r)[t]);
                                        t++;
                                        c = zeichenHinzufügen(c, listHintereElemente.get(r)[t]);
                                    }else {
                                        c = zeichenHinzufügen(c, listHintereElemente.get(r)[t]);
                                    }
                                    if(t<listHintereElemente.get(r).length-1) {
                                        c = zeichenHinzufügen(c, '+');
                                    }
                                }
                                if(!existsChar(c, listVordereElemente.get(w)[e])) {
                                    c = zeichenHinzufügen(c, '+');

                                    if(listVordereElemente.get(w)[e]=='n'){
                                        c = zeichenHinzufügen(c, listVordereElemente.get(w)[e]);
                                        e++;
                                        c = zeichenHinzufügen(c, listVordereElemente.get(w)[e]);
                                        e--;
                                        vordereElementNegiert = true;
                                    }else {
                                        c = zeichenHinzufügen(c, listVordereElemente.get(w)[e]);
                                    }
                                }
                                c = zeichenHinzufügen(c, ')');
                                if(blockHinzufügen(resultCharSet, c)) {
                                    resultCharSet.add(c);
                                }
                            }
                            if(vordereElementNegiert){
                                e++;
                            }
                        }
                    }
                }else if(!hintererTeilInKNF&&!vordererTeilInKNF) {
                    for(int w = 0; w<listVordereElemente.size(); w++) {
                        for(int e = 0; e<listVordereElemente.get(w).length; e++) {
                            boolean hintereCharNegiert = false;
                            for(int r = 0; r<listHintereElemente.size(); r++) {
                                for(int t = 0; t<listHintereElemente.get(r).length; t++) {
                                    char[] c = new char[1];
                                    c[0]='(';
                                    if(listVordereElemente.get(w)[e]=='n'){
                                        c = zeichenHinzufügen(c, listVordereElemente.get(w)[e]);
                                        e++;
                                        c = zeichenHinzufügen(c, listVordereElemente.get(w)[e]);
                                        e--;
                                        hintereCharNegiert = true;
                                    }else {
                                        c = zeichenHinzufügen(c, listVordereElemente.get(w)[e]);
                                    }
                                    c = zeichenHinzufügen(c, '+');
                                    if(listHintereElemente.get(r)[t]=='n') {
                                        c = zeichenHinzufügen(c, listHintereElemente.get(r)[t]);
                                        t++;
                                        c = zeichenHinzufügen(c, listHintereElemente.get(r)[t]);
                                    }else{
                                        c = zeichenHinzufügen(c, listHintereElemente.get(r)[t]);
                                    }
                                    c = zeichenHinzufügen(c, ')');
                                    if(blockHinzufügen(resultCharSet, c)) {
                                        resultCharSet.add(c);
                                    }
                                }
                            }
                            if(hintereCharNegiert){
                                e++;
                            }
                        }
                    }
                }
                char[] finalResult = new char[0];
                for(int l = 0; l< vordererKNFTeil.length; l++) {
                    finalResult = zeichenHinzufügen(finalResult, vordererKNFTeil[l]);
                }
                finalResult = zeichenHinzufügen(finalResult, '(');
                for (int k = 0; k < resultCharSet.size(); k++) {
                    for(int l = 0; l < resultCharSet.get(k).length;l++) {
                        finalResult = zeichenHinzufügen(finalResult, resultCharSet.get(k)[l]);
                    }
                    if(k < resultCharSet.size()-1) {
                        finalResult = zeichenHinzufügen(finalResult, '*');
                    }
                }
                finalResult = zeichenHinzufügen(finalResult, ')');
                for(int l = 0; l< hintererKNFTeil.length; l++) {
                    finalResult = zeichenHinzufügen(finalResult, hintererKNFTeil[l]);
                }
                char[] rekursiveAuflösung = ausaddieren(finalResult);
                char[] ohneKlammern = new char[0];
                if(vordererKNFTeil.length==0&&hintererKNFTeil.length==0) {
                    for (int l = 1; l < rekursiveAuflösung.length - 1; l++) {
                        ohneKlammern = zeichenHinzufügen(ohneKlammern, rekursiveAuflösung[l]);
                    }
                }else{
                    for (int l = 0; l < rekursiveAuflösung.length; l++) {
                        ohneKlammern = zeichenHinzufügen(ohneKlammern, rekursiveAuflösung[l]);
                    }
                }
                return ohneKlammern;
            }
        }
        return formel;
    }

    public boolean blockHinzufügen(List<char[]> blockList, char[] block){
        for(int i = 0; i < blockList.size(); i++){
            char[] b = blockList.get(i);
            if(b.length == block.length){
                for(int j = 0; j < b.length; j++){
                    boolean match = false;
                    for(int k = 0; k < block.length; k++){
                        if(b[j]==block[k]){
                            match = true;
                        }
                    }
                    if(!match){
                        return true;
                    }
                }
                return false;
            }
        }
        return true;
    }

    public char[] ausmultiplizieren(char[] formel) {
        boolean keineKlammmerGefunden = true;
        for (int i = 0; keineKlammmerGefunden&&i<formel.length-1; i++) {
            // Es zählen immer nur + Zeichen in Verbindung mit Klammern, sonst nix
            if((formel[i]=='*'&&formel[i+1]=='(')||(formel[i]=='+'&&formel[i-1]==')')) {
                keineKlammmerGefunden = false;
                char[] ersterTeil = new char[i+1];
                char[] zweiterTeil = new char[formel.length-1-i];
                boolean vordererTeilInKNF = true;
                for (int j = i; j >= 0; j--) {
                    ersterTeil[j] = formel[j];
                    if(j>=1){
                        if(vordererTeilInKNF&&(
                                (Character.toString(formel[j]).matches("[a-n]") && (formel[j-1] == '*' || formel[j -1] == '(')) //Auf ein Buchstabe darf nur ein + oder )
                                        ||(Character.toString(formel[j]).matches("[n]")&&Character.toString(formel[j-1]).matches("[a-n]"))
                                        || (formel[j] == '*' && Character.toString(formel[j-1]).matches("[a-n]")) //Auf ein + nur ein Buchstabe
                                        || (formel[j] == '+' && formel[j-1] == ')') // auf ein * nur eine (
                                        || (formel[j] == '(' && (formel[j-1] == '+' || formel[j-1] == '(')) // auf ein ) nur ein *
                                        || (formel[j] == ')' && (Character.toString(formel[j-1]).matches("[a-n]") || formel[j-1] == ')'))
                        )&&i>2) {
                            vordererTeilInKNF=true;
                        }else if(i<2){
                            vordererTeilInKNF=false;
                        }else if(j==i){
                            continue;
                        }else{
                            vordererTeilInKNF=false;
                        }
                    }
                }
                boolean hintererTeilInKNF = true;
                for(int j = 0; j < formel.length-i-1; j++) {
                    zweiterTeil[j] = formel[j+i+1];
                    if (j<formel.length-i-2) {
                        if (hintererTeilInKNF&&(
                                (Character.toString(formel[j + i + 1]).matches("[a-n]") && (formel[j + i + 2] == '*' || formel[j + i + 2] == ')')) //Auf ein Buchstabe darf nur ein + oder )
                                        ||(Character.toString(formel[j + i + 1]).matches("[n]")&&Character.toString(formel[j + i + 2]).matches("[a-n]"))
                                        || (formel[j + i + 1] == '*' && Character.toString(formel[j + i + 2]).matches("[a-n]")) //Auf ein + nur ein Buchstabe
                                        || (formel[j + i + 1] == '+' && formel[j + i + 2] == '(') // auf ein * nur eine (
                                        || (formel[j + i + 1] == ')' && (formel[j + i + 2] == '+' || formel[j + i + 2] == ')')) // auf ein ) nur ein *
                                        || (formel[j + i + 1] == '(' && (Character.toString(formel[j + i + 2]).matches("[a-n]") || formel[j + i + 2] == '('))
                        )&&formel.length-i-1>1) { // auf ein ( nur ein Buchstabe folgen
                            hintererTeilInKNF = true;
                        } else {
                            hintererTeilInKNF = false;
                        }
                    }
                }
                char[] neuerHinterTeil;

                if(weitereAufloesungNotwendig(zweiterTeil)) {
                    neuerHinterTeil = ausaddieren(zweiterTeil);
                    hintererTeilInKNF = true;
                }else {
                    neuerHinterTeil = new char[zweiterTeil.length];
                    for (int j = 0; j < zweiterTeil.length; j++) {
                        neuerHinterTeil[j] = zweiterTeil[j];
                    }
                }
                List<char[]> listVordereElemente = new ArrayList<char[]>();
                List<char[]> listHintereElemente = new ArrayList<char[]>();
                char[] zeichenErsterBlock = new char[0];
                char[] zeichenZweiterBlock = new char[0];

                /*
                Vordere Block
                 */
                boolean inBlock=true;
                int j = ersterTeil.length-2;
                int klammern = 0;
                int index = 0;
                char[] vordererKNFTeil = new char[0];
                while(inBlock) {
                    if(j == -1  // Am Ende der Reihe
                            || j>=0 // Oder noch nicht am Ende der Reihe
                            &&((ersterTeil[j]=='*'&&Character.toString(ersterTeil[j]).matches("[a-n]"))// Und 'Und' verkn�pfte Buchstaben
                            ||(klammern==0&&ersterTeil[j]=='*')
                            ||(klammern==1&&ersterTeil[j]=='('))) { // Oder es wurde bereits eine Klammer ge�ffnet und es ist eine öffnende Klammer
                        listVordereElemente.add(zeichenErsterBlock);
                        inBlock=false;

                        for (int k = 0; k < j+1; k++){
                            vordererKNFTeil = zeichenHinzufügen(vordererKNFTeil, ersterTeil[k]);
                        }

                        break;
                    }
                    if(Character.toString(ersterTeil[j]).matches("[a-e]")) {
                        zeichenErsterBlock = zeichenHinzufügen(zeichenErsterBlock, ersterTeil[j]);
                        index++;
                    }else if(ersterTeil[j]=='n'){
                        zeichenErsterBlock = zeichenHinzufügen(zeichenErsterBlock, ersterTeil[j+1]);
                        zeichenErsterBlock[index-1]='n';
                        index++;
                    }else if(ersterTeil[j]==')') {
                        klammern--;
                    }else if(ersterTeil[j]=='(') {
                        klammern++;
                    }else if(klammern==0&&ersterTeil[j]=='*') {
                        listVordereElemente.add(zeichenErsterBlock);
                        zeichenErsterBlock=new char[0];
                    }else if(ersterTeil[j]=='*'&&ersterTeil[j+1]=='('&&ersterTeil[j-1]==')'){
                        listVordereElemente.add(zeichenErsterBlock);
                        zeichenErsterBlock=new char[0];
                    }
                    j--;
                }

                /*
                Hintere Block
                 */
                int q = 0;
                int klammernHintererBlock = 0+klammern;
                boolean inHinteremBlock =true;
                char[] hintererKNFTeil = new char[0];
                while(inHinteremBlock) { //||(klammernHintererBlock==0&&neuerHinterTeil[q]=='+')
                    if(q==neuerHinterTeil.length
                            || q<neuerHinterTeil.length
                            && ((neuerHinterTeil[q]=='+'&&Character.toString(neuerHinterTeil[q]).matches("[a-n]")&&!hintererTeilInKNF)
                            ||(klammernHintererBlock==0&&neuerHinterTeil[q]==')')
                            ||(neuerHinterTeil[q]=='+'&&neuerHinterTeil[q+1]=='('&&klammernHintererBlock==0&&!hintererTeilInKNF))) {
                        listHintereElemente.add(zeichenZweiterBlock);
                        inBlock=false;
                        //Ein Teil musste aufgelöst werden, der Rest ist in KNF
                        if(!hintererTeilInKNF&&!weitereAufloesungNotwendig(neuerHinterTeil)){
                            for(int k = q; k< neuerHinterTeil.length; k++){
                                hintererKNFTeil = zeichenHinzufügen(hintererKNFTeil, neuerHinterTeil[k]);
                            }
                        }
                        break;
                    }
                    if(Character.toString(neuerHinterTeil[q]).matches("[a-n]")) {
                        zeichenZweiterBlock = zeichenHinzufügen(zeichenZweiterBlock, neuerHinterTeil[q]);
                    }else if(neuerHinterTeil[q]=='('){
                        klammernHintererBlock++;
                    }else if(neuerHinterTeil[q]==')') {
                        klammernHintererBlock--;
                    }else if(neuerHinterTeil[q]=='+'&&(neuerHinterTeil[q-1]==')'&&neuerHinterTeil[q+1]=='(')) {
                        listHintereElemente.add(zeichenZweiterBlock);
                        zeichenZweiterBlock=new char[0];
                    }
                    q++;
                }

                List<char[]> resultCharSet = new ArrayList<char[]>();
                if(vordererTeilInKNF){
                    for(int w = 0; w<listHintereElemente.size(); w++){
                        for(int e = 0; e<listHintereElemente.get(w).length; e++){
                            for(int r = 0; r<listVordereElemente.size(); r++){
                                char[] c = new char[1];
                                c[0] = '(';
                                for(int t = 0; t<listVordereElemente.get(r).length; t++){
                                    if(listVordereElemente.get(r)[t]=='n'){
                                        c = zeichenHinzufügen(c, listVordereElemente.get(r)[t]);
                                        t++;
                                        c = zeichenHinzufügen(c, listVordereElemente.get(r)[t]);
                                    }else {
                                        c = zeichenHinzufügen(c, listVordereElemente.get(r)[t]);
                                    }
                                    if(t<listVordereElemente.get(r).length-1){
                                        c = zeichenHinzufügen(c, '*');
                                    }
                                }
                                if(!existsChar(c, listHintereElemente.get(w)[e])){
                                    c = zeichenHinzufügen(c, '*');
                                    if(listHintereElemente.get(w)[e]=='n'){
                                        c = zeichenHinzufügen(c, listHintereElemente.get(w)[e]);
                                        e++;
                                        c = zeichenHinzufügen(c, listHintereElemente.get(w)[e]);
                                    }else {
                                        c = zeichenHinzufügen(c, listHintereElemente.get(w)[e]);
                                    }
                                }
                                c = zeichenHinzufügen(c, ')');
                                if(blockHinzufügen(resultCharSet, c)) {
                                    resultCharSet.add(c);
                                }
                            }
                        }
                    }
                }else if(hintererTeilInKNF){
                    for(int w = 0; w<listVordereElemente.size(); w++) {
                        for(int e = 0; e<listVordereElemente.get(w).length; e++) {
                            boolean vordereElementNegiert = false;
                            for(int r = 0; r<listHintereElemente.size(); r++) {
                                char[] c = new char[1];
                                c[0]='(';
                                for(int t = 0; t<listHintereElemente.get(r).length; t++) {
                                    if(listHintereElemente.get(r)[t]=='n'){
                                        c = zeichenHinzufügen(c, listHintereElemente.get(r)[t]);
                                        t++;
                                        c = zeichenHinzufügen(c, listHintereElemente.get(r)[t]);
                                    }else {
                                        c = zeichenHinzufügen(c, listHintereElemente.get(r)[t]);
                                    }
                                    if(t<listHintereElemente.get(r).length-1) {
                                        c = zeichenHinzufügen(c, '*');
                                    }
                                }
                                if(!existsChar(c, listVordereElemente.get(w)[e])) {
                                    c = zeichenHinzufügen(c, '*');

                                    if(listVordereElemente.get(w)[e]=='n'){
                                        c = zeichenHinzufügen(c, listVordereElemente.get(w)[e]);
                                        e++;
                                        c = zeichenHinzufügen(c, listVordereElemente.get(w)[e]);
                                        e--;
                                        vordereElementNegiert = true;
                                    }else {
                                        c = zeichenHinzufügen(c, listVordereElemente.get(w)[e]);
                                    }
                                }
                                c = zeichenHinzufügen(c, ')');
                                if(blockHinzufügen(resultCharSet, c)) {
                                    resultCharSet.add(c);
                                }
                            }
                            if(vordereElementNegiert){
                                e++;
                            }
                        }
                    }
                }else if(!hintererTeilInKNF&&!vordererTeilInKNF) {
                    for(int w = 0; w<listVordereElemente.size(); w++) {
                        for(int e = 0; e<listVordereElemente.get(w).length; e++) {
                            boolean hintereCharNegiert = false;
                            for(int r = 0; r<listHintereElemente.size(); r++) {
                                for(int t = 0; t<listHintereElemente.get(r).length; t++) {
                                    char[] c = new char[1];
                                    c[0]='(';
                                    if(listVordereElemente.get(w)[e]=='n'){
                                        c = zeichenHinzufügen(c, listVordereElemente.get(w)[e]);
                                        e++;
                                        c = zeichenHinzufügen(c, listVordereElemente.get(w)[e]);
                                        e--;
                                        hintereCharNegiert = true;
                                    }else {
                                        c = zeichenHinzufügen(c, listVordereElemente.get(w)[e]);
                                    }
                                    c = zeichenHinzufügen(c, '*');
                                    if(listHintereElemente.get(r)[t]=='n') {
                                        c = zeichenHinzufügen(c, listHintereElemente.get(r)[t]);
                                        t++;
                                        c = zeichenHinzufügen(c, listHintereElemente.get(r)[t]);
                                    }else{
                                        c = zeichenHinzufügen(c, listHintereElemente.get(r)[t]);
                                    }
                                    c = zeichenHinzufügen(c, ')');
                                    if(blockHinzufügen(resultCharSet, c)) {
                                        resultCharSet.add(c);
                                    }
                                }
                            }
                            if(hintereCharNegiert){
                                e++;
                            }
                        }
                    }
                }
                char[] finalResult = new char[0];
                for(int l = 0; l< vordererKNFTeil.length; l++) {
                    finalResult = zeichenHinzufügen(finalResult, vordererKNFTeil[l]);
                }
                finalResult = zeichenHinzufügen(finalResult, '(');
                for (int k = 0; k < resultCharSet.size(); k++) {
                    for(int l = 0; l < resultCharSet.get(k).length;l++) {
                        finalResult = zeichenHinzufügen(finalResult, resultCharSet.get(k)[l]);
                    }
                    if(k < resultCharSet.size()-1) {
                        finalResult = zeichenHinzufügen(finalResult, '+');
                    }
                }
                finalResult = zeichenHinzufügen(finalResult, ')');
                for(int l = 0; l< hintererKNFTeil.length; l++) {
                    finalResult = zeichenHinzufügen(finalResult, hintererKNFTeil[l]);
                }
                char[] rekursiveAuflösung = ausaddieren(finalResult);
                char[] ohneKlammern = new char[0];
                if(vordererKNFTeil.length==0&&hintererKNFTeil.length==0) {
                    for (int l = 1; l < rekursiveAuflösung.length - 1; l++) {
                        ohneKlammern = zeichenHinzufügen(ohneKlammern, rekursiveAuflösung[l]);
                    }
                }else{
                    for (int l = 0; l < rekursiveAuflösung.length; l++) {
                        ohneKlammern = zeichenHinzufügen(ohneKlammern, rekursiveAuflösung[l]);
                    }
                }
                return ohneKlammern;
            }
        }
        return formel;
    }

    private boolean existsChar(char[] charArray, char c) {
        for (int i = 0; i < charArray.length; i++) {
            if(charArray[i]==c && c !='n') {
                return true;
            }
        }
        return false;
    }

    private char[] zeichenHinzufügen(char[] zeichenSatz, char z) {
        char[] newZeichenSatz = new char[zeichenSatz.length+1];
        for (int i = 0; i < zeichenSatz.length; i++) {
            newZeichenSatz[i] = zeichenSatz[i];
        }
        newZeichenSatz[zeichenSatz.length] = z;
        return newZeichenSatz;
    }

    private boolean weitereAufloesungNotwendig(char[] formel) {
        for (int i = 0; i < formel.length; i++) {
            if(i+1<formel.length) {
                if ((formel[i] == '+' && formel[i + 1] == '(') || (formel[i] == '+' && formel[i - 1] == ')')) {
                    return true;
                }
            }
        }
        return false;
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
