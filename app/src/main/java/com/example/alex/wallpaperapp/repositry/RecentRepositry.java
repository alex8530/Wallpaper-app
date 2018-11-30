package com.example.alex.wallpaperapp.repositry;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.example.alex.wallpaperapp.daos.RecentItemDao;
import com.example.alex.wallpaperapp.database.AppDatabase;
import com.example.alex.wallpaperapp.model.RecentItem;

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
