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

public class HistoryDbHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = HistoryDbHelper.class.getSimpleName();


    public static final String DB_NAME = "HistoryDB";
    public static final int DB_VERSION = 1;

    public static final String TABLE_HISTORY = "history";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_FORMULA = "formula";
    public static final String COLUMN_SECOND_FORMULA = "secondFormula";


    public static final String[] columns = {COLUMN_ID, COLUMN_FORMULA, COLUMN_SECOND_FORMULA};

    public static final String SQL_CREATE =
            "CREATE TABLE IF NOT EXISTS "+ TABLE_HISTORY +
                    "("+COLUMN_ID + " InTEGER PRIMARY KEY AUTOINCREMENT, "+
                    COLUMN_FORMULA + " TEXT NOT NULL, " +
                    COLUMN_SECOND_FORMULA + " TEXT);";

    public HistoryDbHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
        onCreate(this.getWritableDatabase());
        Log.d(LOG_TAG, "DbHelper hat die Datenbank: " +getDatabaseName() + " erzeugt.");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: "+ SQL_CREATE + " angelegt.");
            db.execSQL(SQL_CREATE);
        }catch (Exception ex){
            Log.e(LOG_TAG, "Fehler beim Anlegen der Tabelle: "+ex.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public SQLiteDatabase getDB(){
        return this.getWritableDatabase();
    }



}
