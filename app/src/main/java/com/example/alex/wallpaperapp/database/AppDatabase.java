package com.example.alex.wallpaperapp.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import com.example.alex.wallpaperapp.daos.RecentItemDao;
import com.example.alex.wallpaperapp.model.RecentItem;


/**
 * Created by NoOne on 9/29/2018.
 */
@Database(entities = {RecentItem.class }, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "recents";
    private static final String LOG_TAG ="AppDatabase" ;
    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract RecentItemDao recentItemDao();
}
