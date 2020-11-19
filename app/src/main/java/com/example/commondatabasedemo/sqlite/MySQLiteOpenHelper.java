package com.example.commondatabasedemo.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.commondatabasedemo.greendao.MyGreenDaoDbHelper;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    static MySQLiteOpenHelper myGreenDaoDbHelper;

    public static MySQLiteOpenHelper getInstance(Context context) {
        if (myGreenDaoDbHelper == null) {
            synchronized (MySQLiteOpenHelper.class) {
                if (myGreenDaoDbHelper == null) {
                    myGreenDaoDbHelper = new MySQLiteOpenHelper(context);
                }
            }
        }
        return myGreenDaoDbHelper;
    }

    public MySQLiteOpenHelper(Context context) {
        this(context, "sqlite_user.db", null, 2);
    }

    public MySQLiteOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
                + UserTable.USER_TABLE_NAME + " ("
                + UserTable.COLUMNS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + UserTable.COLUMNS_NAME + " TEXT, "
                + UserTable.COLUMNS_NUMBER + " TEXT"
                + ")";
        Log.e("->MySQLiteOpenHelper", "onCreate@41 --> " + CREATE_TABLE);
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE user ADD COLUMN AREA TEXT");
        }
    }

    public static class UserTable {
        public static final String USER_TABLE_NAME = "user";
        public static final String COLUMNS_ID = "id";
        public static final String COLUMNS_NAME = "name";
        public static final String COLUMNS_NUMBER = "number";
    }
}
