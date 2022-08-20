package com.dhbw.app_zur_aussagenlogik.core;

import java.util.ArrayList;
import java.util.List;

public class Ausaddieren extends Normalformen{

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
                    }else if(neuerHinterTeil.getChar(q)=='*'&&(neuerHinterTeil.getChar(q-1)==')'&&neuerHinterTeil.getChar(q+1)=='(')) {
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
    }
}
