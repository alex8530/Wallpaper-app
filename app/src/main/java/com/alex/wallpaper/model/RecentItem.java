package com.alex.wallpaper.model;


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



    /*
     //we add this key .. because  if we are in ViewWallPAper activity and write
                // method there to increase number of views..
                // so we need referance to
                // get that child ..and if we are here in this class ..
                // and go to View activity .. we must set the key value to use it for referance child
                //so we must store the key in  RecentItem to be able to retrive key
     */
    String key;

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


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
