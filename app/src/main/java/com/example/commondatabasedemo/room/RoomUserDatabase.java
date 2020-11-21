package com.example.commondatabasedemo.room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {RoomUser.class}, version = 2)
public abstract class RoomUserDatabase extends RoomDatabase {
    public abstract RoomUserDao getRoomUserDao();

    private static RoomUserDatabase INSTANCE;

    public static RoomUserDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (RoomUserDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), RoomUserDatabase.class, "room_user")
                            .addMigrations(MIGRATION_1_2)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE roomuser ADD COLUMN area TEXT");
        }
    };
}
