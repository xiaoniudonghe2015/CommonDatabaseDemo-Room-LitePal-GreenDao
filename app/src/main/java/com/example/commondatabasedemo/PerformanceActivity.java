package com.example.commondatabasedemo;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.example.commondatabasedemo.greendao.GreenDaoManager;
import com.example.commondatabasedemo.greendao.GreenDaoUser;
import com.example.commondatabasedemo.greendao.GreenDaoUserDao;
import com.example.commondatabasedemo.litepal.LitePalUser;
import com.example.commondatabasedemo.litepal.LitePalUserManager;
import com.example.commondatabasedemo.room.AppExecutors;
import com.example.commondatabasedemo.room.RoomUser;
import com.example.commondatabasedemo.room.RoomUserDao;
import com.example.commondatabasedemo.room.RoomUserDatabase;
import com.example.commondatabasedemo.sqlite.MySQLiteOpenHelper;
import com.example.commondatabasedemo.sqlite.SQLiteActivity;
import com.example.commondatabasedemo.sqlite.SQLiteUser;
import com.example.commondatabasedemo.sqlite.SQLiteUserManager;

import org.greenrobot.greendao.database.Database;
import org.litepal.LitePal;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class PerformanceActivity extends AppCompatActivity {
    private static final int COUNT = 10000;
    private RadioGroup radioGroup;
    private ProgressBar sqliteInsertProgressBar, greenDaoInsertProgressBar, litepalInsertProgressBar, roomInsertProgressBar;
    private ProgressBar sqliteQueryProgressBar, greenDaoQueryProgressBar, litepalQueryProgressBar, roomQueryProgressBar;
    private ProgressBar sqliteDeleteProgressBar, greenDaoDeleteProgressBar, litepalDeleteProgressBar, roomDeleteProgressBar;
    private TextView sqliteInsertTime, greendaoInsertTime, litepalInsertTime, roomInsertTime;
    private TextView sqliteQueryTime, greendaoQueryTime, litepalQueryTime, roomQueryTime;
    private TextView sqliteDeleteTime, greendaoDeleteTime, litepalDeleteTime, roomDeleteTime;
    private long sqliteInsertStartTime, greendaoInsertStartTime, litepalInsertStartTime, roomInsertStartTime;
    private long sqliteQueryStartTime, greendaoQueryStartTime, litepalQueryStartTime, roomQueryStartTime;
    private long sqliteDeleteStartTime, greendaoDeleteStartTime, litepalDeleteStartTime, roomDeleteStartTime;
    private Switch transactionSwitch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.performance_layout);
        radioGroup = findViewById(R.id.rg);
        sqliteInsertProgressBar = findViewById(R.id.sqlite_insert_progress);
        sqliteInsertTime = findViewById(R.id.sqlite_insert_time);
        greenDaoInsertProgressBar = findViewById(R.id.greendao_insert_progress);
        greendaoInsertTime = findViewById(R.id.greendao_insert_time);
        litepalInsertProgressBar = findViewById(R.id.litepal_insert_progress);
        litepalInsertTime = findViewById(R.id.litepal_insert_time);
        roomInsertProgressBar = findViewById(R.id.room_insert_progress);
        roomInsertTime = findViewById(R.id.room_insert_time);

        sqliteQueryProgressBar = findViewById(R.id.sqlite_query_progress);
        sqliteQueryTime = findViewById(R.id.sqlite_query_time);
        greenDaoQueryProgressBar = findViewById(R.id.greendao_query_progress);
        greendaoQueryTime = findViewById(R.id.greendao_query_time);
        litepalQueryProgressBar = findViewById(R.id.litepal_query_progress);
        litepalQueryTime = findViewById(R.id.litepal_query_time);
        roomQueryProgressBar = findViewById(R.id.room_query_progress);
        roomQueryTime = findViewById(R.id.room_query_time);
        sqliteQueryProgressBar.setVisibility(View.INVISIBLE);
        greenDaoQueryProgressBar.setVisibility(View.INVISIBLE);
        litepalQueryProgressBar.setVisibility(View.INVISIBLE);
        roomQueryProgressBar.setVisibility(View.INVISIBLE);

        sqliteDeleteProgressBar = findViewById(R.id.sqlite_delete_progress);
        sqliteDeleteTime = findViewById(R.id.sqlite_delete_time);
        greenDaoDeleteProgressBar = findViewById(R.id.greendao_delete_progress);
        greendaoDeleteTime = findViewById(R.id.greendao_delete_time);
        litepalDeleteProgressBar = findViewById(R.id.litepal_delete_progress);
        litepalDeleteTime = findViewById(R.id.litepal_delete_time);
        roomDeleteProgressBar = findViewById(R.id.room_delete_progress);
        roomDeleteTime = findViewById(R.id.room_delete_time);
        findViewById(R.id.sqlite_delete_layout).setVisibility(View.GONE);
        findViewById(R.id.greendao_delete_layout).setVisibility(View.GONE);
        findViewById(R.id.litepal_delete_layout).setVisibility(View.GONE);
        findViewById(R.id.room_delete_layout).setVisibility(View.GONE);

        sqliteInsertProgressBar.setMax(COUNT);
        greenDaoInsertProgressBar.setMax(COUNT);
        litepalInsertProgressBar.setMax(COUNT);
        roomInsertProgressBar.setMax(COUNT);

