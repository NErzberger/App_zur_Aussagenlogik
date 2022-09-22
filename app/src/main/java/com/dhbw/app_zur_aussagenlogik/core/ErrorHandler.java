package com.dhbw.app_zur_aussagenlogik.core;

import java.util.HashMap;

/**
 * Die Klasse <b>ErrorHandler</b> wird dazu verwendet, um Fehlermeldungen zentral zu halten.
 * Dies soll verhindern, dass Fehlermeldungen über die gesamte App verstreut im Quellcode vorzufinden sind.
 * Die Fehlermeldungen werden in einer HashMap gespeichert. Der zugehörige Schlüssel zu einer Fehlermeldung ist der Fehlercode,
 * welcher beim Werfen eines Fehlers mitgegeben werden muss.
 *
 * @version 1.0
 * @author Nico Erzberger
 */
public class ErrorHandler {

    /**
     * Mittels der {@link HashMap} errorMessages, welches als Key einen int hat, welches der Fehlercode ist, und als Value
     * einen String, was die Fehlermeldung darstellt, können Fehlermeldungen in referenz zum Fehlercode gespeichert werden.
     */
    private HashMap<Integer, String> errorMessages;

    /**
     * Der Konstruktor der Klasse <b>ErrorHandler</b> ist privat.
     * Es ist nicht notwendig, außerhalb der Klasse Objekte des Errorhandlers anzulegen oder diesen zu manipulieren.
     * Der Konstruktor legt die HashMap errorMessages neu an und fügt alle Fehlermeldungen der HashMap hinzu.
     * Es sind aktuell 12 Fehlermeldungen in der Klasse gespeichert:
     * <table>
     *     <tr>
     *         <th>Fehlercode</th>
     *         <th>Fehlermeldung</th>
     *     </tr>
     *     <tr>
     *         <td>-1</td>
     *         <td>Es wurden unterschiedlich viele Klammern angegeben.</td>
     *     </tr>
     *     <tr>
     *         <td>-2</td>
     *         <td>Es wurden mehrere Buchstaben ohne Operatoren aneinandergereiht.</td>
     *     </tr>
     *     <tr>
     *          <td>-3</td>
     *          <td>Es darf kein Operator auf eine öffnende Klammer folgen.</td>
     *      </tr>
     *      <tr>
     *          <td>-4</td>
     *          <td>Es darf kein Buchstabe, keine Negation oder öffnede Klammer auf eine schließende Klammer folgen.</td>
     *      </tr>
     *      <tr>
     *          <td>-5</td>
     *          <td>Es darf kein Operator oder schließende Klammer auf eine Negation folgen.</td>
     *      </tr>
     *      <tr>
     *          <td>-6</td>
     *          <td>Es darf kein weiterer Operator oder eine schließende Klammer auf einen Operator folgen.</td>
     *      </tr>
     *      <tr>
     *          <td>-7</td>
     *          <td>Es darf kein Operator an erster Stelle stehen.</td>
     *      </tr>
     *      <tr>
     *          <td>-8</td>
     *          <td>Die letzte Stelle darf keine öffnede Klammer sein.</td>
     *      </tr>
     *      <tr>
     *          <td>-9</td>
     *          <td>Die erste stelle darf keine schließende Klammer sein.</td>
     *      </tr>
     *      <tr>
     *          <td>-11</td>
     *          <td>Es darf keine Negation an letzter Stelle sein.</td>
     *      </tr>
     *      <tr>
     *         <td>-12</td>
     *         <td>Es darf kein Operator an letzter Stelle sein.</td>
     *     </tr>
     *     <tr>
     *        <td>-13</td>
     *        <td>Bitte geben Sie keine leeren Formeln ein.</td>
     *    </tr>
     * </table>
     */
    private ErrorHandler(){
        errorMessages = new HashMap<>();
        errorMessages.put(-1, "Es wurden unterschiedlich viele Klammern angegeben.");
        errorMessages.put(-2, "Es wurden mehrere Buchstaben ohne Operatoren aneinandergereiht.");
        errorMessages.put(-3, "Es darf kein Operator auf eine öffnende Klammer folgen.");
        errorMessages.put(-4, "Es darf kein Buchstabe, keine Negation oder öffnede Klammer auf eine schließende Klammer folgen.");
        errorMessages.put(-5, "Es darf kein Operator oder schließende Klammer auf eine Negation folgen.");
        errorMessages.put(-6, "Es darf kein weiterer Operator oder eine schließende Klammer auf einen Operator folgen.");
        errorMessages.put(-7, "Es darf kein Operator an erster Stelle stehen.");
        errorMessages.put(-8, "Die letzte Stelle darf keine öffnede Klammer sein.");
        errorMessages.put(-9, "Die erste stelle darf keine schließende Klammer sein.");
        errorMessages.put(-11, "Es darf keine Negation an letzter Stelle sein.");
        errorMessages.put(-12, "Es darf kein Operator an letzter Stelle sein.");
        errorMessages.put(-13, "Bitte geben Sie keine leeren Formeln ein.");
    }

    /**
     * private statische Instanz des Objektes <i>instance</i> der ErrorHandler Klasse selbst.
     * Dies soll einen Zugriff von der gesamten App aus auf das selbe Objekt ermöglichen.
     */
    private static ErrorHandler instance;

    /**
     * Um initial die Instanz <i>instance</i> aufzubauen, wird die Methode newInstance verwendet. Sie wird einmalig in der mainAcition
     * bei Start der App ausgeführt.
     */
    public static void newInstance(){
        ErrorHandler.instance = new ErrorHandler();
    }

    /**
     * Um eine Fehlermeldung anhand des Fehlercodes abzurufen, wird die public static Methode getErrorMessage verwendet.
     * Die Methode kann überall in der App aufgerufen werden. Sie ruft die instance ab, dort die errorMessage und fürht die
     * HashMap Methode get aus, in welchem der übergebene errorCode mit übergeben wird.
     * So kann direkt die Fehlermeldung zurückgegeben werden.
     * @param errorCode Übergabeparameter des Typs int. Es handelt sich um den Fehlercode.
     * @return Es wird die Fehlermedlung als String zurückgegeben.
     */
    public static String getErrorMessage(int errorCode){
        return ErrorHandler.instance.errorMessages.get(errorCode);
    }
}
