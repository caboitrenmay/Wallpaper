package com.fsoc.wallpaper.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;
import static com.fsoc.wallpaper.db.FeedReaderContract.FeedEntry.COLUMN_NAME_PACK;
import static com.fsoc.wallpaper.db.FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE;
import static com.fsoc.wallpaper.db.FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE;
import static com.fsoc.wallpaper.db.FeedReaderContract.FeedEntry.TABLE_NAME;

/**
 * Created by linhcaro on 9/30/2016.
 */

public class FeedReaderDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "FeedReader.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    COLUMN_NAME_SUBTITLE + TEXT_TYPE + COMMA_SEP +
                    COLUMN_NAME_PACK + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public FeedReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
