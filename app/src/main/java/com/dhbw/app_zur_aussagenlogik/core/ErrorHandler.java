package com.dhbw.app_zur_aussagenlogik.core;

import java.util.HashMap;

public class ErrorHandler {

    private HashMap<Integer, String> errorMessages;

    private ErrorHandler(){
        errorMessages = new HashMap<>();
        errorMessages.clear();
        errorMessages.put(-1, "Es wurden unterschiedlich viele Klammern angegeben.");
        errorMessages.put(-2, "Es wurden mehrere Buchstaben ohne Operatoren aneinandergereiht.");
        errorMessages.put(-3, "Es darf kein Operator auf eine öffnende Klammer folgen.");
        errorMessages.put(-4, "Es darf kein Buchstabe, keine Negation oder öffnede Klammer auf eine schließende Klammer folgen.");
        errorMessages.put(-5, "Es darf kein Operator oder schließende Klammer auf eine Negation folgen.");
        errorMessages.put(-6, "Es darf kein weiterer Operator oder eine schließende Klammer auf einen Operator folgen.");
        errorMessages.put(-7, "Es darf kein Operator an erster Stelle stehen");
        errorMessages.put(-8, "Die letzte Stelle darf keine öffnede Klammer sein.");
        errorMessages.put(-9, "Die erste stelle darf keine schließende Klammer sein.");
        errorMessages.put(-11, "Es darf keine Negation an letzter Stelle sein.");
        errorMessages.put(-12, "Es darf kein Operator an letzter Stelle sein.");
    }

    private static ErrorHandler instance;

    public static void newInstance(){
        ErrorHandler.instance = new ErrorHandler();
    }

    public static String getErrorMessage(int errorCode){
        return ErrorHandler.instance.errorMessages.get(errorCode);
    }
}
