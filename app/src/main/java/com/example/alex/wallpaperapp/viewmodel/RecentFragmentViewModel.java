package com.example.alex.wallpaperapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.alex.wallpaperapp.database.AppDatabase;
import com.example.alex.wallpaperapp.model.RecentItem;
import com.example.alex.wallpaperapp.repositry.RecentRepositry;

import java.util.List;

public class RecentFragmentViewModel  extends AndroidViewModel {

    RecentRepositry repository;


    public RecentFragmentViewModel(@NonNull Application application) {
        super(application);

        repository = new RecentRepositry(application);

    }

    public LiveData<List<RecentItem>> getAllRecents() {
        return repository.getAllRecentsLiveData();
    }
}
