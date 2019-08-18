package com.wallpaper.alex.wallpaperapp.repositry;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.wallpaper.alex.wallpaperapp.daos.RecentItemDao;
import com.wallpaper.alex.wallpaperapp.database.AppDatabase;
import com.wallpaper.alex.wallpaperapp.model.RecentItem;

import java.util.List;

public class RecentRepositry  {
    private final RecentItemDao dao;
    Application application;


    public RecentRepositry(Application application) {
        this.application=application;
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        dao = appDatabase.recentItemDao();

    }

    public LiveData<List<RecentItem>> getAllRecentsLiveData() {
        return dao.getAllRecents();
    }

}
