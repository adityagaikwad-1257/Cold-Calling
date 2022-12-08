package com.adi.coldcall.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.adi.coldcall.models.Call;

@Database(entities = {Call.class}, version = 1)
public abstract class CallsDatabase extends RoomDatabase {
    public abstract CallDao callDao();

    private static CallsDatabase database;

    public static synchronized CallsDatabase getDatabase(Context context){
        if (database == null){
            database = Room.databaseBuilder(context.getApplicationContext(),
                    CallsDatabase.class, "CallDb")
                    .build();
        }

        return database;
    }
}
