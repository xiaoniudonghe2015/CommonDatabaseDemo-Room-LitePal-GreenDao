package com.example.commondatabasedemo.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RoomUserDao {
    //@Insert(onConflict = OnConflictStrategy.REPLACE)
    @Insert
    void insertUsers(RoomUser... users);

    @Update
    void updateUsers(RoomUser... users);

    @Delete
    void deleteUsers(RoomUser... users);

    @Query("DELETE FROM ROOMUSER")
    void deleteAllUsers();

    @Query("SELECT * FROM ROOMUSER ORDER BY ID DESC")
    LiveData<List<RoomUser>> findAllUSerInfo();


    @Query("SELECT * FROM ROOMUSER WHERE id IN (:userIds)")
    LiveData<List<RoomUser>> findAllUSerInfo(int[] userIds);

    @Query("SELECT * FROM ROOMUSER WHERE name LIKE :name")
    RoomUser findUserByName(String name);
}
