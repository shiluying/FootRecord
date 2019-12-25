package com.shiluying.footrecord.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class RecordProvider extends ContentProvider {
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    public static final String AUTHORITY = "com.shiluying.footrecord.provider";
    static final String URL = "content://" + AUTHORITY + "/"+DBHelper.RECORD_TABLE_NAME;
    private static UriMatcher uriMatcher;
    public static final Uri CONTENT_URI = Uri.parse(URL);
    public static final int RECORD = 1;
    public static final int RECORD_ID = 2;

    //创建静态代码块
    static {
        //实例化UriMatcher对象
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        //可以实现匹配URI的功能
        uriMatcher.addURI(AUTHORITY, DBHelper.RECORD_TABLE_NAME, RECORD);
        uriMatcher.addURI(AUTHORITY, DBHelper.RECORD_TABLE_NAME+"/#", RECORD_ID);
    }
    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        db = dbHelper.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        if (sortOrder == null || sortOrder == ""){
            sortOrder = DBHelper.RECORD_TITLE;
        }
        Cursor c = db.query(DBHelper.RECORD_TABLE_NAME,projection, selection, selectionArgs,null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long rowID=db.insert(DBHelper.RECORD_TABLE_NAME, null, values);
        if (rowID > 0)
        {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
       int id= db.delete(DBHelper.RECORD_TABLE_NAME,selection,selectionArgs);
        return id;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int id=db.update(DBHelper.RECORD_TABLE_NAME,values,selection,selectionArgs);
        return id;
    }
}
