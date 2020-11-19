package com.example.commondatabasedemo.greendao;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.commondatabasedemo.R;

import java.util.ArrayList;
import java.util.List;

public class GreenDaoActivity extends AppCompatActivity {

    private GreenDaoAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room_layout);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        adapter = new GreenDaoAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        refreshAdapter();
        findViewById(R.id.insert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GreenDaoUser greenDaoUser1 = new GreenDaoUser("ming", "123456");
                GreenDaoUser greenDaoUser2 = new GreenDaoUser("hong", "567890");
                List<GreenDaoUser> list = new ArrayList<>();
                list.add(greenDaoUser1);
                list.add(greenDaoUser2);
                GreenDaoManager.getInstance().insertUsers(list);
                refreshAdapter();
            }
        });

        findViewById(R.id.update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<GreenDaoUser> list = GreenDaoManager.getInstance().getGreenDaoUserDao().queryBuilder().where(GreenDaoUserDao.Properties.Name.eq("ming")).list();
                if (list != null) {
                    for (GreenDaoUser user : list) {
                        user.setNumber("166377");
                    }
                    GreenDaoManager.getInstance().updateUsers(list);
                }
                refreshAdapter();
            }
        });

        findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GreenDaoManager.getInstance().deleteAllUsers();
                refreshAdapter();
            }
        });
    }

    public void refreshAdapter() {
        adapter.setUsers(GreenDaoManager.getInstance().findAllUsers());
        adapter.notifyDataSetChanged();
    }
}
