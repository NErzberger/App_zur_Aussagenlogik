package com.dhbw.app_zur_aussagenlogik.sql.dbHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.dhbw.app_zur_aussagenlogik.sql.dataObjects.History;

import java.util.ArrayList;
import java.util.List;

/**
 * Die Klasse <b>HistoryDbHelper</b> dienst als Zugriffsklasse auf die Datenbank HistoryDB, in welchem der
 * Verlauf der eingegebenen Formeln gespeichert wird.
 * Die Klasse erbt von der Klasse {@link SQLiteOpenHelper}.
 * Die Funktion der Klasse ist an sich, die Datenbank beim ersten Aufruf zu erzeugen und zurückzugeben.
 *
 * @author Nico Erzberger
 * @version 1.0
 */
public class HistoryDbHelper extends SQLiteOpenHelper {

    /**
     * Der LOG_TAG wird für das Logging verwendet, um den Namen der Klasse für das Logfile schneller zu erfassen.
     */
    private static final String LOG_TAG = HistoryDbHelper.class.getSimpleName();

    /**
     * Statische finale Variable für den Namen der Datenbank 'HistoryDB'
     */
    public static final String DB_NAME = "HistoryDB";
    /**
     * Staitische finale Variable für die Version der Datenbank '1'
     */
    public static final int DB_VERSION = 1;

    /**
     * Statische finale Variable für den Namen der Tabelle 'history'
     */
    public static final String TABLE_HISTORY = "history";

    /**
     * Statische finale Variable für die Tabellenspalte '_id'.
     * Aus technischen Gründen innerhalb des SQLite-Frameworks wurde ein führender Unterstrich gewählt.
     */
    public static final String COLUMN_ID = "_id";
    /**
     * Statische finale Variable für die Tabellenspalte 'modi'
     */
    public static final String COLUMN_MODI = "modi";
    /**
     * Statische finale Variable für die Tabellenspalte 'formula'
     */
    public static final String COLUMN_FORMULA = "formula";
    /**
     * Staitsche finale Variable für die Tabellenspalte 'secondFormula'
     */
    public static final String COLUMN_SECOND_FORMULA = "secondFormula";


    /**
     * Statische finale Variable, die in einem String Array alle Tabellenspalten zusammenfasst.
     */
    public static final String[] columns = {COLUMN_ID, COLUMN_MODI, COLUMN_FORMULA, COLUMN_SECOND_FORMULA};

    /**
     * Statische finale Variable für das SQL-Statemet, um die Tabelle history zu erstellen.
     */
    public static final String SQL_CREATE =
            "CREATE TABLE IF NOT EXISTS "+ TABLE_HISTORY +
                    "("+COLUMN_ID + " InTEGER PRIMARY KEY AUTOINCREMENT, "+
                    COLUMN_MODI + " TEXT, "+
                    COLUMN_FORMULA + " TEXT NOT NULL, " +
                    COLUMN_SECOND_FORMULA + " TEXT);";

    /**
     * Um ein Objekt zu erstellen, muss der Context mitgegeben werden.
     * Beim erstellen eines Objektes wird die onCreate Methode ausgeführt.
     * @param context Übergabeparamenter der Klasse {@link Context}
     */
    public HistoryDbHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
        //context.deleteDatabase(getDatabaseName());
        onCreate(this.getWritableDatabase());
        Log.d(LOG_TAG, "DbHelper hat die Datenbank: " +getDatabaseName() + " erzeugt.");
    }

    /**
     * Die Methode onCreate erstellt die Tabelle history.
     * Sie wird vom Konstruktor aus aufgerufen, um beim ersten Start die Tabelle zu erstellen.
     * @param db Übergabeparameter ist die Datenbank bzw. das Objekt davon der Klasse {@link SQLiteDatabase}
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: "+ SQL_CREATE + " angelegt.");
            db.execSQL(SQL_CREATE);
        }catch (Exception ex){
            Log.e(LOG_TAG, "Fehler beim Anlegen der Tabelle: "+ex.getMessage());
        }
    }

    /**
     * <b>Unused</b>
     * @param sqLiteDatabase
     * @param i
     * @param i1
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    /**
     * Die Methode getDB wird ausgeführt, um die zum Helper zugehörige Datenbank zurückzugeben.
     * Hierzu wird die Methode getWritableDatabese ausgeführt.
     * @return Gibt ein Datenbankobjekt der Klasse {@link SQLiteDatabase} zurück.
     */
    public SQLiteDatabase getDB(){
        return this.getWritableDatabase();
    }



}
