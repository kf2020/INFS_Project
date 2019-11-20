package com.example.infs_project.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Recipe {
    @PrimaryKey
    long id;

    String title;
    String image; // URL

    float spoonacularScore;

    int calories;
    int readyInMinutes;
    String summary;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public float getSpoonacularScore() {
        return spoonacularScore;
    }

    public void setSpoonacularScore(float spoonacularScore) {
        this.spoonacularScore = spoonacularScore;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
