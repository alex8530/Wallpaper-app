package com.example.alex.wallpaperapp.model;

public class WallPaperItem {
    String imageUrl;
    String categoryId;
    long numberViews;


    public WallPaperItem() {
    }

    public WallPaperItem(String imageUrl, String categoryId) {
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
    }
    public long getNumberViews() {
        return numberViews;
    }

    public void setNumberViews(long numberViews) {
        this.numberViews = numberViews;
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

    @Override
    public String toString() {
        return "WallPaperItem{" +
                "imageUrl='" + imageUrl + '\'' +
                ", categoryId='" + categoryId + '\'' +
                '}';
    }
}
