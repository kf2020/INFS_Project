package com.example.infs_project.async;

import com.example.infs_project.model.Recipe;

import java.util.ArrayList;

public interface GetAllRecipesAsyncDelegate {
        void processFinish(ArrayList<Recipe> output);
}
