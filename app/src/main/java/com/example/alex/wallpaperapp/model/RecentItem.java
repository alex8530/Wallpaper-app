package com.example.alex.wallpaperapp.model;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "RecentItem")
public class RecentItem {
    //this class for local database !! and not using in firebase..

    //todo be sure from primary key here !!!

    @NonNull
    @PrimaryKey
    String imageUrl;
    String categoryId;
    String lastTimeVisited; //i will use this probarty to get last 10 object depend this..


    @Ignore
    public RecentItem() {
    }

    public RecentItem(String imageUrl, String categoryId, String lastTimeVisited) {
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
        this.lastTimeVisited = lastTimeVisited;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getLastTimeVisited() {
        return lastTimeVisited;
    }

    public void setLastTimeVisited(String lastTimeVisited) {
        this.lastTimeVisited = lastTimeVisited;
    }

    @Override
    public String toString() {
        return "RecentItem{" +
                "imageUrl='" + imageUrl + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", lastTimeVisited='" + lastTimeVisited + '\'' +
                '}';
    }
}
