package com.example.alex.wallpaperapp.model;

public class User {
    public User(String email, String imageURL, String name) {
        this.email = email;
        this.imageURL = imageURL;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String email;
    public String imageURL;
    public String name;

    public User() {
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
