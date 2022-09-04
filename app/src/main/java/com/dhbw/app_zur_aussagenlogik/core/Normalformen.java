package com.dhbw.app_zur_aussagenlogik.core;

import java.util.List;

public class Normalformen {

    protected static boolean weitereAufloesungNotwendig(Formel formel) {
        for (int i = 0; i < formel.length(); i++) {
            if(i+1<formel.length()) {
                if ((formel.getChar(i) == '+' && formel.getChar(i + 1) == '(') || (formel.getChar(i) == '+' && formel.getChar(i - 1) == ')')) {
                    return true;
                }
            }
        }
        return false;
    }

    protected static boolean blockHinzufügen(List<Formel> blockList, Formel block){
        for(int i = 0; i < blockList.size(); i++){
            Formel b = blockList.get(i);
            if(b.length() == block.length()){
                for(int j = 0; j < b.length(); j++){
                    int match = 0;
                    for(int k = 0; k < block.length(); k++){
                        if(b.getChar(j)==block.getChar(k)){
                            match++;
                        }
                    }
                    if(match==block.length()){
                        return false;
                    }
                }
                return true;
            }
        }
        return true;
    }

    public static boolean existsChar(Formel charArray, char c) {
        for (int i = 0; i < charArray.length(); i++) {
            if(charArray.getChar(i)==c && c !='n') {
                return true;
            }
        }
        return false;
    }


}
