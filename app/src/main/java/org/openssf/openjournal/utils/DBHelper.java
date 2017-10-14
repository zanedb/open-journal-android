package org.openssf.openjournal.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.openssf.openjournal.R;

import java.util.ArrayList;

/**
 * DBHelper Class
 * Simple database helper class for retrieving notes data
 * @author zanedb on GitHub
 * @version 1.0
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "openJournalDB.db";
    private static final String NOTES_TABLE_NAME = "notes";
    private static final String NOTES_COLUMN_ID = "id";
    private static final String NOTES_COLUMN_TITLE = "title";
    private static final String NOTES_COLUMN_TEXT = "notetext";
    private static final String NOTES_COLUMN_TIMESTAMP = "timestamp";

    private Context mContext;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table "+NOTES_TABLE_NAME+" " +
                        "("+NOTES_COLUMN_ID+" integer primary key, "+NOTES_COLUMN_TITLE+" text,"+NOTES_COLUMN_TEXT+" text,"+NOTES_COLUMN_TIMESTAMP+" text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+NOTES_TABLE_NAME+"");
        onCreate(db);
    }

    public boolean insertNote (String title, String notetext, String timestamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTES_COLUMN_TITLE, title);
        contentValues.put(NOTES_COLUMN_TEXT, notetext);
        contentValues.put(NOTES_COLUMN_TIMESTAMP, timestamp);
        db.insert(NOTES_TABLE_NAME, null, contentValues);
        return true;
    }

    public String getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "select "+NOTES_COLUMN_TEXT+" from "+NOTES_TABLE_NAME+" where "+NOTES_COLUMN_ID+"="+id+"", null);
        if(res.moveToFirst()) {
            // Set variable to string
            String notetext = res.getString(0);
            // Close cursor
            res.close();
            // Return variable
            return notetext;
        } else {
            res.close();
            return mContext.getResources().getString(R.string.general_error);
        }
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, NOTES_TABLE_NAME);
    }

    public boolean updateNote (Integer id, String title, String notetext, String timestamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTES_COLUMN_TITLE, title);
        contentValues.put(NOTES_COLUMN_TEXT, notetext);
        contentValues.put(NOTES_COLUMN_TIMESTAMP, timestamp);
        db.update(NOTES_TABLE_NAME, contentValues, ""+NOTES_COLUMN_ID+" = ? ", new String[] { Integer.toString(id) });
        return true;
    }

    public Integer deleteNote(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(NOTES_TABLE_NAME,
                ""+NOTES_COLUMN_ID+" = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<String> getAllNotes() {
        ArrayList<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+NOTES_TABLE_NAME+"", null);
        res.moveToFirst();

        while(!res.isAfterLast()){
            array_list.add(res.getString(res.getColumnIndex(NOTES_COLUMN_TITLE)));
            res.moveToNext();
        }
        res.close();
        return array_list;
    }

    public Integer getNoteIdFromTitle(String title) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "select "+NOTES_COLUMN_ID+" from "+NOTES_TABLE_NAME+" where "+NOTES_COLUMN_TITLE+"='"+title+"'", null);
        if(res.moveToFirst()) {
            int noteid = res.getInt(0);
            res.close();
            return noteid;
        }
        else {
            res.close();
            return -1;
        }

    }

    public boolean doesNoteExist(String title) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "select * from "+NOTES_TABLE_NAME+" where "+NOTES_COLUMN_TITLE+"='"+title+"'", null);
        if(res.moveToFirst()) {
            res.close();
            return true;
        }
        else {
            res.close();
            return false;
        }
    }

    public String getTimestamp(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "select "+NOTES_COLUMN_TIMESTAMP+" from "+NOTES_TABLE_NAME+" where "+NOTES_COLUMN_ID+"="+id+"", null);
        if(res.moveToFirst()) {
            String timestamp = res.getString(0);
            res.close();
            return timestamp;
        }
        else {
            res.close();
            return mContext.getResources().getString(R.string.general_error);
        }
    }
}
