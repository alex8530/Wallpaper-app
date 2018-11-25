package com.example.alex.wallpaperapp.model;

public class Category {

    private String name;
    private String imageLink;

    public Category() {
    }

    public Category(String imageLink, String name) {
        this.name = name;
        this.imageLink = imageLink;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                ", imageLink='" + imageLink + '\'' +
                '}';
    }
}
