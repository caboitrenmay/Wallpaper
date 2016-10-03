package com.fsoc.wallpaper.db;

import android.provider.BaseColumns;

/**
 * Created by linhcaro on 9/30/2016.
 */

public class FeedReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private FeedReaderContract() {}

    /* Inner class that defines the table contents */
    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_SUBTITLE = "subtitle";

        public static final String COLUMN_NAME_PACK = "package";
    }
}
