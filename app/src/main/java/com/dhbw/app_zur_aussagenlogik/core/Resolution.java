package com.dhbw.app_zur_aussagenlogik.core;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Resolution {

    private List<Formel> resolutionsliste;

    List<Resolutionsschritt> resolutionsschritte = new ArrayList<Resolutionsschritt>();

    public Resolution(){
        this.resolutionsliste = new ArrayList<>();
    }


    public Formel variablenEliminieren(Formel formel){

        //Hier werden unnötige Negationen gestrichen
        formel = Parser.getInstance().negationenStreichen(formel);

        // Es wird eine Gesamtmenge gebildet mit allen Teilmengen drin
        List<Formel> gesamtmenge = new ArrayList<>();
        // neue Teilmenge
        Formel teilmenge = new Formel();

        List<Formel> endmenge = new ArrayList<>();

        // Es wird die Formel ausgelesen und die Teilmengen gebildet und die Teilmengen in die Gesamtmenge hinzugefügt
        for(int i = 0; i<formel.length(); i++) {
            if(Character.toString(formel.getChar(i)).matches("[a-n]")) {
                teilmenge.zeichenHinzufügen(formel.getChar(i));
            }else if(formel.getChar(i)=='*') {
                gesamtmenge.add(teilmenge);
                teilmenge = new Formel();
            }
        }
        gesamtmenge.add(teilmenge);

        for(int i = 0; i < gesamtmenge.size(); i++){
            Formel menge = gesamtmenge.get(i);
            Formel newMenge = new Formel();
            for(int j = 0; j < menge.length(); j++){
                if(menge.getChar(j) == 'n'){
                    continue;
                }
                boolean match = false;
                for(int k = j+1; k < menge.length(); k++){
                    // wenn j ungleich k (darf nicht mit sich selbst verglichen werden)
                    // und der Buchstabe an der Stelle j dem Buchstabe an Stelle k entspricht und k kein n ist
                    // ist ein doppelter

                    if(j != k && menge.getChar(j) == menge.getChar(k) && menge.getChar(k) != 'n'){
                        if(j>0) {
                            if (menge.getChar(j - 1) == 'n' && menge.getChar(k - 1) == 'n' ||
                                    menge.getChar(j - 1) != 'n' && menge.getChar(k - 1) != 'n') {
                                match = true;
                            }
                        }else if(j==0){
                            // j kann nur positiv sein, daher muss nur geprüft werden, ob das k positiv ist
                            if(menge.getChar(k-1)!='n') {
                                match = true;
                            }
                        }
                    }
                }
                if(!match){
                    if(j>0&&menge.getChar(j-1)=='n'){
                        newMenge.zeichenHinzufügen('n');
                    }
                    newMenge.zeichenHinzufügen(menge.getChar(j));
                }
            }

            // Teilmenge zur Sortierung in die Zahlen 1 - 9 konvertieren
            String convertedFormel = "";
            for(int j = 0; j < newMenge.length(); j++){
                if(newMenge.getChar(j)=='a' ) {
                       if (j > 0 && newMenge.getChar(j - 1) == 'n') {
                            convertedFormel = convertedFormel+"1";
                       } else {
                           convertedFormel = convertedFormel+"0";
                       }
                    }
                if(newMenge.getChar(j)=='b' ) {
                    if (j > 0 && newMenge.getChar(j - 1) == 'n') {
                        convertedFormel = convertedFormel+"3";
                    } else {
                        convertedFormel = convertedFormel+"2";
                    }
                }
                if(newMenge.getChar(j)=='c' ) {
                    if (j > 0 && newMenge.getChar(j - 1) == 'n') {
                        convertedFormel = convertedFormel+"5";
                    } else {
                        convertedFormel = convertedFormel+"4";
                    }
                }
                if(newMenge.getChar(j)=='d' ) {
                    if (j > 0 && newMenge.getChar(j - 1) == 'n') {
                        convertedFormel = convertedFormel+"7";
                    } else {
                        convertedFormel = convertedFormel+"6";
                    }
                }
                if(newMenge.getChar(j)=='e' ) {
                    if (j > 0 && newMenge.getChar(j - 1) == 'n') {
                        convertedFormel = convertedFormel+"9";
                    } else {
                        convertedFormel = convertedFormel+"8";
                    }
                }
            }

            // Die konvertierte Teilmenge sortieren
            char[] c = convertedFormel.toCharArray();
            Arrays.sort(c);
            String sortedString = new String(c);

            // Prüfen, ob die Teilmenge "unnötig" ist
            if(sortedString.contains("0")&&sortedString.contains("1")){
                continue;
            }else if(sortedString.contains("2")&&sortedString.contains("3")){
                continue;
            }else if(sortedString.contains("4")&&sortedString.contains("5")){
                continue;
            }else if(sortedString.contains("6")&&sortedString.contains("7")){
                continue;
            }else if(sortedString.contains("8")&&sortedString.contains("9")){
                continue;
            }



            // Die konvertierte sortierte Teilmenge zurück in Buchstaben konvertieren
            Formel sortedFormel = new Formel();
            for(int j=0; j < sortedString.length(); j++){
                int charValue = Integer.parseInt(String.valueOf(sortedString.charAt(j)));
                switch (charValue){
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

        Formel ergebnisFormel = new Formel();

        for(int i = 0; i < endmenge.size(); i++){
            if(i > 0){
                ergebnisFormel.zeichenHinzufügen(';');
                ergebnisFormel.zeichenHinzufügen(' ');
            }
            ergebnisFormel.zeichenHinzufügen('{');
            Formel subFormel = endmenge.get(i);
            for(int j = 0; j < subFormel.length(); j++){
                if(subFormel.getChar(j)=='n'){
                    ergebnisFormel.zeichenHinzufügen(subFormel.getChar(j));
                    j++;
                    ergebnisFormel.zeichenHinzufügen(subFormel.getChar(j));
                }else {
                    ergebnisFormel.zeichenHinzufügen(subFormel.getChar(j));
                }
                if(j < subFormel.length()-1){
                    ergebnisFormel.zeichenHinzufügen(',');
                }
            }
            ergebnisFormel.zeichenHinzufügen('}');
        }

        return ergebnisFormel;
    }


    public boolean resolvieren(Formel formel){

        // Es wird eine Gesamtmenge gebildet mit allen Teilmengen drin
        List<Formel> gesamtmenge = new ArrayList<>();
        // neue Teilmenge
        Formel teilmenge = new Formel();

        // Es wird die Formel ausgelesen und die Teilmengen gebildet und die Teilmengen in die Gesamtmenge hinzugefügt
        for(int i = 0; i<formel.length(); i++) {
            if(Character.toString(formel.getChar(i)).matches("[a-n]")) {
                teilmenge.zeichenHinzufügen(formel.getChar(i));
            }else if(formel.getChar(i)==';') {
                gesamtmenge.add(teilmenge);
                teilmenge = new Formel();
            }
        }
        gesamtmenge.add(teilmenge);


        boolean weitermachen = true;
        int current = 0;
        while(weitermachen) {
            for(int i = 0; i < gesamtmenge.get(current).length(); i++) {
                boolean gefunden = false;
                int indexTeilmenge = 0;
                char searchChar;
                int indexSearchChar = 0;
                int indexFoundedChar = 0;
                if(gesamtmenge.get(current).getChar(i)=='n') {
                    searchChar = gesamtmenge.get(current).getChar(i+1);
                    indexSearchChar = i;
                    for (int j = 0; j < gesamtmenge.size() && gefunden == false; j++) {
                        if(current!=j) {
                            for (int k = 0; k < gesamtmenge.get(j).length(); k++) {
                                if ((k > 0 && gesamtmenge.get(j).getChar(k - 1) != 'n' && searchChar == gesamtmenge.get(j).getChar(k))
                                        || (k==0 && searchChar == gesamtmenge.get(j).getChar(k))) {
                                    gefunden = true;
                                    indexTeilmenge = j;
                                    indexFoundedChar = k;
                                }
                            }
                        }
                    }
                    if(gefunden){
                        Resolutionsschritt r = new Resolutionsschritt();
                        r.setBlockEins(gesamtmenge.get(i));
                        r.setBlockZwei(gesamtmenge.get(indexTeilmenge));
                        Formel newBlock = new Formel();
                        for(int j = 0; j < gesamtmenge.get(i).length(); j++) {
                            // Suchwert mit negation rausstreichen
                            if(j == indexSearchChar-1 || j == indexSearchChar){
                                continue;
                            }

                            newBlock.zeichenHinzufügen(gesamtmenge.get(i).getChar(j));
                        }
                        for(int j = 0; j < gesamtmenge.get(indexTeilmenge).length(); j++) {
                            // Gefundener Buchstabe überspringen / rausstreichen
                            if(j == indexFoundedChar){
                                continue;
                            }

                            boolean darfHinzugefügtWerden = true;
                            for(int k = 0; k < gesamtmenge.get(i).length(); k++){
                                if(gesamtmenge.get(i).getChar(k)=='n'){
                                    continue;
                                }
                                // Hier kann weder j noch k negiert sein
                                else if(k==0 && j==0&&gesamtmenge.get(i).getChar(k)==gesamtmenge.get(indexTeilmenge).getChar(j)){
                                    darfHinzugefügtWerden = false;
                                }
                                // Prüfen, ob j und k negiert sind
                                else if(k>0 && j > 0 &&gesamtmenge.get(i).getChar(k)==gesamtmenge.get(indexTeilmenge).getChar(j)
                                && gesamtmenge.get(i).getChar(k-1)=='n' && gesamtmenge.get(indexTeilmenge).getChar(j-1)=='n'){
                                    darfHinzugefügtWerden = false;
                                }
                                // Prüfen, ob j und k nicht negiert sind
                                else if(k>0 && j > 0 &&gesamtmenge.get(i).getChar(k)==gesamtmenge.get(indexTeilmenge).getChar(j)
                                        && gesamtmenge.get(i).getChar(k-1)!='n' && gesamtmenge.get(indexTeilmenge).getChar(j-1)!='n'){
                                    darfHinzugefügtWerden = false;
                                }
                                // Prüfen, ob k negiert ist, da j nur positiv sein kann
                                else if(k>0 && j == 0 &&gesamtmenge.get(i).getChar(k)==gesamtmenge.get(indexTeilmenge).getChar(j)
                                        && gesamtmenge.get(i).getChar(k-1)!='n'){
                                    darfHinzugefügtWerden = false;
                                }
                                // Prüfen, ob j negiert ist da k nur positiv sein kann
                                else if(k==0 && j > 0 &&gesamtmenge.get(i).getChar(k)==gesamtmenge.get(indexTeilmenge).getChar(j)
                                        && gesamtmenge.get(indexTeilmenge).getChar(j-1)!='n'){
                                    darfHinzugefügtWerden = false;
                                }
                            }
                            if(darfHinzugefügtWerden) {
                                newBlock.zeichenHinzufügen(gesamtmenge.get(indexTeilmenge).getChar(j));
                            }
                        }
                        r.setErgebnis(newBlock);
                        resolutionsschritte.add(r);

                        /*
                        !!!!!Leere Menge!!!!!
                         */
                        if(newBlock.length()==0){
                            return true;
                        }


                        gesamtmenge.add(newBlock);

                    }
                }
            }
            current++;

            if(current == gesamtmenge.size()){
                break;
            }
        }



        return false;
    }
}
