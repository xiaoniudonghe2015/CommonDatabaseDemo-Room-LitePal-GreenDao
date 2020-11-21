package com.example.commondatabasedemo.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SQLiteUserManager {
    static SQLiteUserManager sqLiteUserManager;
    private MySQLiteOpenHelper mySQLiteOpeMnHelper;
    private SQLiteDatabase sqLiteDatabase;

    public static SQLiteUserManager getInstance(Context context) {
        if (sqLiteUserManager == null) {
            synchronized (SQLiteUserManager.class) {
                if (sqLiteUserManager == null) {
                    sqLiteUserManager = new SQLiteUserManager(context);
                }
            }
        }
        return sqLiteUserManager;
    }

    public SQLiteDatabase getSqLiteDatabase() {
        return sqLiteDatabase;
    }

    ;


    public SQLiteUserManager(Context context) {
        try {
            mySQLiteOpeMnHelper = MySQLiteOpenHelper.getInstance(context);
            sqLiteDatabase = mySQLiteOpeMnHelper.getWritableDatabase();
        } catch (Exception ex) {

        }
    }

    public long insert(String table, String nullColumnHack, ContentValues values) {
        long raw = -1;
        try {
            raw = sqLiteDatabase.insert(table, nullColumnHack, values);
        } catch (Exception ex) {
            Log.e("->SQLiteUserManager", "insert@39 --> " + ex);
        }
        return raw;
    }


    public int delete(String table, String whereClause, String[] whereArgs) {
        int raw = 0;
        try {
            raw = sqLiteDatabase.delete(table, whereClause, whereArgs);
        } catch (Exception ex) {

        }
        return raw;
    }

    public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        int raw = 0;
        try {
            raw = sqLiteDatabase.update(table, values, whereClause, whereArgs);
        } catch (Exception ex) {

        }
        return raw;
    }

    public Cursor query(String sql, String[] selectionArgs) {
        Cursor cursor = null;
        try {
            cursor = sqLiteDatabase.rawQuery(sql, selectionArgs);
        } catch (Exception ex) {

        }
        return cursor;
    }
}
