package com.dhbw.app_zur_aussagenlogik.sql.dbHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dhbw.app_zur_aussagenlogik.sql.dataObjects.History;

import java.util.ArrayList;
import java.util.List;

/**
 * Die Klasse HistoryDataSource wird als Datenquelle für historische Elemente der Klasse {@link History} verwendet.
 * Sie nutzt für den Zugriff auf die Datenbank, welche eine SQLite Datenbank ist, die Klasse {@link HistoryDbHelper}.
 * Sie wird im MainFragment und im Fragment {@link com.dhbw.app_zur_aussagenlogik.fragments.HistoryFragment} verwendet.
 *
 * @author Nico Erzberger
 * @version 1.0
 * @see com.dhbw.app_zur_aussagenlogik.fragments.HistoryFragment
 */
public class HistoryDataSource {

    /**
     * Der LOG_TAG wird für das Logging verwendet, um den Namen der Klasse für das Logfile schneller zu erfassen.
     */
    private static final String LOG_TAG = HistoryDataSource.class.getSimpleName();

    /**
     * Die Klassenvariable database ist ein Objekt der Klasse {@link SQLiteDatabase} und stellt die eigentliche Datenbank dar.
     * Um Operationen in der Datenbank auszuführen, muss das Klassenattribut verwendet werden. Es können die Operationen
     * direkt über das Attribut ausgeführt werden.
     */
    private SQLiteDatabase database;

    /**
     * Das Klassenattribut dbHelper ist ein Objekt der Klasse {@link HistoryDbHelper}. Es dient als Zugriffsklasse auf die
     * Datenbank und ist für deren Verwaltung und initial auch für die Erstellung verantwortlich.
     */
    private HistoryDbHelper dbHelper;

    /**
     * Um ein Objekt der Klasse {@link HistoryDataSource} zu erstellen, ist ein Objekt des Context notwendig,
     * da der {@link HistoryDbHelper} diesen benötigt.
     * Ist der historyDbHelper erzeugt, so wird die Datenbank geladen.
     * @param context
     */
    public HistoryDataSource(Context context){
        Log.d(LOG_TAG, "Unsere DataSource erzeugt jetzt den dbHelper");
        dbHelper = new HistoryDbHelper(context);
        database = this.dbHelper.getDB();
    }

    /**
     * Mittels der Methode addHistoryEntry wird ein historisches Element {@link History} in die Datenbank eingetragen.
     * Zunächst werden die Werte in eine Variable der Klasse {@link ContentValues} übernommen. Daraufhin wird ein insert
     * Befehl an die Datenbank geschickt, welcher die Parameter Tabelenname und die übernommenen Variablen bekommt.
     * Ist das Element erfolgreich eingetragen, so wird die komplette Liste gelesen. Wenn mehr als 50 Einträge in der Tabelle
     * stehen, so wird das erste Element gelöscht.
     * Die Methode gibt das eingetragenen historische Element mit ID zurück.
     * @param history Übergabeparameter der Klasse {@link History}.
     * @return Es wird das hinzugefügte historische Element mit ID zurückgegeben.
     */
    public History addHistoryEntry(History history){

        // Werte der history in values übernehmen
        ContentValues values = new ContentValues();
        values.put(HistoryDbHelper.COLUMN_MODI, history.getModi());
        values.put(HistoryDbHelper.COLUMN_FORMULA, history.getFormula());
        values.put(HistoryDbHelper.COLUMN_SECOND_FORMULA, history.getSecondFormula());

        // history in db speichern
        long newId = database.insert(HistoryDbHelper.TABLE_HISTORY, null, values);
        //Tabelle lesen
        List<History> historyEntries = getAllHistoryEntries();

        // Wenn mehr als 50 Elemente in der Datenbank stehen, den ersten Eintrag löschen.
        if(historyEntries.size()>50){
            database.delete(HistoryDbHelper.TABLE_HISTORY, HistoryDbHelper.COLUMN_ID + "="+historyEntries.get(0).getId(), null);
        }

        // Bei einem Fehler ein leeres History Element zurückgeben.
        if(newId == -1){
            return new History(-1, null, null, null);
        }else{
            // Den eben hinzugefügten Eintrag zurückgeben.
            Cursor cursor = database.query(HistoryDbHelper.TABLE_HISTORY, HistoryDbHelper.columns, "_id="+newId, null, null, null, HistoryDbHelper.COLUMN_ID);
            cursor.moveToFirst();
            History h = cursorToHistory(cursor);
            cursor.close();
            return h;
        }
    }

    /**
     * Die Methode getAllHistoryEntries liest die Tabelle TABLE_HISTORY und gibt den kompletten Inhalt als Liste
     * mit {@link History} generics zurück.
     * @return Es wird eine List Objekten der Klasse {@link History} zurückgegeben.
     */
    public List<History> getAllHistoryEntries(){
        List<History> historyEntries = new ArrayList<>();

        Cursor cursor = database.query(HistoryDbHelper.TABLE_HISTORY, HistoryDbHelper.columns, null,
                null,null,null,HistoryDbHelper.COLUMN_ID);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            historyEntries.add(cursorToHistory(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return historyEntries;
    }

    /**
     * Die Methode getOneBeforeHistory wird dazu verwendet,
     * @param id
     * @return
     */
    public History getOneBeforeHistory(int id){
        Cursor cursor = database.query(HistoryDbHelper.TABLE_HISTORY, HistoryDbHelper.columns, "_id="+id+"-1", null, null, null, HistoryDbHelper.COLUMN_ID);
        cursor.moveToFirst();
        History h = cursorToHistory(cursor);
        cursor.close();
        return h;
    }

    /**
     *
     * @param cursor
     * @return
     */
    private History cursorToHistory(Cursor cursor){
        int idIndex = cursor.getColumnIndex(HistoryDbHelper.COLUMN_ID);
        int idModi = cursor.getColumnIndex(HistoryDbHelper.COLUMN_MODI);
        int idFormula = cursor.getColumnIndex(HistoryDbHelper.COLUMN_FORMULA);
        int idSecondFormula = cursor.getColumnIndex(HistoryDbHelper.COLUMN_SECOND_FORMULA);

        String modi = cursor.getString(idModi);
        String formula = cursor.getString(idFormula);
        String secondFormula = cursor.getString(idSecondFormula);
        int id = cursor.getInt(idIndex);

        History h = new History(id, modi, formula, secondFormula);
        return h;
    }

}
