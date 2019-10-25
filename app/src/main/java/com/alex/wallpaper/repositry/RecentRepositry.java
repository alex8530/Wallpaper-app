package com.alex.wallpaper.repositry;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.alex.wallpaper.daos.RecentItemDao;
import com.alex.wallpaper.database.AppDatabase;
import com.alex.wallpaper.model.RecentItem;

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
