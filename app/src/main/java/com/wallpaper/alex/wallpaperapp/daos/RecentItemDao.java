package com.wallpaper.alex.wallpaperapp.daos;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.wallpaper.alex.wallpaperapp.model.RecentItem;

import java.util.List;

@Dao
public interface RecentItemDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecent(RecentItem recentItem);


    @Query("SELECT * FROM RecentItem ORDER BY lastTimeVisited DESC LIMIT 10")
    LiveData<List<RecentItem>> getAllRecents();


    // todo .. add buttom for clear all recent and use this
    @Query("DELETE FROM RecentItem")
     void DeleteAllRecents();
}
