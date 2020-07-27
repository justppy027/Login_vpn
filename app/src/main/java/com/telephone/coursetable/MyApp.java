package com.telephone.coursetable;

import android.app.Application;

import androidx.room.Room;

import com.telephone.coursetable.Database.AppDatabase;

public class MyApp extends Application {
    private static MyApp app;
    private static AppDatabase db;
    public static String[] times = {"1","2","3","4","5"};

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        db = Room.databaseBuilder(this, AppDatabase.class, "telephone-db").build();
    }

    public static MyApp getCurrentApp(){
        return app;
    }

    public static AppDatabase getCurrentAppDB(){
        return db;
    }
}
