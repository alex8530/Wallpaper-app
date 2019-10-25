package com.alex.wallpaper.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.alex.wallpaper.model.RecentItem;
import com.alex.wallpaper.repositry.RecentRepositry;

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
