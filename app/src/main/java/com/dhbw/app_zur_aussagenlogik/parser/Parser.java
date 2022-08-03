package com.dhbw.app_zur_aussagenlogik.parser;

import com.dhbw.app_zur_aussagenlogik.Modi;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    private static Parser parser = new Parser();

    public static Parser getInstance() {
        return Parser.parser;
    }

    private Modi modus;

    private String formula;
    private char[] formulaArray;

    // Methode zum setzen

    public void setParserParameter(Modi modus, String formula){
        setModus(modus);
        parseFormula(formula);
    }

    public int parseFormula(String formula){
        String unparsedFormula = formula;

        // Formel parsen
        formulaArray = new char[formula.length()];
        for(int i = 0; i<formula.length(); i++){
            formulaArray[i] = formula.charAt(i);
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
            return fehlercode;
        }
        setFormula(parsedFormula);
        //char[] knfNormalform = ausaddieren(deMorgan(pfeileAufloesen(this.formulaArray)));
        return fehlercode;
    }

    private int checkUserInput(){
        // Zähler für Klamern
        int countOpen = 0;
        int countClose = 0;

        for(int i = 0; i < formulaArray.length; i++){
            char c = formulaArray[i];

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
            if((i+1)<formulaArray.length) {
                if(Character.toString(c).matches("[a-n]")){
                    if (Character.toString(formulaArray[i + 1]).matches("[a-e(\\u00AC]")){
                        // Fehler: Nach Buchstabe muss ein Operator kommen
                        return -2;
                    }
                }
                else if(Character.compare(c, '(')==0){
                    if(Character.toString(formulaArray[i + 1]).matches("[\\u22C1\\u2227\\u2194\\u2194]")){
                        return -3;
                    }
                }
                else if(Character.compare(c, ')')==0){
                    if(Character.toString(formulaArray[i+1]).matches("[a-e\\u00AC(]")){
                        return -4;
                    }
                }
                else if(Character.toString(c).matches("[\\u00AC]")){
                    if(Character.toString(formulaArray[i+1]).matches("[\\u22C1\\u2227\\u2192\\u2194)\\u00AC]")){
                        return -5;
                    }
                }
                else if(Character.toString(c).matches("[\\u22C1\\u2227\\u2192\\u2194]")){
                    if(Character.toString(formulaArray[i+1]).matches("[\\u2192\\u2194\\u22C1\\u2227)]")){
                        return -6;
                    }
                }
            }

        }
        if(countOpen != countClose){
            // Fehlermeldung
            return -1;
        }
        return 42;
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

        // Unnötige Klammern weg machen
        /*for (int j = 0; j<bFormel.length; j++){
            if(bFormel[j]=='(' && klammerNotwendig(bFormel,j)){
                continue;
            }else{
                char[] teilInKlammer = new char[0];
                int klammern = 1;
                int count = j+1;
                while(klammern>0){
                    if(bFormel[count]==')'){
                        klammern--;
                        if(klammern==0){
                            break;
                        }
                    }else if(bFormel[count]=='('){
                        klammern++;
                    }
                    teilInKlammer = zeichenHinzufügen(teilInKlammer, bFormel[count]);
                    count++;
                }
                bFormel = blockEinsetzen(bFormel, teilInKlammer, j, count);
            }
        }
        */

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
        return bFormel;
    }


    private boolean klammerNotwendig(char[] formel, int indexÖffnedeKlammer){
        if(indexÖffnedeKlammer-1>0 && indexÖffnedeKlammer+1 <= formel.length) {
            boolean endklammerNichtGefunden = true;
            int indexEndklammer = indexÖffnedeKlammer+1;
            int anzahlKlammern = 1;
            while (endklammerNichtGefunden) {
                if (formel[indexEndklammer] == '(') {
                    anzahlKlammern++;
                } else if (formel[indexEndklammer] == ')') {
                    anzahlKlammern--;
                }
                if (anzahlKlammern == 0) {
                    endklammerNichtGefunden = false;
                    break;
                }
                indexEndklammer++;
            }
            if ((formel[indexÖffnedeKlammer - 1] == '*' || formel[indexÖffnedeKlammer - 1] == '1' || formel[indexÖffnedeKlammer - 1] == '2' || formel[indexÖffnedeKlammer - 1] == 'n') ||
                    (formel[indexEndklammer + 1] == '*' || formel[indexEndklammer + 1] == '1' || formel[indexEndklammer + 1] == '2' || formel[indexEndklammer + 1] == 'n')) {
                return true;
            }
        }
        return false;
    }


    public char[] ausaddieren(char[] formel) {
        boolean keineKlammmerGefunden = true;
        for (int i = 0; keineKlammmerGefunden; i++) {
            if((formel[i]=='+'&&formel[i+1]=='(')||(formel[i]=='+'&&formel[i-1]==')')||(formel[i]=='+'&&Character.toString(formel[i+1]).matches("[a-n]"))) {
                keineKlammmerGefunden = false;
                char[] ersterTeil = new char[i+1];
                char[] zweiterTeil = new char[formel.length-1-i];
                for (int j = i; j >= 0; j--) {
                    ersterTeil[j] = formel[j];
                }
                for(int j = 0; j < formel.length-i-1; j++) {
                    zweiterTeil[j] = formel[j+i+1];
                }
                char[] neuerHinterTeil;
                boolean hintererTeilInKNF = false;
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
                boolean inBlock=true;
                int j = 0;
                int klammern = 0;
                while(inBlock) {
                    if(j == ersterTeil.length  // Am Ende der Reihe
                            || j<ersterTeil.length // Oder noch nicht am Ende der Reihe
                            &&((ersterTeil[j]=='*'&&Character.toString(ersterTeil[j]).matches("[a-n]")) // Und 'Und' verkn�pfte Buchstaben
                            ||(klammern==1&&ersterTeil[j]==')'))) { // Oder es wurde bereits eine Klammer ge�ffnet und es ist eine schlie�ende Klammer
                        listVordereElemente.add(zeichenErsterBlock);
                        inBlock=false;
                        break;
                    }
                    if(Character.toString(ersterTeil[j]).matches("[a-n]")) {
                        zeichenErsterBlock = zeichenHinzufügen(zeichenErsterBlock, ersterTeil[j]);
                    }else if(ersterTeil[j]==')') {
                        klammern++;
                    }else if(ersterTeil[j]=='(') {
                        klammern--;
                    }else if(klammern==0&&ersterTeil[j]=='+') {
                        listVordereElemente.add(zeichenErsterBlock);
                        zeichenErsterBlock=new char[0];
                    }
                    j++;
                }
                int q = 0;
                int klammernHintererBlock = 0;
                boolean inHinteremBlock =true;
                while(inHinteremBlock) { //||(klammernHintererBlock==0&&neuerHinterTeil[q]=='+')
                    if(q==neuerHinterTeil.length
                            || q<neuerHinterTeil.length
                            && ((neuerHinterTeil[q]=='*'&&Character.toString(neuerHinterTeil[q]).matches("[a-n]"))
                            ||(klammernHintererBlock==1&&neuerHinterTeil[q]=='('))) {
                        listHintereElemente.add(zeichenZweiterBlock);
                        inBlock=false;
                        break;
                    }
                    if(Character.toString(neuerHinterTeil[q]).matches("[a-n]")) {
                        zeichenZweiterBlock = zeichenHinzufügen(zeichenZweiterBlock, neuerHinterTeil[q]);
                    }else if(neuerHinterTeil[q]=='('){
                        klammernHintererBlock++;
                    }else if(neuerHinterTeil[q]==')') {
                        klammernHintererBlock--;
                    }else if(neuerHinterTeil[q]=='*') {
                        listHintereElemente.add(zeichenZweiterBlock);
                        zeichenZweiterBlock=new char[0];
                    }
                    q++;
                }
                List<char[]> resultCharSet = new ArrayList<char[]>();
                if(!hintererTeilInKNF) {
                    for(int w = 0; w<listVordereElemente.size(); w++) {
                        for(int e = 0; e<listVordereElemente.get(w).length; e++) {
                            for(int r = 0; r<listHintereElemente.size(); r++) {
                                for(int t = 0; t<listHintereElemente.get(r).length; t++) {
                                    char[] c = new char[1];
                                    c[0]='(';
                                    if(listVordereElemente.get(w)[e]=='n'){
                                        c = zeichenHinzufügen(c, listVordereElemente.get(w)[e]);
                                        e++;
                                        c = zeichenHinzufügen(c, listVordereElemente.get(w)[e]);
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
                                    resultCharSet.add(c);
                                }
                            }
                        }
                    }
                }else {
                    for(int w = 0; w<listVordereElemente.size(); w++) {
                        for(int e = 0; e<listVordereElemente.get(w).length; e++) {
                            for(int r = 0; r<listHintereElemente.size(); r++) {
                                char[] c = new char[1];
                                c[0]='(';
                                for(int t = 0; t<listHintereElemente.get(r).length; t++) {
                                    c = zeichenHinzufügen(c, listHintereElemente.get(r)[t]);
                                    if(t<listHintereElemente.get(r).length-1) {
                                        c = zeichenHinzufügen(c, '+');
                                    }
                                }
                                if(!existsChar(c, listVordereElemente.get(w)[e])) {
                                    c = zeichenHinzufügen(c, '+');
                                    c = zeichenHinzufügen(c, listVordereElemente.get(w)[e]);
                                }
                                c = zeichenHinzufügen(c, ')');
                                resultCharSet.add(c);
                            }
                        }
                    }
                }
                char[] finalResult = new char[0];
                for (int k = 0; k < resultCharSet.size(); k++) {
                    for(int l = 0; l < resultCharSet.get(k).length;l++) {
                        finalResult = zeichenHinzufügen(finalResult, resultCharSet.get(k)[l]);
                    }
                    if(k < resultCharSet.size()-1) {
                        finalResult = zeichenHinzufügen(finalResult, '*');
                    }
                }
                return finalResult;
            }
        }
        return null;
    }

    public char[] ausmultiplizieren(char[] formel) {
        boolean keineKlammmerGefunden = true;
        for (int i = 0; keineKlammmerGefunden; i++) {
            if((formel[i]=='*'&&formel[i+1]=='(')||(formel[i]=='*'&&formel[i-1]==')')||(formel[i]=='*'&&Character.toString(formel[i+1]).matches("[a-n]"))) {
                keineKlammmerGefunden = false;
                char[] ersterTeil = new char[i+1];
                char[] zweiterTeil = new char[formel.length-1-i];
                for (int j = i; j >= 0; j--) {
                    ersterTeil[j] = formel[j];
                }
                for(int j = 0; j < formel.length-i-1; j++) {
                    zweiterTeil[j] = formel[j+i+1];
                }
                char[] neuerHinterTeil;
                boolean hintererTeilInKNF = false;
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
                boolean inBlock=true;
                int j = 0;
                int klammern = 0;
                while(inBlock) {
                    if(j == ersterTeil.length  // Am Ende der Reihe
                            || j<ersterTeil.length // Oder noch nicht am Ende der Reihe
                            &&((ersterTeil[j]=='+'&&Character.toString(ersterTeil[j]).matches("[a-n]")) // Und 'Und' verkn�pfte Buchstaben
                            ||(klammern==1&&ersterTeil[j]==')'))) { // Oder es wurde bereits eine Klammer ge�ffnet und es ist eine schlie�ende Klammer
                        listVordereElemente.add(zeichenErsterBlock);
                        inBlock=false;
                        break;
                    }
                    if(Character.toString(ersterTeil[j]).matches("[a-n]")) {
                        zeichenErsterBlock = zeichenHinzufügen(zeichenErsterBlock, ersterTeil[j]);
                    }else if(ersterTeil[j]==')') {
                        klammern++;
                    }else if(ersterTeil[j]=='(') {
                        klammern--;
                    }else if(klammern==0&&ersterTeil[j]=='*') {
                        listVordereElemente.add(zeichenErsterBlock);
                        zeichenErsterBlock=new char[0];
                    }
                    j++;
                }
                int q = 0;
                int klammernHintererBlock = 0;
                boolean inHinteremBlock =true;
                while(inHinteremBlock) { //||(klammernHintererBlock==0&&neuerHinterTeil[q]=='+')
                    if(q==neuerHinterTeil.length
                            || q<neuerHinterTeil.length
                            && ((neuerHinterTeil[q]=='+'&&Character.toString(neuerHinterTeil[q]).matches("[a-n]"))
                            ||(klammernHintererBlock==1&&neuerHinterTeil[q]=='('))) {
                        listHintereElemente.add(zeichenZweiterBlock);
                        inBlock=false;
                        break;
                    }
                    if(Character.toString(neuerHinterTeil[q]).matches("[a-n]")) {
                        zeichenZweiterBlock = zeichenHinzufügen(zeichenZweiterBlock, neuerHinterTeil[q]);
                    }else if(neuerHinterTeil[q]=='('){
                        klammernHintererBlock++;
                    }else if(neuerHinterTeil[q]==')') {
                        klammernHintererBlock--;
                    }else if(neuerHinterTeil[q]=='+') {
                        listHintereElemente.add(zeichenZweiterBlock);
                        zeichenZweiterBlock=new char[0];
                    }
                    q++;
                }
                List<char[]> resultCharSet = new ArrayList<char[]>();
                if(!hintererTeilInKNF) {
                    for(int w = 0; w<listVordereElemente.size(); w++) {
                        for(int e = 0; e<listVordereElemente.get(w).length; e++) {
                            for(int r = 0; r<listHintereElemente.size(); r++) {
                                for(int t = 0; t<listHintereElemente.get(r).length; t++) {
                                    char[] c = new char[1];
                                    c[0]='(';
                                    if(listVordereElemente.get(w)[e]=='n'){
                                        c = zeichenHinzufügen(c, listVordereElemente.get(w)[e]);
                                        e++;
                                        c = zeichenHinzufügen(c, listVordereElemente.get(w)[e]);
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
                                    resultCharSet.add(c);
                                }
                            }
                        }
                    }
                }else {
                    for(int w = 0; w<listVordereElemente.size(); w++) {
                        for(int e = 0; e<listVordereElemente.get(w).length; e++) {
                            for(int r = 0; r<listHintereElemente.size(); r++) {

                                char[] c = new char[1];
                                c[0]='(';
                                for(int t = 0; t<listHintereElemente.get(r).length; t++) {
                                    c = zeichenHinzufügen(c, listHintereElemente.get(r)[t]);
                                    if(t<listHintereElemente.get(r).length-1) {
                                        c = zeichenHinzufügen(c, '*');
                                    }
                                }
                                if(!existsChar(c, listVordereElemente.get(w)[e])) {
                                    c = zeichenHinzufügen(c, '*');
                                    c = zeichenHinzufügen(c, listVordereElemente.get(w)[e]);
                                }
                                c = zeichenHinzufügen(c, ')');
                                resultCharSet.add(c);
                            }
                        }
                    }
                }
                char[] finalResult = new char[0];
                for (int k = 0; k < resultCharSet.size(); k++) {
                    for(int l = 0; l < resultCharSet.get(k).length;l++) {
                        finalResult = zeichenHinzufügen(finalResult, resultCharSet.get(k)[l]);
                    }
                    if(k < resultCharSet.size()-1) {
                        finalResult = zeichenHinzufügen(finalResult, '+');
                    }
                }
                return finalResult;
            }
        }
        return null;
    }


    private boolean existsChar(char[] charArray, char c) {
        for (int i = 0; i < charArray.length; i++) {
            if(charArray[i]==c) {
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
            if((formel[i]=='+'&&formel[i+1]=='(')||(formel[i]=='+'&&formel[i-1]==')')||(formel[i]=='+'&&Character.toString(formel[i+1]).matches("[a-n]"))) {
                return true;
            }
        }
        return false;
    }
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
