package com.wallpaper.alex.wallpaperapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.wallpaper.alex.wallpaperapp.model.RecentItem;
import com.wallpaper.alex.wallpaperapp.repositry.RecentRepositry;

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
