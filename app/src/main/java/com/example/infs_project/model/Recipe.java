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
}
