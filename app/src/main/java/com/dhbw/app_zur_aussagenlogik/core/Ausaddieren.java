package com.dhbw.app_zur_aussagenlogik.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Die Klasse <b>Ausaddieren</b> ist zum ausaddieren der übergebenen Formel. Also es
 * geht hier um die Bildung der KNF.
 *
 * Diese Klasse ist genauso aufgebaut wie die Klasse "Ausmultiplizieren". Es wurden nur die "* Zeichen"
 * mit den "+ Zeichen" vertauscht. Ansonsten ist die Logik die selbe.
 *
 * @author Nico Erzberger, Daniel Miller
 * @version 1.0
 */

public class Ausaddieren{

    /**
     * Dies ist die Hauptmethode der Klasse "Ausaddieren". Alles andere sind Hilfsmethoden.
     * Genaues funktionieren steht an den Codestellen selbst.
     * Diese Mehtode ruft sich rekursiv auf.
     *
     * @param formel übergebene Formel
     * @return Formel in KNF-Schreibweise
     */
    public static Formel ausaddieren(Formel formel) {

        /*
         * Die Liste "blockList" besteht aus weiteren Listen (Variable: innererBlock), welche
         * widerum aus char[] (Variable: block) bestehen.
         * In "block" stehen Variablen, welche ODER-verknüpft sind.
         * In "innererBlock" stehen Variablen, UND-verknüpft sind.
         * In "blockList" stehen die innerenBlöcke und nach jedem innerenBlock steht ein Operator
         * (UND/ODER), welche die innerenBlöcke verbindet.
         */
        List<List<char[]>> blockList = new ArrayList<>();
        List<char[]> innererBlock = new ArrayList<>();
        char[] block = new char[0];

        /*
         * In "klammern" zählen wir unsere Klammern. Offene Klammer zählen wir hoch und
         * bei geschlossenen Klammern zählen wir runter. Wenn die Klammer == 0 ist, wird dieser Teil
         * dem innerenBlock hinzugefügt.
         */
        int klammer = 0;

        /*
        indexKlammer steht immer auf dem Index unserer Formel, wo unsere letzte öffnende Klammer ist.
        Dies brauchen wir später, um einen Block wieder an die richtige Stelle in der Formel einzufügen.
         */
        int indexKlammer = 0;

        /*
        Hier gehen wir durch die ganze übergebene Formel und bauen unsere Struktur in der Liste
        "blockList" auf.
        In der Formel können rekursive Aufrufe stattfinden.
         */
        for (int i = 0; i < formel.length(); i++) {

            char c = formel.getChar(i);

            //Wenn wir einen Buchstaben haben, schreib ihn in den Block.
            if (Character.toString(c).matches("[a-z]")) {
                block = zeichenHinzufügen(block, c);

                // "c" ist ein + und die Zeichen davor und danach sind Buchstaben
                //Also schreibe + nicht, da + schon durch die Verbindung in "block" dargestellt wird
            } else if (c == '+' && Character.toString(formel.getChar(i - 1)).matches("[a-z]")
                    && Character.toString(formel.getChar(i + 1)).matches("[a-z]")) {
                continue;

                // "c" ist ein *
            } else if (c == '*' && block.length > 0) {
                innererBlock.add(block);
                block = new char[0];
                if (klammer == 0) {
                    //Füge "innerBlock" der "blockList" hinzu und danach ein *
                    blockList.add(innererBlock);
                    blockList.add(new ArrayList<char[]>(Arrays.asList(new char[] { '*' })));
                    innererBlock = new ArrayList<>();
                }
                //"c" ist ein +, wir haben keine Klammern und das Zeichen davor oder danach ist
                //kein Buchstabe.
            } else if (block.length > 0 && klammer == 0 && c == '+'
                    && (!Character.toString(formel.getChar(i - 1)).matches("[a-z]")
                    || !Character.toString(formel.getChar(i + 1)).matches("[a-z]"))) {
                innererBlock.add(block);
                block = new char[0];

                //Füge "innerBlock" der "blockList" hinzu und danach ein +
                blockList.add(innererBlock);
                blockList.add(new ArrayList<char[]>(Arrays.asList(new char[] { '+' })));
                innererBlock = new ArrayList<>();

                /*
                 * Rekursion rechts
                 * Da Klammer größer 0 kommt und auf das * eine weitere Klammer folgt, wird nun
                 * eine Rekursion folgen.
                 */
            } else if (klammer > 0 && c == '+' && !Character.toString(formel.getChar(i + 1)).matches("[a-z]")) {
                // Neue Formel bilden
                String formelString = "";

				/*
                Wir brauchen nun den linken und den rechten Teil von unserem +.
                Hier bauen wir den linken Teil auf. Das heißt, wir gehen von unserem + nach links
                durch die Formel.
                 */
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
                    else if (formelChar == '*' && klammernLinks==0) {
                        break;
                    }
                    formelString = formelString + formelChar;
                    counterLinks++;
                }
                //Der String muss umgedreht werden, da wir ihn falschherum einlesen.
                formelString = new StringBuilder(formelString).reverse().toString();

                /*
                Wir brauchen nun den linken und den rechten Teil von unserem +.
                Hier bauen wir den rechten Teil auf. Das heißt, wir gehen von unserem + nach rechts
                durch die Formel.
                 */
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

                //Rekursiveraufruf
                Formel newFormel = ausaddieren(new Formel(formelString));

                //Die ausmultiplizierte Teilformel ("newFormel") wird nun an die richtige Stelle in
                //unsere originalen Formel eingesetzt.
                formel.blockEinsetzen(newFormel, i - counterLinks + 1, i - 1 + counterRechts);
                klammer++;

                //Grund für den zweiten rekursiven Aufruf ist, dass unsere größeren Variablen wieder
                //geresetet (denglish) werden müssen und dies die einfachst Methode dafür ist.
                return ausaddieren(formel);


                /*
                 * Linke Rekursion
                 * Da Klammer größer 0 kommt und vor dem * eine weitere Klammer steht, wird nun
                 * eine Rekursion folgen.
                 */
            } else if (klammer > 0 && c == '+' && !Character.toString(formel.getChar(i - 1)).matches("[a-z]")) {
                // Neue Formel bilden
                //Man könnte auch nur einen String wie oben verwenden, doch dann muss man die zweite
                //While zuerst machen.
                String formelStringRechts = "";
                String formelStringLinks = "";

                /*
                Wir brauchen nun den linken und den rechten Teil von unserem +.
                Hier bauen wir den rechten Teil auf. Das heißt, wir gehen von unserem + nach rechts
                durch die Formel.
                 */
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
                    else if (formelChar == '*'&&klammernRechts==0) {
                        break;
                    }
                    formelStringRechts = formelStringRechts + formelChar;
                    counterRechts++;
                }

                /*
                Hier bauen wir den linken Teil auf. Das heißt, wir gehen von unserem + nach linken
                durch die Formel.
                 */
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

                //Der String muss umgedreht werden, da wir ihn falschherum einlesen.
                formelStringLinks = new StringBuilder(formelStringLinks).reverse().toString();

                //Zusammenbauen des linken und rechten Teils
                formelStringLinks = formelStringLinks + formelStringRechts;

                //Rekursiveraufruf
                Formel newFormel = ausaddieren(new Formel(formelStringLinks));

                //Die ausmultiplizierte Teilformel ("newFormel") wird nun an die richtige Stelle in
                //unsere originalen Formel eingesetzt.
                formel.blockEinsetzen(newFormel, indexKlammer, indexKlammer + formelStringLinks.length()-1);
                klammer++;

                //Grund für den zweiten rekursiven Aufruf ist, dass unsere größeren Variablen wieder
                //geresetet (denglish) werden müssen und dies die einfachst Methode dafür ist.
                return ausaddieren(formel);

            } else if (c == '(') {
                klammer++;
                //"indexKlammer" damit wir später wissen, wo wir einen rekursiven Block einfügen müssen.
                indexKlammer = i;
            } else if (c == ')') {
                klammer--;
                //Wenn Klammer == 0 und "c" == ')' sind wir am Ende von "block"
                if (klammer == 0) {
                    innererBlock.add(block);
                    block = new char[0];
                    blockList.add(innererBlock);

                    //Da wir einen innerenBlock der "blockList" zugefügt haben, muss als nächstes ein
                    //* oder ein + in die "blockList" eingefügt werden.
                    if (i + 2 <= formel.length() && (formel.getChar(i + 1) == '*' || formel.getChar(i + 1) == '+')) {
                        blockList.add(new ArrayList<char[]>(Arrays.asList(new char[] { formel.getChar(i + 1) })));
                    }

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

        /*
        ---------------------------------------------
        Unsere Struktur in "blockList" ist nun fertig aufgebaut und kann ausaddiert werden.
        ---------------------------------------------
         */


        //In "ergebnisBloecke" wird nach und nach unsere ausaddierte Lösung eingefügt.
        //Das Ausaddieren folgt nun.
        List<char[]> ergebnisBloecke = new ArrayList<>();

        //Der Check ist eingebaut, falls die Formel nur aus * Zeichen besteht. Dann geht das
        //ausaddieren schneller und die untere IF zieht.
        //In "sonderfall" wird dann schnell die Formel aufgebaut.
        boolean check = true;
        char[] sonderfall = new char[0];
        for (int k = 0; k < formel.length(); k++) {
            if (formel.getChar(k) == '*') {
                check = false;
                break;
            }
        }
        if (check) {
            for (int k = 0; k < blockList.size(); k++) {
                for (int h = 0; h < blockList.get(k).size(); h++) {
                    for (int u = 0; u < blockList.get(k).get(h).length; u++) {
                        if (blockList.get(k).get(h)[u] != '+') {
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

            //"b" ist der Index, mit welchem wir durch "blockList" iterieren.
            //"einstieg" wird später benötigt, damit der Einstieg in ergebnisBloecke gefunden wird,
            //wo angefangen werden muss Variablen einzufügen. Die Stellen vor "einstieg" sind nämlich
            //schon fertig ausgefüllt.
            int b = 0;
            int einstieg = 0;
            while (b < blockList.size()) {
                innererBlock = null;
                //Wenn wir an der ersten Stelle sind, soll "innererBlock" die erste Stelle aus "blockList" sein.
                if (b == 0) {
                    innererBlock = blockList.get(0);
                } else {
                    if (b + 1 < blockList.size()) {
                        innererBlock = blockList.get(b + 1);
                    } else {
                        break;
                    }
                }

                /*
                In der folgenden For-Schleife schauen wir auf unseren nächsten innerenBlock, um zu
                wissen, wie häufig wir die Variablen in unserem aktuellen innerenBlock duplizieren müssen.
                In dieser Schleife wird auch die erste Hälfte in die ergebnisBloecke geschrieben. Die
                zweite Hälfte geschieht weiter unten.
                "counter2" sind die bisher geschriebenen Einträge.
                "counter1" ist dasselbe wie "counter2", außer im ersten Durchlauf, muss dieser die
                Anzahl der Variablen im innerenBlock haben.
                "counter2Alt" merkt sich die die bisher geschriebenen Einträge eins vor diesem Durchlauf.
                "counterGemachteEintraege" zählt, wie viele Einträge wir in diesem Durchlauf machen.
                 */
                int bAlt = b;
                boolean keepGoing = true;
                int counter1 = innererBlock.size();
                int counter2 = 0;
                int counter2Alt = 0;
                int counterGemachteEintraege = 0;
                for (b = b + 1; b < blockList.size() && keepGoing; b++) {

                    //Hier wird durch den aktuellen innerenBlock iteriert und die Variablen darin dupliziert.
                    for (int l = 0; l < innererBlock.size(); l++) {

                        //Im ersten Durchlauf muss counter2 immer 0 sein.
                        if (counter1 == innererBlock.size()) {
                            counter2 = 0;
                        }

                        //Wenn unserer innererBlock ein * ist und wir danach noch einen Block haben
                        if (blockList.get(b).get(0)[0] == '+') {
                            if (b + 1 < blockList.size()
                                    && (1 < blockList.get(b + 1).size() || 1 < innererBlock.size())) {

                                /*
                                Bedingung:
                                - counter1 = Wie viele Variablen haben wir im aktuellen innerenBlock, welche
                                           mit einem * verbunden sind
                                - Mal die Anzahl der Variablen des nächsten innerenBlock, welche mit einem
                                * verbunden sind.
                                - Minus counter2.
                                - Dividiert durch Anzahl der Variablen des aktuellen innerenBlock, welche
                                mit einem * verbunden sind.
                                --> Dies ergibt, wie häufig wir die Variablen in die Ergebnisliste duplizieren müssen.
                                 */
                                for (int m = 0; m < (counter1 * blockList.get(b + 1).size() - counter2) / innererBlock.size(); m++) {

                                    // Clonen vom ersten Teil
                                    char[] chars = innererBlock.get(l).clone();
                                    ergebnisBloecke.add(chars);
                                    counterGemachteEintraege++;
                                }

                            }

                            /*
                             * 1. ich bin am Ende der Formel und direkt vor mir kommt ein * | b+1=blockList.get(b).size()
                             * und b-1 = *
                             * 2. ich bin am Anfang der Formel und es kommt ein * an der nächsten Stelle
                             * | b=0 und b+1 = *
                             * 3. ich bin irgendwo und direkt vor mir und nach mir kommt ein *
                             */
                        } else if (// blockList.get(b).size()==1 &&
                                ((b + 1 == blockList.size() && blockList.get(b - 1).get(0)[0] == '*')
                                        || (b == 1 && blockList.get(b).get(0)[0] == '*')
                                        || (b > 0 && b < blockList.size() && blockList.get(b - 1).get(0)[0] == '*'
                                        && blockList.get(b + 1).get(0)[0] == '*'))) {

                            //Wenn unserer aktueller innererBlock ein * ist, müssen wir die Variablen
                            //aus dem innerenBlock davor jeweils nur einmal in die ergebnisBloeacke schreiben,
                            //da sie mit nichts ausaddiert werden müssen.
                            if (blockList.get(b).get(0)[0] == '*') {
                                for (int m = 0; m < blockList.get(b - 1).size(); m++) {

                                    // Clonen vom ersten Teil
                                    char[] chars = innererBlock.get(m).clone();
                                    ergebnisBloecke.add(chars);
                                    einstieg++;
                                    counterGemachteEintraege++;
                                }
                            } else if (blockList.get(b).get(0)[0] != '*') {

                                // Clonen vom ersten Teil
                                char[] chars = innererBlock.get(l).clone();
                                ergebnisBloecke.add(chars);
                                einstieg++;
                                counterGemachteEintraege++;
                            }

                            keepGoing = false;

                            // Wenn b ein * ist, soll es für die nachfolgende Logik um eins reduziert
                            // werden
                            if (blockList.get(b).get(0)[0] == '*') {
                                b--;
                            }

                        } else if (blockList.get(b).get(0)[0] == '*') {
                            keepGoing = false;
                            b--;
                        }

                    }

                    /*
                    "counter2" sind die bisher geschriebenen Einträge.
                    "counter1" ist dasselbe wie "counter2", außer im ersten Durchlauf, muss dieser die
                    Anzahl der Variablen im innerenBlock haben.
                    "counter2Alt" merkt sich die die bisher geschriebenen Einträge eins vor diesem Durchlauf.
                    */
                    if (b + 1 < blockList.size() && blockList.get(b).get(0)[0] == '+') {
                        //Für den ersten Durchlauf
                        if (counter2Alt == 0) {
                            counter2 = blockList.get(b + 1).size() * innererBlock.size();
                            counter2Alt = blockList.get(b + 1).size() * innererBlock.size();
                        }//Für alle anderen Durchläufe
                        else {
                            int temp = (counter1 * blockList.get(b + 1).size() - counter2);
                            counter2 = counter1 * blockList.get(b + 1).size() - counter2 + counter2Alt;
                            counter2Alt = temp;
                        }
                        counter1 = counter2;
                    }
                }

                //Hier werden die neu in ergebnnisBloecke geschriebenen Variablen neu in ergebnisBloecke
                //eingesetzt  und zwar immer abwechselnd und die alte Reihenfolge wird somit ersetzt,
                //damit sich im folgenden Verlauf die Formel richtig aufbaut
                int beginn = ergebnisBloecke.size() - counterGemachteEintraege;
                for (int i = beginn; i < ergebnisBloecke.size(); i++) {
                    for (int j = 0; j < innererBlock.size(); j++) {
                        ergebnisBloecke.set(i,innererBlock.get(j));
                        if(j<innererBlock.size()-1){
                            i++;
                        }
                    }
                }


                if (bAlt == 0) {
                    bAlt = -1;
                }

                /*
                Die erste Hälfte von ergebnisBloecke ist fertig.
                In dieser Schleife wird die zweite Hälfte in die ergebnisBloecke geschrieben.
                 */
                int blockCounter = 0;
                int eintrag = ergebnisBloecke.size() - einstieg;
                for (int n = bAlt + 3; n < b; n++) {

                    int counter = 0;

                    innererBlock = blockList.get(n);
                    if (innererBlock.get(0)[0] == '+' || innererBlock.get(0)[0] == '*') {
                        continue;
                    }
                    blockCounter++;

                    /*
                    Dadurch, dass "eintrag" immer kleiner wird, entsteht ein Muster, wodurch die
                    Lösungsmengen sich nicht doppeln.
                    "einsteig" ist der Punkt, wo angefangen werden muss die Variablen einzufügen.
                    In der FOR-Schleife wird von "einsteig" bis "ergebnisBloecke"-Ende Variablen eingefügt.
                    */
                    int bereitsEingetrag = 0;
                    eintrag = eintrag / innererBlock.size();
                    for (int j = einstieg; j < ergebnisBloecke.size(); j++) {

                        if (counter == innererBlock.size()) {
                            counter = 0;
                        }

                        //Hier wird berechnet, wie groß der neue Block zum Einfügen in ergebnisBloecke sein muss
                        char[] neuerBlock = new char[ergebnisBloecke.get(j).length + innererBlock.get(counter).length];
                        int counterNeuerBlock = 0;
                        //Hier werden die bisherigen Variablen in "neuerBlock" eingefügt
                        for (int l = 0; l < ergebnisBloecke.get(j).length; l++) {
                            neuerBlock[l] = ergebnisBloecke.get(j)[l];
                            counterNeuerBlock++;
                        }

                        //Hier werden die neuen Variablen in "neuerBlock" eingefügt
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

                        //Hier wird der fertige "neuerBlock" zu ergebnisBloecke hinzugefügt.
                        ergebnisBloecke.set(j, neuerBlock);

                    }
                }
            }
        }

        //Hier wird ergebnisBloecke in einen String geschrieben, welcher danach in eine Instanz von
        //Formel geschrieben und ausgegeben wird.
        String loesung = "";
        for (int i = 0; i < ergebnisBloecke.size(); i++) {
            loesung = loesung + "(";
            for (int j = 0; j < ergebnisBloecke.get(i).length; j++) {
                loesung = loesung + ergebnisBloecke.get(i)[j];
                if(ergebnisBloecke.get(i)[j]!='n') {
                    if (j < ergebnisBloecke.get(i).length - 1) {
                        loesung = loesung + "+";
                    }
                }
            }
            loesung = loesung + ")";
            if (i < ergebnisBloecke.size() - 1) {
                loesung = loesung + "*";
            }
        }
        Formel loesungsFormel = new Formel(loesung);

        return loesungsFormel;

    }

    /**
     * Mit dieser Methode können Zeichen einem char[] hinzugefügt werden.
     * @param origin bisheriger char[]
     * @param z das Zeichen, welches hinzugefügt werden soll.
     * @return neues char[]
     */
    private static char[] zeichenHinzufügen(char[] origin, char z) {
        char[] newZeichenSatz = new char[origin.length + 1];
        for (int i = 0; i < origin.length; i++) {
            newZeichenSatz[i] = origin[i];
        }
        newZeichenSatz[origin.length] = z;
        return newZeichenSatz;
    }

}
