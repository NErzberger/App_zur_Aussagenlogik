package com.dhbw.app_zur_aussagenlogik.core;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Die Klasse <b>Resolution</b> ist für die Klauselschreibweise und die Bildung der Resolution
 * zuständig.
 *
 *    !!!WICHTIG!!!
 * Diese Klasse ist noch nicht fertig implementiert oder komplett getestet worden!!!
 *    !!!WICHTIG!!!
 *
 * @author Nico Erzberg, Daniel Miller
 * @version 1.0
 */

public class Resolution {

    private List<Formel> resolutionsliste;

    List<Resolutionsschritt> resolutionsschritte = new ArrayList<Resolutionsschritt>();

    public Resolution(){
        this.resolutionsliste = new ArrayList<>();
    }


    /**
     * In dieser Methode wird die übergebene Formel, welche in der KNF ist, in die Klauselschreibweise
     * überführt.
     * @param formel übergebene Formel in KNF
     * @return Formel in der Klauselschreibweise
     */
    public Formel klauselschreibweise(Formel formel){

        Formel ergebnisFormel = new Formel();
        List<char[]> endmenge = new ArrayList<>();

        formel = Parser.getInstance().negationenStreichen(formel);
        endmenge = Parser.getInstance().parseFormelToList(formel);
        endmenge = Parser.getInstance().zeichenErsetzen(endmenge);
        endmenge = Parser.getInstance().teilmengenErsetzten(endmenge);

        for(int i = 0; i < endmenge.size(); i++){
            if(i > 0){
                ergebnisFormel.zeichenHinzufügen(';');
                ergebnisFormel.zeichenHinzufügen(' ');
            }
            ergebnisFormel.zeichenHinzufügen('{');
            char[] subMenge = endmenge.get(i);
            for(int j = 0; j < subMenge.length; j++){
                if(subMenge[j]=='n'){
                    ergebnisFormel.zeichenHinzufügen(subMenge[j]);
                    j++;
                    ergebnisFormel.zeichenHinzufügen(subMenge[j]);
                }else {
                    ergebnisFormel.zeichenHinzufügen(subMenge[j]);
                }
                if(j < subMenge.length-1){
                    ergebnisFormel.zeichenHinzufügen(',');
                }
            }
            ergebnisFormel.zeichenHinzufügen('}');
        }

        return ergebnisFormel;
    }


    /**
     * In dieser Methode wird die übergebene Formel resolviert.
     * @param formel übergebene Formel
     * @return boolean ob Tautologie oder nicht
     */
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