//        sqliteQueryProgressBar.setMax(COUNT);
//        greenDaoQueryProgressBar.setMax(COUNT);
//        litepalQueryProgressBar.setMax(COUNT);
//        roomQueryProgressBar.setMax(COUNT);

        sqliteDeleteProgressBar.setMax(COUNT);
        greenDaoDeleteProgressBar.setMax(COUNT);
        litepalDeleteProgressBar.setMax(COUNT);
        roomDeleteProgressBar.setMax(COUNT);

        transactionSwitch = findViewById(R.id.transaction_switch);
        findViewById(R.id.insert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioGroup.getCheckedRadioButtonId() == R.id.sqlite_rb) {
                    if (transactionSwitch.isChecked()) {
                        AppExecutors executors = new AppExecutors();
                        executors.diskIO().execute(() -> {
//                        Log.e("->PerformanceActivity", "sqlite start insert ");
                            sqliteInsertStartTime = System.currentTimeMillis();
                            SQLiteDatabase sqLiteDatabase = SQLiteUserManager.getInstance(PerformanceActivity.this).getSqLiteDatabase();
                            sqLiteDatabase.beginTransaction();
                            try {
                                for (int i = 0; i < COUNT; i++) {
                                    ContentValues values1 = new ContentValues();
                                    values1.put("name", "ming" + i);
                                    values1.put("number", "123456");
                                    SQLiteUserManager.getInstance(PerformanceActivity.this).insert(MySQLiteOpenHelper.UserTable.USER_TABLE_NAME, null, values1);
//                            Log.e("->PerformanceActivity", "sqlite insert --> " + i);
                                    sqliteInsertProgressBar.setProgress(i);
                                    if (i == COUNT - 1) {
                                        sqLiteDatabase.setTransactionSuccessful();
                                    }
                                }
                            } catch (Exception ex) {

                            } finally {
                                sqLiteDatabase.endTransaction();
                                executors.mainThread().execute(() -> {
                                    float interval = ((float) (System.currentTimeMillis() - sqliteInsertStartTime)) / 1000;
                                    sqliteInsertTime.setText("insert->" + interval + "");
                                    Log.e("->PerformanceActivity", "sqlite insert total time --> " + interval);
                                });
                            }
//                        Log.e("->PerformanceActivity", "sqlite end insert ");
                        });
                    } else {
                        AppExecutors executors = new AppExecutors();
                        executors.diskIO().execute(() -> {
//                        Log.e("->PerformanceActivity", "sqlite start insert ");
                            sqliteInsertStartTime = System.currentTimeMillis();
                            for (int i = 0; i < COUNT; i++) {
                                ContentValues values1 = new ContentValues();
                                values1.put("name", "ming" + i);
                                values1.put("number", "123456");
                                SQLiteUserManager.getInstance(PerformanceActivity.this).insert(MySQLiteOpenHelper.UserTable.USER_TABLE_NAME, null, values1);
//                            Log.e("->PerformanceActivity", "sqlite insert --> " + i);
                                sqliteInsertProgressBar.setProgress(i);
                                if (i == COUNT - 1) {
                                    executors.mainThread().execute(() -> {
                                        float interval = ((float) (System.currentTimeMillis() - sqliteInsertStartTime)) / 1000;
                                        sqliteInsertTime.setText("insert->" + interval + "");
                                        Log.e("->PerformanceActivity", "sqlite insert total time --> " + interval);
                                    });
                                }
                            }
//                        Log.e("->PerformanceActivity", "sqlite end insert ");
                        });
                    }

                } else if (radioGroup.getCheckedRadioButtonId() == R.id.greendao_rb) {
                    if (transactionSwitch.isChecked()) {
                        AppExecutors executors = new AppExecutors();
                        executors.diskIO().execute(() -> {
//                        Log.e("->PerformanceActivity", "greendao start insert ");
                            greendaoInsertStartTime = System.currentTimeMillis();
                            GreenDaoUserDao greenDaoUserDao = GreenDaoManager.getInstance().getGreenDaoUserDao();
                            ArrayList<GreenDaoUser> list = new ArrayList<>();
                            for (int i = 0; i < COUNT; i++) {
                                GreenDaoUser greenDaoUser = new GreenDaoUser("ming" + i, "123456");
                                list.add(greenDaoUser);
                            }
                            greenDaoUserDao.insertInTx(list);
                            greenDaoInsertProgressBar.setProgress(COUNT);
                            executors.mainThread().execute(() -> {
                                float interval = ((float) (System.currentTimeMillis() - greendaoInsertStartTime)) / 1000;
                                greendaoInsertTime.setText("insert->" + interval + "");
                                Log.e("->PerformanceActivity", "greendao insert total time --> " + interval);
                            });
//                        Log.e("->PerformanceActivity", "greendao end insert ");
                        });
                    } else {
                        AppExecutors executors = new AppExecutors();
                        executors.diskIO().execute(() -> {
//                        Log.e("->PerformanceActivity", "greendao start insert ");
                            greendaoInsertStartTime = System.currentTimeMillis();
                            GreenDaoUserDao greenDaoUserDao = GreenDaoManager.getInstance().getGreenDaoUserDao();
                            for (int i = 0; i < COUNT; i++) {
                                GreenDaoUser greenDaoUser = new GreenDaoUser("ming" + i, "123456");
                                greenDaoUserDao.insert(greenDaoUser);
//                            Log.e("->PerformanceActivity", "greendao insert --> " + i);
                                greenDaoInsertProgressBar.setProgress(i);
                                if (i == COUNT - 1) {
                                    executors.mainThread().execute(() -> {
                                        float interval = ((float) (System.currentTimeMillis() - greendaoInsertStartTime)) / 1000;
                                        greendaoInsertTime.setText("insert->" + interval + "");
                                        Log.e("->PerformanceActivity", "greendao insert total time --> " + interval);
                                    });
                                }
                            }
//                        Log.e("->PerformanceActivity", "greendao end insert ");
                        });
                    }
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.litepal_rb) {
                    if (transactionSwitch.isChecked()) {
                        AppExecutors executors = new AppExecutors();
                        executors.diskIO().execute(() -> {
                            try {
                                LitePal.beginTransaction();
//                        Log.e("->PerformanceActivity", "litepal start insert ");
                                litepalInsertStartTime = System.currentTimeMillis();
                                for (int i = 0; i < COUNT; i++) {
                                    LitePalUser user2 = new LitePalUser("ming" + i, "123456");
                                    user2.save();
//                            Log.e("->PerformanceActivity", "litepal insert --> " + i);
                                    litepalInsertProgressBar.setProgress(i);
                                    if (i == COUNT - 1) {
                                        LitePal.setTransactionSuccessful();
                                    }
                                }
//                        Log.e("->PerformanceActivity", "litepal end insert ");
                            } catch (Exception ex) {

                            } finally {
                                LitePal.endTransaction();
                                executors.mainThread().execute(() -> {
                                    float interval = ((float) (System.currentTimeMillis() - litepalInsertStartTime)) / 1000;
                                    litepalInsertTime.setText("insert->" + interval + "");
                                    Log.e("->PerformanceActivity", "litepal insert total time --> " + interval);
                                });
                            }
                        });
                    } else {
                        AppExecutors executors = new AppExecutors();
                        executors.diskIO().execute(() -> {
//                        Log.e("->PerformanceActivity", "litepal start insert ");
                            litepalInsertStartTime = System.currentTimeMillis();
                            for (int i = 0; i < COUNT; i++) {
                                LitePalUser user2 = new LitePalUser("ming" + i, "123456");
                                user2.save();
//                            Log.e("->PerformanceActivity", "litepal insert --> " + i);
                                litepalInsertProgressBar.setProgress(i);
                                if (i == COUNT - 1) {
                                    executors.mainThread().execute(() -> {
                                        float interval = ((float) (System.currentTimeMillis() - litepalInsertStartTime)) / 1000;
                                        litepalInsertTime.setText("insert->" + interval + "");
                                        Log.e("->PerformanceActivity", "litepal insert total time --> " + interval);
                                    });
                                }
                            }
//                        Log.e("->PerformanceActivity", "litepal end insert ");
                        });
                    }
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.room_rb) {
                    if (transactionSwitch.isChecked()) {
                        AppExecutors executors = new AppExecutors();
                        executors.diskIO().execute(() -> {
                            Log.e("->PerformanceActivity", "room start insert ");
                            roomInsertStartTime = System.currentTimeMillis();
                            RoomUserDatabase roomUserDatabase = RoomUserDatabase.getDatabase(getApplicationContext());
                            RoomUserDao roomUserDao = roomUserDatabase.getRoomUserDao();
                            ArrayList<RoomUser> list = new ArrayList<>();
                            for (int i = 0; i < COUNT; i++) {
                                RoomUser roomUser = new RoomUser("ming" + i, "123456");
                                list.add(roomUser);
                            }
                            roomUserDao.insertUsersTransaction(list);
                            roomInsertProgressBar.setProgress(COUNT);
                            executors.mainThread().execute(() -> {
                                float interval = ((float) (System.currentTimeMillis() - roomInsertStartTime)) / 1000;
                                roomInsertTime.setText("insert->" + interval + "");
                                Log.e("->PerformanceActivity", "room insert total time --> " + interval);
                            });
//                        Log.e("->PerformanceActivity", "room end insert ");
                        });
                    } else {
//                        AppExecutors executors = new AppExecutors();
//                        executors.diskIO().execute(() -> {
//                            Log.e("->PerformanceActivity", "room start insert ");
//                            roomInsertStartTime = System.currentTimeMillis();
//                            RoomUserDatabase roomUserDatabase = RoomUserDatabase.getDatabase(getApplicationContext());
//                            RoomUserDao roomUserDao = roomUserDatabase.getRoomUserDao();
//                            for (int i = 0; i < COUNT; i++) {
//                                RoomUser roomUser = new RoomUser("ming" + i, "123456");
//                                roomUserDao.insertUsers(roomUser);
//                                roomInsertProgressBar.setProgress(i);
//                                if (i == COUNT - 1) {
//                                    executors.mainThread().execute(() -> {
//                                        float interval = ((float) (System.currentTimeMillis() - roomInsertStartTime)) / 1000;
//                                        roomInsertTime.setText("insert->" + interval + "");
//                                        Log.e("->PerformanceActivity", "room insert total time --> " + interval);
//                                    });
//                                }
//                            }
////                        Log.e("->PerformanceActivity", "room end insert ");
//                        });

                        AppExecutors executors = new AppExecutors();
                        executors.diskIO().execute(() -> {
                            Log.e("->PerformanceActivity", "room start insert ");
                            roomInsertStartTime = System.currentTimeMillis();
                            RoomUserDatabase roomUserDatabase = RoomUserDatabase.getDatabase(getApplicationContext());
                            RoomUserDao roomUserDao = roomUserDatabase.getRoomUserDao();
                            ArrayList<RoomUser> list = new ArrayList<>();
                            for (int i = 0; i < COUNT; i++) {
                                RoomUser roomUser = new RoomUser("ming" + i, "123456");
                                list.add(roomUser);
                            }
                            roomUserDao.insertAllUsers(list);
                            roomInsertProgressBar.setProgress(COUNT);
                            executors.mainThread().execute(() -> {
                                float interval = ((float) (System.currentTimeMillis() - roomInsertStartTime)) / 1000;
                                roomInsertTime.setText("insert->" + interval + "");
                                Log.e("->PerformanceActivity", "room insert total time --> " + interval);
                            });
//                        Log.e("->PerformanceActivity", "room end insert ");
                        });
                    }
                }
            }
        });

        findViewById(R.id.query).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioGroup.getCheckedRadioButtonId() == R.id.sqlite_rb) {
                    AppExecutors executors = new AppExecutors();
                    executors.diskIO().execute(() -> {
                        sqliteQueryStartTime = System.currentTimeMillis();
                        Cursor cursor = SQLiteUserManager.getInstance(PerformanceActivity.this).query("select * from user", null);
                        List<SQLiteUser> list = initData(cursor);
                        executors.mainThread().execute(() -> {
                            Log.e("->PerformanceActivity", "sqlite count --> " + list.size());
                            float interval = ((float) (System.currentTimeMillis() - sqliteQueryStartTime)) / 1000;
                            sqliteQueryTime.setText("query->" + interval + "");
                            Log.e("->PerformanceActivity", "sqlite query total time --> " + interval);
                        });
                    });
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.greendao_rb) {
                    AppExecutors executors = new AppExecutors();
                    executors.diskIO().execute(() -> {
                        greendaoQueryStartTime = System.currentTimeMillis();
                        List<GreenDaoUser> list = GreenDaoManager.getInstance().findAllUsers();
                        executors.mainThread().execute(() -> {
                            Log.e("->PerformanceActivity", "greendao count --> " + list.size());
                            float interval = ((float) (System.currentTimeMillis() - greendaoQueryStartTime)) / 1000;
                            greendaoQueryTime.setText("query->" + interval + "");
                            Log.e("->PerformanceActivity", "greendao query total time --> " + interval);
                        });
                    });
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.litepal_rb) {
                    AppExecutors executors = new AppExecutors();
                    executors.diskIO().execute(() -> {
                        litepalQueryStartTime = System.currentTimeMillis();
                        List<LitePalUser> list = LitePalUserManager.getInstance().findAllUsers();
                        executors.mainThread().execute(() -> {
                            Log.e("->PerformanceActivity", "litepal count --> " + list.size());
                            float interval = ((float) (System.currentTimeMillis() - litepalQueryStartTime)) / 1000;
                            litepalQueryTime.setText("query->" + interval + "");
                            Log.e("->PerformanceActivity", "litepal query total time --> " + interval);
                        });
                    });
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.room_rb) {
                    AppExecutors executors = new AppExecutors();
                    executors.diskIO().execute(() -> {
                        roomQueryStartTime = System.currentTimeMillis();
                        RoomUserDatabase roomUserDatabase = RoomUserDatabase.getDatabase(getApplicationContext());
                        RoomUserDao roomUserDao = roomUserDatabase.getRoomUserDao();
                        List<RoomUser> list = roomUserDao.findAllUSerInfo1();
                        executors.mainThread().execute(() -> {
                            Log.e("->PerformanceActivity", "room count --> " + list.size());
                            float interval = ((float) (System.currentTimeMillis() - roomQueryStartTime)) / 1000;
                            roomQueryTime.setText("query->" + interval + "");
                            Log.e("->PerformanceActivity", "room query total time --> " + interval);
                        });
                    });
                }
            }
        });

        findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioGroup.getCheckedRadioButtonId() == R.id.sqlite_rb) {

                } else if (radioGroup.getCheckedRadioButtonId() == R.id.greendao_rb) {

                } else if (radioGroup.getCheckedRadioButtonId() == R.id.litepal_rb) {

                } else if (radioGroup.getCheckedRadioButtonId() == R.id.room_rb) {

                }
            }
        });
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
