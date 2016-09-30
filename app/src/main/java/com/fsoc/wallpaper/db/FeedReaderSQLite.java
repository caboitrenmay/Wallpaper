package com.fsoc.wallpaper.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fsoc.wallpaper.model.ImgPackObj;

import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static com.fsoc.wallpaper.db.FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE;
import static com.fsoc.wallpaper.db.FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE;
import static com.fsoc.wallpaper.db.FeedReaderContract.FeedEntry.TABLE_NAME;

/**
 * Created by linhcaro on 9/30/2016.
 */

public class FeedReaderSQLite {
    private FeedReaderDbHelper mDbHelper;
    private SQLiteDatabase db;

    public FeedReaderSQLite(Context context) {
        mDbHelper = new FeedReaderDbHelper(context);
        db = mDbHelper.getReadableDatabase();
    }

    public void openDb() {

    }

    public void closeDb() {
        db.close();
    }

    /**
     * Put object and insert to database.
     * @param obj data object.
     * @return primary key value of the new row
     */
    public long putObject(ImgPackObj obj) {
        // Gets the data repository in write mode
        db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(_ID, obj.getId());
        values.put(COLUMN_NAME_TITLE, obj.getName());
        values.put(COLUMN_NAME_SUBTITLE, obj.getThumb());

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(TABLE_NAME, null, values);
        return newRowId;
    }

    public ImgPackObj getObject(String id) {
        //db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                _ID,
                COLUMN_NAME_TITLE,
                COLUMN_NAME_SUBTITLE
        };

        // Filter results WHERE "title" = 'My Title'
        String selection = _ID + " = ?";
        String[] selectionArgs = { id };

        // How you want the results sorted in the resulting Cursor
        String sortOrder = COLUMN_NAME_SUBTITLE + " DESC";

        Cursor cursor = db.query(
                TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        cursor.moveToFirst();
        long itemId = cursor.getLong(
                cursor.getColumnIndexOrThrow(_ID)
        );

        ImgPackObj obj = new ImgPackObj(cursor.getString(1), cursor.getString(2));
        return obj;
    }

    public List<ImgPackObj> getAllObjects() {
        List<ImgPackObj> objectList = new ArrayList<ImgPackObj>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        //SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ImgPackObj obj = new ImgPackObj();
                obj.setId(cursor.getString(0));
                obj.setName(cursor.getString(1));
                obj.setThumb(cursor.getString(2));
                // Adding contact to list
                objectList.add(obj);
            } while (cursor.moveToNext());
        }

        db.close();
        // return contact list
        return objectList;
    }

    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    public void delete() {
        // Define 'where' part of query.
        String selection = COLUMN_NAME_TITLE + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { "MyTitle" };
        // Issue SQL statement.
        db.delete(TABLE_NAME, selection, selectionArgs);
    }

    public void deleteObject(ImgPackObj obj) {
        db.delete(TABLE_NAME, _ID + " = ?",
                new String[] { String.valueOf(obj.getId()) });
    }

    public void update(String title) {
        // New value for one column
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_TITLE, title);

        // Which row to update, based on the title
        String selection = COLUMN_NAME_TITLE + " LIKE ?";
        String[] selectionArgs = { "MyTitle" };

        int count = db.update(
                TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    public int updateObject(ImgPackObj obj) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_TITLE, obj.getName());
        values.put(COLUMN_NAME_SUBTITLE, obj.getThumb());

        // updating row
        return db.update(TABLE_NAME, values, _ID + " = ?",
                new String[] { String.valueOf(obj.getId()) });
    }
}
