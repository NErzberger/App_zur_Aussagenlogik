package com.dhbw.app_zur_aussagenlogik.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Ausaddieren extends Normalformen{

    public static Formel ausaddieren(Formel formel) {
        List<List<char[]>> blockList = new ArrayList<>();
        List<char[]> innererBlock = new ArrayList<>();
        char[] block = new char[0];
        int klammer = 0;
        int indexKlammer = 0;
        for (int i = 0; i < formel.length(); i++) {

            char c = formel.getChar(i);

            if (Character.toString(c).matches("[a-z]")) {

                block = zeichenHinzufügen(block, c);

// C ist ein Mal und die Zeichen davor und danach sind Buchstaben
            } else if (c == '*' && Character.toString(formel.getChar(i - 1)).matches("[a-z]")
                    && Character.toString(formel.getChar(i + 1)).matches("[a-z]")) {
                continue;

// C ist ein Plus oder C ist ein Mal und entweder das Zeichen davor oder danach ist kein Buchstabe
            } else if (c == '+' && block.length > 0) {
                innererBlock.add(block);
                block = new char[0];
                if (klammer == 0) {
                    blockList.add(innererBlock);
                    blockList.add(new ArrayList<char[]>(Arrays.asList(new char[] { '+' })));
                    innererBlock = new ArrayList<>();
                }
            } else if (block.length > 0 && klammer == 0 && c == '*'
                    && (!Character.toString(formel.getChar(i - 1)).matches("[a-z]")
                    || !Character.toString(formel.getChar(i + 1)).matches("[a-z]"))) {
                innererBlock.add(block);
                block = new char[0];

                blockList.add(innererBlock);
                blockList.add(new ArrayList<char[]>(Arrays.asList(new char[] { '*' })));
                innererBlock = new ArrayList<>();

                /*
                 * Rekursion Rechts
                 *
                 */
            } else if (klammer > 0 && c == '*' && !Character.toString(formel.getChar(i + 1)).matches("[a-z]")) {
// Neue Formel bilden
                String formelString = "";

				/*for (int j = 0; j < block.length; j++) {
					formelString = formelString + block[j] + "*";
				}*/
                int counterLinks = 0;
                int klammernLinks = 0;
                while(true) {
                    char formelChar = formel.getChar(i - counterLinks );
                    if (formelChar == '(') {
                        if(klammernLinks== 0) {
                            break;
                        }
                        klammernLinks--;
                    }
                    else if(formelChar==')') {
                        klammernLinks++;
                    }
                    else if (formelChar == '+' && klammernLinks==0) {
                        break;
                    }
                    formelString = formelString + formelChar;
                    counterLinks++;
                }
                formelString = new StringBuilder(formelString).reverse().toString();

                int klammernRechts = 1;
                int counterRechts = 1;
                while (true) {
                    char formelChar = formel.getChar(i + counterRechts);
                    if (formelChar == '(') {
                        klammernRechts++;
                    } else if (formelChar == ')') {
                        klammernRechts--;
                        if (klammernRechts == 0) {
                            break;
                        }
                    }
                    formelString = formelString + formelChar;
                    counterRechts++;
                }
                Formel newFormel = ausaddieren(new Formel(formelString));

                formel.blockEinsetzen(newFormel, i - counterLinks + 1, i - 1 + counterRechts);
                klammer++;
                return ausaddieren(formel);
                // i = i + newFormel.length();

                /*
                 * Linke Rekursion
                 *
                 */
            } else if (klammer > 0 && c == '*' && !Character.toString(formel.getChar(i - 1)).matches("[a-z]")) {
                // Neue Formel bilden
                String formelStringRechts = "";
                String formelStringLinks = "";

                int counterRechts = 0;
                int klammernRechts = 0;
                while (true) {
                    char formelChar = formel.getChar(i + counterRechts);
                    if(formelChar=='(') {
                        klammernRechts++;
                    }
                    else if(formelChar==')') {
                        if(klammernRechts==0) {
                            break;
                        }
                        klammernRechts--;
                    }
                    else if (formelChar == '+'&&klammernRechts==0) {
                        break;
                    }
                    formelStringRechts = formelStringRechts + formelChar;
                    counterRechts++;
                }

                int innereKlammerLinks = 1;
                int counterLinks = 1;
                while (true) {
                    char formelChar = formel.getChar(i - counterLinks);
                    if (formelChar == ')') {
                        innereKlammerLinks++;
                    } else if (formelChar == '(') {
                        innereKlammerLinks--;
                        if (innereKlammerLinks == 0) {
                            break;
                        }
                    }
                    formelStringLinks = formelStringLinks + formelChar;
                    counterLinks++;
                }

                formelStringLinks = new StringBuilder(formelStringLinks).reverse().toString();
                formelStringLinks = formelStringLinks + formelStringRechts;
                Formel newFormel = ausaddieren(new Formel(formelStringLinks));
                formel.blockEinsetzen(newFormel, indexKlammer, indexKlammer + formelStringLinks.length()-1);
                klammer++;
                return ausaddieren(formel);
                // i = i + newFormel.length();
            } else if (c == '(') {
                klammer++;
                indexKlammer = i;
            } else if (c == ')') {
                klammer--;
                if (klammer == 0) {
// bin ich am Ende von meinem Block
                    innererBlock.add(block);
                    block = new char[0];

//Hier,an der Stelle
                    blockList.add(innererBlock);
                    // for(i=i;i+2 <= formel.length()&&formel.getChar(i)==')';i++) {
                    // klammer--;
                    if (i + 2 <= formel.length() && (formel.getChar(i + 1) == '+' || formel.getChar(i + 1) == '*')) {
                        blockList.add(new ArrayList<char[]>(Arrays.asList(new char[] { formel.getChar(i + 1) })));
                    }
                    // }

                    innererBlock = new ArrayList<>();
                }
            }
            // Formel Ende - Letzter Block von der Formel
            if (block.length > 0 && i + 1 == formel.length()) {
                innererBlock.add(block);
                block = new char[0];
                blockList.add(innererBlock);
                innererBlock = new ArrayList<>();
            }

        }

        List<char[]> ergebnisBloecke = new ArrayList<>();

        // nur wenn alles * ist
        boolean check = true;
        char[] sonderfall = new char[0];
        for (int k = 0; k < formel.length(); k++) {
            if (formel.getChar(k) == '+') {
                // sonderfall = new char[0];
                check = false;
                break;
            }
        }
        if (check) {
            for (int k = 0; k < blockList.size(); k++) {
                for (int h = 0; h < blockList.get(k).size(); h++) {
                    for (int u = 0; u < blockList.get(k).get(h).length; u++) {
                        if (blockList.get(k).get(h)[u] != '*') {
                            sonderfall = zeichenHinzufügen(sonderfall, blockList.get(k).get(h)[u]);
                        }

                    }

                }

            }
        }
        if (sonderfall.length != 0) {
            ergebnisBloecke.add(sonderfall);
        } else {

            if (blockList.size() == 1) {
                ergebnisBloecke.clear();
                ergebnisBloecke.addAll(blockList.get(0));
            }

            int b = 0;
            int einstieg = 0;
            while (b < blockList.size()) {
                // Hier haben wir innereBloecke abgelöst ------------------
// for (int i = 0; i < blockList.size(); i++) {
                innererBlock = null;
                if (b == 0) {
                    innererBlock = blockList.get(0);
                } else {
                    if (b + 1 < blockList.size()) {
                        innererBlock = blockList.get(b + 1);
                    } else {
                        break;
                    }
                }
// int originLength = ergebnisBloecke.size();
//if (originLength == 0) {
//for (int k = 0; k < innereBloecke.size(); k++) {
//ergebnisBloecke.add(innereBloecke.get(k));
//}
//}
                int bAlt = b;
                boolean keepGoing = true;
                int counter1 = innererBlock.size();
                int counter2 = 0;
                int counter2Alt = 0;
                for (b = b + 1; b < blockList.size() && keepGoing; b++) {
                    for (int l = 0; l < innererBlock.size(); l++) {
                        // Im Falle, wenn ein Mal kommt
                        if (counter1 == innererBlock.size()) {
                            counter2 = 0;
                        }

                        if (blockList.get(b).get(0)[0] == '*') {
                            if (b + 1 < blockList.size()
                                    && (1 < blockList.get(b + 1).size() || 1 < innererBlock.size())) {
                                for (int m = 0; m < (counter1 * blockList.get(b + 1).size() - counter2) / innererBlock.size(); m++) {

                                    // Clonen vom ersten Teil
                                    char[] chars = innererBlock.get(l).clone();
                                    ergebnisBloecke.add(chars);

                                }

                            }
                            // Wenn ein + kommt

                            // Oder wenn der Teil an Stelle b die Länge 1 hat und an der letzten Stelle
                            // steht und vor oder nach der Stelle b ein + kommt
                            /*
                             * 1. ich bin am Anfang der Formel und es kommt ein Plus an der nächsten Stelle
                             * | b=0 und b+1 = + 2. ich bin am Ende der Formel und direkt vor mir kommt ein
                             * Plus | b+1=blockList.get(b).size() und b-1 = + 3. ich bin irgendwo und direkt
                             * vor mir und nach mir kommt ein Plus
                             */
                        } else if (// blockList.get(b).size()==1 &&
                                ((b + 1 == blockList.size() && blockList.get(b - 1).get(0)[0] == '+')
                                        || (b == 1 && blockList.get(b).get(0)[0] == '+')
                                        || (b > 0 && b < blockList.size() && blockList.get(b - 1).get(0)[0] == '+'
                                        && blockList.get(b + 1).get(0)[0] == '+'))) {

                            if (blockList.get(b).get(0)[0] == '+') {
                                for (int m = 0; m < blockList.get(b - 1).size(); m++) {

                                    // Clonen vom ersten Teil
                                    char[] chars = innererBlock.get(m).clone();
                                    ergebnisBloecke.add(chars);
                                    einstieg++;
                                }
                            } else if (blockList.get(b).get(0)[0] != '+') {

                                // Clonen vom ersten Teil
                                char[] chars = innererBlock.get(l).clone();
                                ergebnisBloecke.add(chars);
                                einstieg++;

                            }

                            keepGoing = false;

                            // Wenn b ein Plus ist, soll es für die nachfolgende Logik um eins reduziert
                            // werden
                            if (blockList.get(b).get(0)[0] == '+') {
                                b--;
                            }

                        } else if (blockList.get(b).get(0)[0] == '+') {
                            keepGoing = false;
                            b--;
                        }

                    }
                    if (b + 1 < blockList.size() && blockList.get(b).get(0)[0] == '*') {
                        if (counter2Alt == 0) {
                            counter2 = blockList.get(b + 1).size() * innererBlock.size();
                            counter2Alt = blockList.get(b + 1).size() * innererBlock.size();
                        } else {
                            int temp = (counter1 * blockList.get(b + 1).size() - counter2);
                            counter2 = counter1 * blockList.get(b + 1).size() - counter2 + counter2Alt;
                            counter2Alt = temp;
                        }
                        counter1 = counter2;
                    }
                }

                /*
                 * for(int l = 0; l<blockList.size();l++) { for(int z = 0;
                 * z<blockList.get(1).size();z++) { ergebnisBloecke.add(innereBloecke.get(l)); }
                 * }
                 */

//if(i>0) {
                /*
                 * List<char[]> coypList = new ArrayList<>(ergebnisBloecke); for (int j = 0; j <
                 * innereBloecke.size()-1; j++) { for (int k = 0; k < coypList.size(); k++) {
                 * char[] chars = coypList.get(k).clone(); ergebnisBloecke.add(chars); } }
                 */
//}
                if (bAlt == 0) {
                    bAlt = -1;
                }

                int blockCounter = 0;
                int eintrag = ergebnisBloecke.size() - einstieg;
                for (int n = bAlt + 3; n < b; n++) {

                    int counter = 0;

                    innererBlock = blockList.get(n);
                    if (innererBlock.get(0)[0] == '*' || innererBlock.get(0)[0] == '+') {
                        continue;
                    }
                    blockCounter++;
                    /*
                     * if(bAlt==-1) { einstieg=0; }
                     */

                    int bereitsEingetrag = 0;
                    eintrag = eintrag / innererBlock.size();
                    for (int j = einstieg; j < ergebnisBloecke.size(); j++) {

                        if (counter == innererBlock.size()) {
                            counter = 0;
                        }
                        char[] neuerBlock = new char[ergebnisBloecke.get(j).length + innererBlock.get(counter).length];
                        int counterNeuerBlock = 0;
                        for (int l = 0; l < ergebnisBloecke.get(j).length; l++) {
                            neuerBlock[l] = ergebnisBloecke.get(j)[l];
                            counterNeuerBlock++;
                        }

                        // if((ergebnisBloecke.size()%blockCounter)%2==1) {
                        if (n < b - 1) {

                            if (bereitsEingetrag < eintrag) {
                                for (int l = 0; l < innererBlock.get(counter).length; l++) {
                                    neuerBlock[counterNeuerBlock + l] = innererBlock.get(counter)[l];
                                }
                                bereitsEingetrag++;
                            }
                            if (bereitsEingetrag == eintrag) {
                                bereitsEingetrag = 0;
                                counter++;
                            }

                            // Ungearade
                            // }else if((ergebnisBloecke.size()%blockCounter)%2==0) {
                        } else if (n == b - 1) {
                            for (int l = 0; l < innererBlock.get(counter).length; l++) {
                                neuerBlock[counterNeuerBlock + l] = innererBlock.get(counter)[l];
                            }
                            counter++;
                        }
                        ergebnisBloecke.set(j, neuerBlock);

                    }

                    /*
                     * for (int j = 0; j < innereBloecke.size(); j++) { for (int k = 0; k <
                     * originLength; k++) { char[] neuerBlock = new char[ergebnisBloecke.get(j +
                     * k).length + innereBloecke.get(j).length]; int counterNeuerBlock = 0; for (int
                     * l = 0; l < ergebnisBloecke.get(j + k).length; l++) { neuerBlock[l] =
                     * ergebnisBloecke.get(j + k)[l]; counterNeuerBlock++; } for (int l = 0; l <
                     * innereBloecke.get(j).length; l++) { neuerBlock[counterNeuerBlock + l] =
                     * innereBloecke.get(j)[l]; } ergebnisBloecke.set(j + k, neuerBlock); } }
                     */
                }
            }
        }
        // }
        String loesung = "";
        for (int i = 0; i < ergebnisBloecke.size(); i++) {
            loesung = loesung + "(";
            for (int j = 0; j < ergebnisBloecke.get(i).length; j++) {
                loesung = loesung + ergebnisBloecke.get(i)[j];
                if(ergebnisBloecke.get(i)[j]!='n') {
                    if (j < ergebnisBloecke.get(i).length - 1) {
                        loesung = loesung + "*";
                    }
                }
            }
            loesung = loesung + ")";
            if (i < ergebnisBloecke.size() - 1) {
                loesung = loesung + "+";
            }
        }
        Formel loesungsFormel = new Formel(loesung);

        return loesungsFormel;

    }

    private static char[] zeichenHinzufügen(char[] origin, char z) {
        char[] newZeichenSatz = new char[origin.length + 1];
        for (int i = 0; i < origin.length; i++) {
            newZeichenSatz[i] = origin[i];
        }
        newZeichenSatz[origin.length] = z;
        return newZeichenSatz;
    }





    /* Zwei weitere Male musste ich Kommentare setzten, um alles auszukommentieren
    public static Formel ausaddieren(Formel formel) {
        boolean keineKlammmerGefunden = true;
        for (int i = 0; keineKlammmerGefunden&&i<formel.length()-1; i++) {
            // Es zählen immer nur + Zeichen in Verbindung mit Klammern, sonst nix
            if((formel.getChar(i)=='+'&&formel.getChar(i+1)=='(')||(formel.getChar(i)=='+'&&formel.getChar(i-1)==')')) {
                keineKlammmerGefunden = false;
                Formel ersterTeil = new Formel(i+1);
                Formel zweiterTeil = new Formel(formel.length()-1-i);
                boolean vordererTeilInKNF = true;
                for (int j = i; j >= 0; j--) {
                    ersterTeil.setChar(j, formel.getChar(j));
                    if(j>=1){
                        if(vordererTeilInKNF&&(
                                (Character.toString(formel.getChar(j)).matches("[a-n]") && (formel.getChar(j-1) == '+' || formel.getChar(j -1) == '(')) //Auf ein Buchstabe darf nur ein + oder )
                                        ||(Character.toString(formel.getChar(j)).matches("[n]")&&Character.toString(formel.getChar(j-1)).matches("[a-n]"))
                                        || (formel.getChar(j) == '+' && Character.toString(formel.getChar(j-1)).matches("[a-n]")) //Auf ein + nur ein Buchstabe
                                        || (formel.getChar(j) == '*' && formel.getChar(j-1) == ')') // auf ein * nur eine (
                                        || (formel.getChar(j) == '(' && (formel.getChar(j-1) == '*' || formel.getChar(j-1) == '(')) // auf ein ) nur ein *
                                        || (formel.getChar(j) == ')' && (Character.toString(formel.getChar(j-1)).matches("[a-n]") || formel.getChar(j-1) == ')'))
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
                for(int j = 0; j < formel.length()-i-1; j++) {
                    zweiterTeil.setChar(j, formel.getChar(j+i+1));
                    if (j<formel.length()-i-2) {
                        if (hintererTeilInKNF&&(
                                (Character.toString(formel.getChar(j + i + 1)).matches("[a-n]") && (formel.getChar(j + i + 2) == '+' || formel.getChar(j + i + 2) == ')')) //Auf ein Buchstabe darf nur ein + oder )
                                        ||(Character.toString(formel.getChar(j + i + 1)).matches("[n]")&&Character.toString(formel.getChar(j + i + 2)).matches("[a-n]"))
                                        || (formel.getChar(j + i + 1) == '+' && Character.toString(formel.getChar(j + i + 2)).matches("[a-n]")) //Auf ein + nur ein Buchstabe
                                        || (formel.getChar(j + i + 1) == '*' && formel.getChar(j + i + 2) == '(') // auf ein * nur eine (
                                        || (formel.getChar(j + i + 1) == ')' && (formel.getChar(j + i + 2) == '*' || formel.getChar(j + i + 2) == ')')) // auf ein ) nur ein *
                                        || (formel.getChar(j + i + 1) == '(' && (Character.toString(formel.getChar(j + i + 2)).matches("[a-n]") || formel.getChar(j + i + 2) == '('))
                        )&&formel.length()-i-1>1) { // auf ein ( nur ein Buchstabe folgen
                            hintererTeilInKNF = true;
                        } else {
                            hintererTeilInKNF = false;
                        }
                    }
                }
                Formel neuerHinterTeil;

                if(weitereAufloesungNotwendig(zweiterTeil)) {
                    neuerHinterTeil = ausaddieren(zweiterTeil);
                    hintererTeilInKNF = true;
                }else {
                    neuerHinterTeil = new Formel(zweiterTeil.getFormel());
                }
                List<Formel> listVordereElemente = new ArrayList<Formel>();
                List<Formel> listHintereElemente = new ArrayList<Formel>();
                Formel zeichenErsterBlock = new Formel();
                Formel zeichenZweiterBlock = new Formel();

                /*
                Vordere Block
                 */
    /*
                boolean inBlock=true;
                int j = ersterTeil.length()-2;
                int klammern = 0;
                int index = 0;
                Formel vordererKNFTeil = new Formel();
                while(inBlock) {
                    if(j == -1  // Am Ende der Reihe
                            || j>=0 // Oder noch nicht am Ende der Reihe
                            &&((ersterTeil.getChar(j)=='*'&&Character.toString(ersterTeil.getChar(j)).matches("[a-n]"))// Und 'Und' verkn�pfte Buchstaben
                            ||(klammern==0&&ersterTeil.getChar(j)=='+')
                            ||(klammern==1&&ersterTeil.getChar(j)=='('))) { // Oder es wurde bereits eine Klammer ge�ffnet und es ist eine öffnende Klammer
                        listVordereElemente.add(zeichenErsterBlock);
                        inBlock=false;

                        for (int k = 0; k < j+1; k++){
                            vordererKNFTeil.zeichenHinzufügen(ersterTeil.getChar(k));
                        }
                        break;
                    }
                    if(Character.toString(ersterTeil.getChar(j)).matches("[a-e]")) {
                        zeichenErsterBlock.zeichenHinzufügen(ersterTeil.getChar(j));
                        index++;
                    }else if(ersterTeil.getChar(j)=='n'){
                        zeichenErsterBlock.zeichenHinzufügen(ersterTeil.getChar(j+1));
                        zeichenErsterBlock.setChar(index-1, 'n');
                        index++;
                    }else if(ersterTeil.getChar(j)==')') {
                        klammern--;
                    }else if(ersterTeil.getChar(j)=='(') {
                        klammern++;
                    }else if(klammern==0&&ersterTeil.getChar(j)=='+') {
                        listVordereElemente.add(zeichenErsterBlock);
                        zeichenErsterBlock=new Formel();
                    }else if((ersterTeil.getChar(j)=='*'&&ersterTeil.getChar(j+1)=='('&&ersterTeil.getChar(j-1)==')')
                            ||(Character.toString(ersterTeil.getChar(j-1)).matches("[a-e]")&&ersterTeil.getChar(j)=='*'&&ersterTeil.getChar(j+1)=='(')
                            ||(Character.toString(ersterTeil.getChar(j+1)).matches("[a-e]")&&ersterTeil.getChar(j)=='*'&&ersterTeil.getChar(j-1)==')')){
                        listVordereElemente.add(zeichenErsterBlock);
                        zeichenErsterBlock=new Formel();
                    }
                    j--;
                }

                /*
                Hintere Block
                 */
    /*
                int q = 0;
                int klammernHintererBlock = 0+klammern;
                boolean inHinteremBlock =true;
                Formel hintererKNFTeil = new Formel();
                while(inHinteremBlock) { //||(klammernHintererBlock==0&&neuerHinterTeil[q]=='+')
                    if(q==neuerHinterTeil.length()
                            || q<neuerHinterTeil.length()
                            && ((neuerHinterTeil.getChar(q)=='*'&&Character.toString(neuerHinterTeil.getChar(q)).matches("[a-n]")&&!hintererTeilInKNF)
                            ||(klammernHintererBlock==0&&neuerHinterTeil.getChar(q)==')')
                            ||(neuerHinterTeil.getChar(q)=='*'&&neuerHinterTeil.getChar(q+1)=='('&&klammernHintererBlock==0&&!hintererTeilInKNF))) {
                        listHintereElemente.add(zeichenZweiterBlock);
                        inBlock=false;
                        //Ein Teil musste aufgelöst werden, der Rest ist in KNF
                        if(!hintererTeilInKNF&&!weitereAufloesungNotwendig(neuerHinterTeil)){
                            for(int k = q; k< neuerHinterTeil.length(); k++){
                                hintererKNFTeil.zeichenHinzufügen(neuerHinterTeil.getChar(k));
                            }
                        }
                        break;
                    }
                    if(Character.toString(neuerHinterTeil.getChar(q)).matches("[a-n]")) {
                        zeichenZweiterBlock.zeichenHinzufügen(neuerHinterTeil.getChar(q));
                    }else if(neuerHinterTeil.getChar(q)=='('){
                        klammernHintererBlock++;
                    }else if(neuerHinterTeil.getChar(q)==')') {
                        klammernHintererBlock--;
                    }else if((neuerHinterTeil.getChar(q)=='*'&&(neuerHinterTeil.getChar(q-1)==')'&&neuerHinterTeil.getChar(q+1)=='(')
                            ||(Character.toString(neuerHinterTeil.getChar(q-1)).matches("[a-e]")&&neuerHinterTeil.getChar(q)=='*'&&neuerHinterTeil.getChar(q+1)==')')
                            ||(Character.toString(neuerHinterTeil.getChar(q+1)).matches("[a-e]")&&neuerHinterTeil.getChar(q)=='*'&&neuerHinterTeil.getChar(q-1)=='(')
                            ||(neuerHinterTeil.getChar(q)=='*'&&Character.toString(neuerHinterTeil.getChar(q+1)).matches("[a-e]")&&Character.toString(neuerHinterTeil.getChar(q*1)).matches("[a-e]")))) {
                        listHintereElemente.add(zeichenZweiterBlock);
                        zeichenZweiterBlock=new Formel();
                    }
                    q++;
                }

                List<Formel> resultCharSet = new ArrayList<Formel>();
                if(vordererTeilInKNF){
                    for(int w = 0; w<listHintereElemente.size(); w++){
                        for(int e = 0; e<listHintereElemente.get(w).length(); e++){
                            for(int r = 0; r<listVordereElemente.size(); r++){
                                Formel c = new Formel(1);
                                c.setChar(0, '(');
                                for(int t = 0; t<listVordereElemente.get(r).length(); t++){
                                    if(listVordereElemente.get(r).getChar(t)=='n'){
                                        c.zeichenHinzufügen(listVordereElemente.get(r).getChar(t));
                                        t++;
                                        c.zeichenHinzufügen(listVordereElemente.get(r).getChar(t));
                                    }else {
                                        c.zeichenHinzufügen(listVordereElemente.get(r).getChar(t));
                                    }
                                    if(t<listVordereElemente.get(r).length()-1){
                                        c.zeichenHinzufügen('+');
                                    }
                                }
                                if(!existsChar(c, listHintereElemente.get(w).getChar(e))){
                                    c.zeichenHinzufügen('+');
                                    if(listHintereElemente.get(w).getChar(e)=='n'){
                                        c.zeichenHinzufügen(listHintereElemente.get(w).getChar(e));
                                        e++;
                                        c.zeichenHinzufügen(listHintereElemente.get(w).getChar(e));
                                    }else {
                                        c.zeichenHinzufügen(listHintereElemente.get(w).getChar(e));
                                    }
                                }
                                c.zeichenHinzufügen(')');
                                if(blockHinzufügen(resultCharSet, c)) {
                                    resultCharSet.add(c);
                                }
                            }
                        }
                    }
                }else if(hintererTeilInKNF){
                    for(int w = 0; w<listVordereElemente.size(); w++) {
                        for(int e = 0; e<listVordereElemente.get(w).length(); e++) {
                            boolean vordereElementNegiert = false;
                            for(int r = 0; r<listHintereElemente.size(); r++) {
                                Formel c = new Formel(1);
                                c.setChar(0, '(');
                                for(int t = 0; t<listHintereElemente.get(r).length(); t++) {
                                    if(listHintereElemente.get(r).getChar(t)=='n'){
                                        c.zeichenHinzufügen(listHintereElemente.get(r).getChar(t));
                                        t++;
                                        c.zeichenHinzufügen(listHintereElemente.get(r).getChar(t));
                                    }else {
                                        c.zeichenHinzufügen(listHintereElemente.get(r).getChar(t));
                                    }
                                    if(t<listHintereElemente.get(r).length()-1) {
                                        c.zeichenHinzufügen('+');
                                    }
                                }
                                if(!existsChar(c, listVordereElemente.get(w).getChar(e))) {
                                    c.zeichenHinzufügen('+');

                                    if(listVordereElemente.get(w).getChar(e)=='n'){
                                        c.zeichenHinzufügen(listVordereElemente.get(w).getChar(e));
                                        e++;
                                        c.zeichenHinzufügen(listVordereElemente.get(w).getChar(e));
                                        e--;
                                        vordereElementNegiert = true;
                                    }else {
                                        c.zeichenHinzufügen(listVordereElemente.get(w).getChar(e));
                                    }
                                }
                                c.zeichenHinzufügen(')');
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
                        for(int e = 0; e<listVordereElemente.get(w).length(); e++) {
                            boolean hintereCharNegiert = false;
                            for(int r = 0; r<listHintereElemente.size(); r++) {
                                for(int t = 0; t<listHintereElemente.get(r).length(); t++) {
                                    Formel c = new Formel(1);
                                    c.setChar(0,'(');
                                    if(listVordereElemente.get(w).getChar(e)=='n'){
                                        c.zeichenHinzufügen(listVordereElemente.get(w).getChar(e));
                                        e++;
                                        c.zeichenHinzufügen(listVordereElemente.get(w).getChar(e));
                                        e--;
                                        hintereCharNegiert = true;
                                    }else {
                                        c.zeichenHinzufügen(listVordereElemente.get(w).getChar(e));
                                    }
                                    c.zeichenHinzufügen('+');
                                    if(listHintereElemente.get(r).getChar(t)=='n') {
                                        c.zeichenHinzufügen(listHintereElemente.get(r).getChar(t));
                                        t++;
                                        c.zeichenHinzufügen(listHintereElemente.get(r).getChar(t));
                                    }else{
                                        c.zeichenHinzufügen(listHintereElemente.get(r).getChar(t));
                                    }
                                    c.zeichenHinzufügen(')');
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
                Formel finalResult = new Formel();
                for(int l = 0; l< vordererKNFTeil.length(); l++) {
                    finalResult.zeichenHinzufügen(vordererKNFTeil.getChar(l));
                }
                finalResult.zeichenHinzufügen('(');
                for (int k = 0; k < resultCharSet.size(); k++) {
                    for(int l = 0; l < resultCharSet.get(k).length();l++) {
                        finalResult.zeichenHinzufügen(resultCharSet.get(k).getChar(l));
                    }
                    if(k < resultCharSet.size()-1) {
                        finalResult.zeichenHinzufügen('*');
                    }
                }
                finalResult.zeichenHinzufügen(')');
                for(int l = 0; l< hintererKNFTeil.length(); l++) {
                    finalResult.zeichenHinzufügen(hintererKNFTeil.getChar(l));
                }
                Formel rekursiveAuflösung = ausaddieren(finalResult);
                Formel ohneKlammern = new Formel();
                if(vordererKNFTeil.length()==0&&hintererKNFTeil.length()==0) {
                    for (int l = 1; l < rekursiveAuflösung.length() - 1; l++) {
                        ohneKlammern.zeichenHinzufügen(rekursiveAuflösung.getChar(l));
                    }
                }else{
                    for (int l = 0; l < rekursiveAuflösung.length(); l++) {
                        ohneKlammern.zeichenHinzufügen(rekursiveAuflösung.getChar(l));
                    }
                }
                return ohneKlammern;
            }
        }
        return formel;
    }*/
}
