package com.example.infs_project.model;

import java.util.List;

public class ProfileInfo {

    private double id = Math.random();

    private int totalPoints;
    private int totalQuizzes;

    private List<Recipe> favourites;

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public int getTotalQuizzes() {
        return totalQuizzes;
    }

    public void setTotalQuizzes(int totalQuizzes) {
        this.totalQuizzes = totalQuizzes;
    }

    public List<Recipe> getFavourites() {
        return favourites;
    }

    public void setFavourites(List<Recipe> favourites) {
        this.favourites = favourites;
    }
}
