package com.stone.mmt.app.notelite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class NoteDBAdapter {
    private final Context context;
    private NoteHelper noteHelper;
    private SQLiteDatabase sqLiteDatabase;
    int indexTitle,indexDesc,indexDateTime;
    public NoteDBAdapter(Context context) {
        this.context = context;
    }
    public void dbOpen() {
        noteHelper = new NoteHelper(context);
        sqLiteDatabase = noteHelper.getWritableDatabase();
    }

    public long noteInsert(String title,String desc,String dateTime) {
        ContentValues cv = new ContentValues();
        cv.put(NoteHelper.NOTE_TITLE,title);
        cv.put(NoteHelper.NOTE_DESC,desc);
        cv.put(NoteHelper.NOTE_DATETIME,dateTime);
        return sqLiteDatabase.insert(NoteHelper.TB_NAME,null,cv);
    }

    public void dbClose() {
        sqLiteDatabase.close();
    }
    Cursor readAllData() {
        String query = "SELECT * FROM "+ NoteHelper.TB_NAME;
        sqLiteDatabase = noteHelper.getReadableDatabase();
        Cursor cursor = null;
        if (sqLiteDatabase != null) {
            cursor = sqLiteDatabase.rawQuery(query,null);
            indexTitle = cursor.getColumnIndex(NoteHelper.NOTE_TITLE);
            indexDesc = cursor.getColumnIndex(NoteHelper.NOTE_DESC);
            indexDateTime = cursor.getColumnIndex(NoteHelper.NOTE_DATETIME);
        }
        return cursor;
    }
    public int noteUpdate(String dateTime,String newTitle,String newDesc,String newDateTime) {
        noteHelper = new NoteHelper(context);
        noteHelper.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NoteHelper.NOTE_TITLE,newTitle);
        cv.put(NoteHelper.NOTE_DESC,newDesc);
        cv.put(NoteHelper.NOTE_DATETIME,newDateTime);
        String [] arg = {dateTime};
        return sqLiteDatabase.update(NoteHelper.TB_NAME,cv,NoteHelper.NOTE_DATETIME+" =?",arg);
    }

    public void noteDelete(String dateTime) {
        sqLiteDatabase.delete(NoteHelper.TB_NAME, NoteHelper.NOTE_DATETIME + " =?", new String[]{dateTime});
    }

    public void deleteAllData() {
        String deleteAll = "DELETE FROM "+NoteHelper.TB_NAME;
        sqLiteDatabase.execSQL(deleteAll);
    }

    private static class NoteHelper extends SQLiteOpenHelper {
        private static final String DB_NAME = "d_nana";
        private static final String TB_NAME = "t_nana";
        private static final String NOTE_TITLE = "title";
        private static final String NOTE_DESC = "_desc";
        private static final String NOTE_DATETIME = "date_time";
        private static final int DB_VERSION = 1;

        public NoteHelper(@Nullable Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE "+TB_NAME+" ("+NOTE_TITLE+" TEXT,"+NOTE_DESC +
                    " TEXT,"+ NOTE_DATETIME +" TEXT)");

            //Log.d("DataBase","onCreate");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+TB_NAME);
            //Log.d("DataBase","onUpgrade");
            onCreate(db);
        }
    }
}
