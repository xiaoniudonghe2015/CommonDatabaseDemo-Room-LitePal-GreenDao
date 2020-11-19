package com.example.commondatabasedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.commondatabasedemo.greendao.GreenDaoActivity;
import com.example.commondatabasedemo.litepal.LitePalActivity;
import com.example.commondatabasedemo.room.RoomActivity;
import com.example.commondatabasedemo.sqlite.SQLiteActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.sqlite).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, SQLiteActivity.class)));
        findViewById(R.id.litepal).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, LitePalActivity.class)));
        findViewById(R.id.greendao).setOnClickListener(v -> { startActivity(new Intent(MainActivity.this, GreenDaoActivity.class)); });
        findViewById(R.id.room).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, RoomActivity.class)));
    }
}