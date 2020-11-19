package com.example.commondatabasedemo.room;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class RoomUserRepository {

    private LiveData<List<RoomUser>> allUSerInfo;
    private RoomUserDao roomUserDao;

    public RoomUserRepository(Context context) {
        RoomUserDatabase roomUserDatabase = RoomUserDatabase.getDatabase(context.getApplicationContext());
        roomUserDao = roomUserDatabase.getRoomUserDao();
        allUSerInfo = roomUserDao.findAllUSerInfo();
    }

    RoomUser findUserByName(String name) {
        return roomUserDao.findUserByName(name);
    }

    void insertUsers(RoomUser... users) {
        new InsertAsyncTask(roomUserDao).execute(users);
    }

    void deleteAllUsers() {
        new DeleteAllAsyncTask(roomUserDao).execute();
    }

    void updateUsers(RoomUser... users) {
        new UpdateAsyncTask(roomUserDao).execute(users);
    }

    LiveData<List<RoomUser>> getAllUSerInfo() {
        return allUSerInfo;
    }

    static class InsertAsyncTask extends AsyncTask<RoomUser, Void, Void> {
        private RoomUserDao userDao;

        public InsertAsyncTask(RoomUserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(RoomUser... users) {
            userDao.insertUsers(users);
            return null;
        }
    }


    static class UpdateAsyncTask extends AsyncTask<RoomUser, Void, Void> {
        private RoomUserDao userDao;

        public UpdateAsyncTask(RoomUserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(RoomUser... users) {
            userDao.updateUsers(users);
            return null;
        }
    }


    static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private RoomUserDao userDao;

        public DeleteAllAsyncTask(RoomUserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            userDao.deleteAllUsers();
            return null;
        }
    }

}
