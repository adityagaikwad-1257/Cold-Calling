package com.adi.coldcall;

import android.app.Application;

import com.adi.coldcall.room.CallsDatabase;

public class ColdCall extends Application {
    private static CallsDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        database = CallsDatabase.getDatabase(getApplicationContext());
    }

    public static CallsDatabase getDatabase() {
        return database;
    }
}
