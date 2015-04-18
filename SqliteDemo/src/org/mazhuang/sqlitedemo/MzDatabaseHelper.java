package org.mazhuang.sqlitedemo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MzDatabaseHelper extends SQLiteOpenHelper {
    
    private static final String DATABASE_NAME = "test.db";
    private static final int DATABASE_VERSION = 10;
    private static final String DBINFO_TABLE_NAME = "dbinfo";
    private static final String CONTENT_TABLE_NAME = "content";
    private Context mContext;
    
    public MzDatabaseHelper(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL("CREATE TABLE " + DBINFO_TABLE_NAME + " (" + DBInfoColumns.VERSION
//                + " INTEGER PRIMARY KEY);");
//        
//        db.execSQL("CREATE TABLE " + CONTENT_TABLE_NAME + " (" + ContentColumns.ID
//                + " INTEGER PRIMARY KEY," + ContentColumns.NAME + "TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

    public static final class DBInfoColumns {
        private DBInfoColumns() {
        }
        
        public static final String VERSION = "version";
    }
    
    public static final class ContentColumns {
        private ContentColumns() {
        }
        
        public static final String ID = "id";
        public static final String NAME = "name";
    }
}
