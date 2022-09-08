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

    public long addHistoryEntry(History history){

        ContentValues values = new ContentValues();
        values.put(HistoryDbHelper.COLUMN_FORMULA, history.getFormula());
        values.put(HistoryDbHelper.COLUMN_SECOND_FORMULA, history.getSecondFormula());

        long newId = database.insert(HistoryDbHelper.TABLE_HISTORY, null, values);
        List<History> historyEntries = getAllHistoryEntries();
        if(historyEntries.size()>50){
            database.delete(HistoryDbHelper.TABLE_HISTORY, HistoryDbHelper.COLUMN_ID + "="+historyEntries.get(0).getId(), null);
        }


        if(newId == -1){
            return -1;
        }else{
            return newId;
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

    private History cursorToHistory(Cursor cursor){
        int idIndex = cursor.getColumnIndex(HistoryDbHelper.COLUMN_ID);
        int idFormula = cursor.getColumnIndex(HistoryDbHelper.COLUMN_FORMULA);
        int idSecondFormula = cursor.getColumnIndex(HistoryDbHelper.COLUMN_SECOND_FORMULA);

        String formula = cursor.getString(idFormula);
        String secondFormula = cursor.getString(idSecondFormula);
        int id = cursor.getInt(idIndex);

        History h = new History(id, formula, secondFormula);
        return h;
    }

}
