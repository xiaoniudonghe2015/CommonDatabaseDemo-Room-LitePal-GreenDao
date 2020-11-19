package com.example.commondatabasedemo.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.commondatabasedemo.R;
import com.example.commondatabasedemo.greendao.GreenDaoAdapter;
import com.example.commondatabasedemo.greendao.GreenDaoManager;
import com.example.commondatabasedemo.greendao.GreenDaoUser;
import com.example.commondatabasedemo.greendao.GreenDaoUserDao;

import java.util.ArrayList;
import java.util.List;

public class SQLiteActivity extends AppCompatActivity {

    private SQLiteAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room_layout);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        adapter = new SQLiteAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        refreshAdapter();
        findViewById(R.id.insert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values1 = new ContentValues();
                values1.put("name", "ming");
                values1.put("number", "123456");
                SQLiteUserManager.getInstance(SQLiteActivity.this).insert(MySQLiteOpenHelper.UserTable.USER_TABLE_NAME, null, values1);
                ContentValues values2 = new ContentValues();
                values2.put("name", "hong");
                values2.put("number", "567890");
                SQLiteUserManager.getInstance(SQLiteActivity.this).insert(MySQLiteOpenHelper.UserTable.USER_TABLE_NAME, null, values2);
                refreshAdapter();
            }
        });

        findViewById(R.id.update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put("number", "166377");
                SQLiteUserManager.getInstance(SQLiteActivity.this).update(MySQLiteOpenHelper.UserTable.USER_TABLE_NAME, values, "name =?", new String[]{"ming"});
                refreshAdapter();
            }
        });

        findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteUserManager.getInstance(SQLiteActivity.this).delete(MySQLiteOpenHelper.UserTable.USER_TABLE_NAME, null, null);
                refreshAdapter();
            }
        });
    }

    public void refreshAdapter() {
        Cursor cursor = SQLiteUserManager.getInstance(this).query("select * from user order by id desc", null);
        adapter.setUsers(initData(cursor));
        adapter.notifyDataSetChanged();
    }

    List<SQLiteUser> initData(Cursor cursor) {
        List<SQLiteUser> list = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                SQLiteUser user = new SQLiteUser();
                user.id = cursor.getInt(cursor.getColumnIndexOrThrow(MySQLiteOpenHelper.UserTable.COLUMNS_ID));
                user.name = cursor.getString(cursor.getColumnIndexOrThrow(MySQLiteOpenHelper.UserTable.COLUMNS_NAME));
                user.number = cursor.getString(cursor.getColumnIndexOrThrow(MySQLiteOpenHelper.UserTable.COLUMNS_NUMBER));
                list.add(user);
            }
        }
        return list;
    }
}
