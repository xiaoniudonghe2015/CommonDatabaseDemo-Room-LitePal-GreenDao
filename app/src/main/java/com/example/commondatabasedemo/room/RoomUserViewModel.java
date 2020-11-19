package com.example.commondatabasedemo.room;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class RoomUserViewModel extends AndroidViewModel {
    private RoomUserRepository roomUserRepository;

    public RoomUserViewModel(@NonNull Application application) {
        super(application);
        roomUserRepository = new RoomUserRepository(application);
    }

    LiveData<List<RoomUser>> getAllWordsLive() {
        return roomUserRepository.getAllUSerInfo();
    }

    RoomUser findUserByName(String name) {
        return roomUserRepository.findUserByName(name);
    }

    void insertUsers(RoomUser... users) {
        roomUserRepository.insertUsers(users);
    }

    void deleteAllUsers() {
        roomUserRepository.deleteAllUsers();
    }

    void updateUsers(RoomUser... users) {
        roomUserRepository.updateUsers(users);
    }

}
