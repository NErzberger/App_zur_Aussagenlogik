package com.dhbw.app_zur_aussagenlogik.sql.dbHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dhbw.app_zur_aussagenlogik.sql.dataObjects.History;

import java.util.ArrayList;
import java.util.List;

public class HistoryDataSource {

    private static final String LOG_TAG = HistoryDataSource.class.getSimpleName();

    private SQLiteDatabase database;
    private HistoryDbHelper dbHelper;

    public HistoryDataSource(Context context){
        Log.d(LOG_TAG, "Unsere DataSource erzeugt jetzt den dbHelper");
        dbHelper = new HistoryDbHelper(context);
        database = this.dbHelper.getDB();
    }

    public History addHistoryEntry(History history){

        ContentValues values = new ContentValues();
        values.put(HistoryDbHelper.COLUMN_MODI, history.getModi());
        values.put(HistoryDbHelper.COLUMN_FORMULA, history.getFormula());
        values.put(HistoryDbHelper.COLUMN_SECOND_FORMULA, history.getSecondFormula());

        long newId = database.insert(HistoryDbHelper.TABLE_HISTORY, null, values);
        List<History> historyEntries = getAllHistoryEntries();
        if(historyEntries.size()>50){
            database.delete(HistoryDbHelper.TABLE_HISTORY, HistoryDbHelper.COLUMN_ID + "="+historyEntries.get(0).getId(), null);
        }


        if(newId == -1){
            return new History(-1, null, null, null);
        }else{
            Cursor cursor = database.query(HistoryDbHelper.TABLE_HISTORY, HistoryDbHelper.columns, "_id="+newId, null, null, null, HistoryDbHelper.COLUMN_ID);
            cursor.moveToFirst();
            History h = cursorToHistory(cursor);
            cursor.close();
            return h;
        }
    }

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

    public History getOneBeforeHistory(int id){
        Cursor cursor = database.query(HistoryDbHelper.TABLE_HISTORY, HistoryDbHelper.columns, "_id="+id+"-1", null, null, null, HistoryDbHelper.COLUMN_ID);
        cursor.moveToFirst();
        History h = cursorToHistory(cursor);
        cursor.close();
        return h;
    }

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
