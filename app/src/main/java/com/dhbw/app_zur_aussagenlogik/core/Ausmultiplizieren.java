package com.dhbw.app_zur_aussagenlogik.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Ausmultiplizieren{

    public static Formel ausmultiplizieren(Formel formel) {
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
                Formel newFormel = ausmultiplizieren(new Formel(formelString));

                formel.blockEinsetzen(newFormel, i - counterLinks + 1, i - 1 + counterRechts);
                klammer++;
                return ausmultiplizieren(formel);
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
                Formel newFormel = ausmultiplizieren(new Formel(formelStringLinks));
                formel.blockEinsetzen(newFormel, indexKlammer, indexKlammer + formelStringLinks.length()-1);
                klammer++;
                return ausmultiplizieren(formel);
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
        Formel parseFormel = Parser.getInstance().parseListToFormel(ergebnisBloecke);
        parseFormel = Parser.getInstance().negationenStreichen(parseFormel);
        ergebnisBloecke = Parser.getInstance().parseFormelToList(parseFormel);
        ergebnisBloecke = Parser.getInstance().zeichenErsetzen(ergebnisBloecke);

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


}
