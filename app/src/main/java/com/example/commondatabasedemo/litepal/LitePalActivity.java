package com.example.commondatabasedemo.litepal;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.commondatabasedemo.DemoApplication;
import com.example.commondatabasedemo.R;
import com.example.commondatabasedemo.databinding.LitepalLayoutBinding;
import com.example.commondatabasedemo.room.AppExecutors;
import com.example.commondatabasedemo.room.RoomUser;

import java.util.ArrayList;
import java.util.List;


public class LitePalActivity extends AppCompatActivity {

    private LitepalLayoutBinding mBinding;
    private LitePalAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.litepal_layout);
        adapter = new LitePalAdapter();
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerView.setAdapter(adapter);
        refreshAdapter();
        mBinding.insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LitePalUser user1 = new LitePalUser("ming", "123456");
                LitePalUser user2 = new LitePalUser("hong", "678910");
                ArrayList<LitePalUser> list = new ArrayList<>();
                list.add(user1);
                list.add(user2);
                LitePalUserManager.getInstance().insertUsers(list);
                refreshAdapter();
            }
        });

        mBinding.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<LitePalUser> list = LitePalUserManager.getInstance().findUsersByName("ming");
                for (LitePalUser litePalUser : list) {
                    litePalUser.number = "166377";
                }
                LitePalUserManager.getInstance().updateUsersByName(list);
                refreshAdapter();
            }
        });

        mBinding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LitePalUserManager.getInstance().deleteAllUsers();
                refreshAdapter();
            }
        });
    }

    private void refreshAdapter() {
        adapter.setUsers(LitePalUserManager.getInstance().findAllUsers());
        adapter.notifyDataSetChanged();
    }
}
