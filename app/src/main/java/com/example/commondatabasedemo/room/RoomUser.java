package com.example.commondatabasedemo.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class RoomUser {
    @PrimaryKey(autoGenerate = true)
    public int id;
    //@ColumnInfo(name = "name")
    public String name;

    public String number;

    public String area;

    public RoomUser(String name, String number) {
        this.name = name;
        this.number = number;
    }
}
