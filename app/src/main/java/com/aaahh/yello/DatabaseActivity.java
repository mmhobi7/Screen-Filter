package com.aaahh.yello;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DatabaseActivity {

    private final Context context;

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    /* Inner class that defines the table contents */
    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_SUBTITLE = "subtitle";

        private static final String TEXT_TYPE = " TEXT";
        private static final String COMMA_SEP = ",";
        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                        FeedEntry._ID + " INTEGER PRIMARY KEY," +
                        FeedEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
                        FeedEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                        " )";

        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;
    }

    public static class DatabaseHelper extends SQLiteOpenHelper {
        // If you change the database schema, you must increment the database version.
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "FeedReader.db";

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(FeedEntry.SQL_CREATE_ENTRIES);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(FeedEntry.SQL_DELETE_ENTRIES);
            onCreate(db);
        }

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }

    public DatabaseActivity(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    //---opens the database---
    public DatabaseActivity open() {
        SQLiteDatabase db = DBHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---
    public void close() {
        DBHelper.close();
    }

    //---insert a title into the database---
    public void insertTitle(String isbn, String title, String content) {
        ContentValues values = new ContentValues();
        values.put(FeedEntry.COLUMN_NAME_ENTRY_ID, isbn);
        values.put(FeedEntry.COLUMN_NAME_TITLE, title);
        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                FeedEntry.TABLE_NAME,
                null,
                values);
    }

    //---deletes a particular title---
    public boolean deleteTitle(long rowId) {
        return db.delete(FeedEntry.TABLE_NAME, FeedEntry.COLUMN_NAME_ENTRY_ID +
                "=" + rowId, null) > 0;
    }


    //---retrieves a particular title---
    public Cursor getTitle(long rowId) throws SQLException {
        Cursor mCursor =
                db.query(true, FeedEntry.TABLE_NAME, new String[]{
                                FeedEntry.COLUMN_NAME_ENTRY_ID,
                                FeedEntry.COLUMN_NAME_TITLE,
                                FeedEntry.COLUMN_NAME_SUBTITLE
                        },
                        FeedEntry.COLUMN_NAME_ENTRY_ID + "=" + rowId,
                        null,
                        null,
                        null,
                        null,
                        null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //---updates a title---
    public boolean updateTitle(long rowId, String isbn,
                               String title, String publisher) {
        ContentValues args = new ContentValues();
        args.put(FeedEntry.COLUMN_NAME_ENTRY_ID, isbn);
        args.put(FeedEntry.COLUMN_NAME_TITLE, title);
        args.put(FeedEntry.COLUMN_NAME_SUBTITLE, publisher);
        return db.update(FeedEntry.COLUMN_NAME_TITLE, args,
                FeedEntry.COLUMN_NAME_ENTRY_ID + "=" + rowId, null) > 0;
    }

}