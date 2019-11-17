package com.example.infs_project.async;

import com.example.infs_project.model.Recipe;

import java.util.ArrayList;

public interface InsertRecipesAsyncDelegate {
        void processFinish(ArrayList<Recipe> output);
}
