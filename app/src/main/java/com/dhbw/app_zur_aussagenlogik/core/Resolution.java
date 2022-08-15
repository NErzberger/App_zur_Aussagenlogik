package com.dhbw.app_zur_aussagenlogik.core;

import java.util.ArrayList;
import java.util.List;

public class Resolution {

    public boolean resolvieren(Formel formel){
        List<Formel> blocklist = new ArrayList<>();
        Formel aktuellesChar = new Formel();
        for(int i = 0; i<formel.length(); i++) {
            if(Character.toString(formel.getChar(i)).matches("[a-n]")) {
                aktuellesChar.zeichenHinzufügen(formel.getChar(i));
            }else if(formel.getChar(i)=='*') {
                blocklist.add(aktuellesChar);
                aktuellesChar = new Formel();
            }
        }

        List<Resolutionsschritt> resolutionsschritte = new ArrayList<Resolutionsschritt>();

        boolean weitermachen = true;
        int current = 0;
        while(weitermachen) {
            for(int i = 0; i < blocklist.get(current).length(); i++) {
                boolean gefunden = false;
                int zielblock = 0;
                if(blocklist.get(current).getChar(i)=='n') {
                    char searchChar = blocklist.get(current).getChar(i+1);
                    for (int j = 0; j < blocklist.size() && gefunden == false; j++) {
                        for (int k = 0; k < blocklist.get(j).length(); k++) {
                            if(current!=j&&(k>0&&blocklist.get(j).getChar(k-1)!='n')&&searchChar==blocklist.get(j).getChar(k)) {
                                gefunden=true;
                                zielblock = j;
                            }
                        }
                    }
                    if(gefunden){
                        Resolutionsschritt r = new Resolutionsschritt();
                        r.setBlockEins(blocklist.get(i));
                        r.setBlockZwei(blocklist.get(zielblock));
                        Formel newBlock = new Formel(blocklist.get(i).length()-2);
                        for(int j = 0; j < blocklist.get(i).length(); j++) {
                            if(blocklist.get(i).getChar(j)=='n') {
                                j++;
                                continue;
                            }
                            newBlock.setChar(j, blocklist.get(i).getChar(j));
                        }
                        r.setErgebnis(newBlock);
                        blocklist.add(newBlock);
                    }
                }
            }
            current++;
        }

        return false;
    }
}
