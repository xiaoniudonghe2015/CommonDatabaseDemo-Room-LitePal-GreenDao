package com.example.commondatabasedemo.room;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.commondatabasedemo.DemoApplication;
import com.example.commondatabasedemo.R;

import java.util.List;

public class RoomActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room_layout);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RoomAdapter adapter = new RoomAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        RoomUserViewModel roomUserViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(this.getApplication())).get(RoomUserViewModel.class);
        roomUserViewModel.getAllWordsLive().observe(this, new Observer<List<RoomUser>>() {
            @Override
            public void onChanged(List<RoomUser> roomUsers) {
                adapter.setRoomUsers(roomUsers);
                adapter.notifyDataSetChanged();
            }
        });
        findViewById(R.id.insert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RoomUser roomUser1 = new RoomUser("ming", "123456");
                RoomUser roomUser2 = new RoomUser("hong", "678910");
                roomUserViewModel.insertUsers(roomUser1, roomUser2);
            }
        });

        findViewById(R.id.update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppExecutors executors = DemoApplication.getApplication().getAppExecutors();
                executors.diskIO().execute(() -> {
                    RoomUser roomUser = roomUserViewModel.findUserByName("ming");
                    if (roomUser != null) {
                        roomUser.number = "update";
                        roomUserViewModel.updateUsers(roomUser);
                    }
                });
            }
        });

        findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roomUserViewModel.deleteAllUsers();
            }
        });
    }
}
