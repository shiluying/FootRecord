package com.shiluying.footrecord.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    public final static String RECORD_TABLE_NAME = "Record";
public final static String RECORD_ID = "recordid";
    public final static String RECORD_TITLE = "recordtitle";
    public final static String RECORD_CONTENT = "recordcontent";
    public final static String RECORD_SITE = "recordsite";
    public static final String[] RECORD_TABLE_COLUMNS = {
            RECORD_ID,
            RECORD_TITLE,
            RECORD_CONTENT,
            RECORD_SITE
    };

    public DBHelper(Context context) {
        super(context, "Database.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            String sql = "CREATE TABLE IF NOT EXISTS " + RECORD_TABLE_NAME + " (" +
                    RECORD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    RECORD_TITLE + " TEXT NOT NULL,"+
                    RECORD_CONTENT + " TEXT NOT NULL,"+
                    RECORD_SITE + " TEXT NOT NULL)";
            db.execSQL(sql);
            Log.i("TEST","初始化数据库成功!!");
        }catch (Exception e) {
            Log.i("TEST","初始化数据库失败!!"+e);
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
    public boolean deleteDatabase(Context context) {
        return context.deleteDatabase("Database.db");
    }

}
