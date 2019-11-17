package com.example.infs_project.model;

import java.util.List;

public class Quiz {
    int numQuestions;
    int currQuestionNum;
    int currScore;
    List<Recipe> recipes;

    public Quiz() {
        numQuestions = 5;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    public int getCurrQuestionNum() {
        return currQuestionNum;
    }

    public void setCurrQuestionNum(int currQuestionNum) {
        this.currQuestionNum = currQuestionNum;
    }

    public int getCurrScore() {
        return currScore;
    }

    public void setCurrScore(int currScore) {
        this.currScore = currScore;
    }
}
