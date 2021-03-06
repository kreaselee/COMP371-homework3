package com.example.homework3;

import android.widget.ImageView;
import android.widget.TextView;

public class Character {

    // instance variables
    private String imageUrl;
    private String name;

    // constructor
    public Character(String imageUrl, String name) {
        this.imageUrl = imageUrl;
        this.name = name;
    }

    // setters and getters for each variable
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
