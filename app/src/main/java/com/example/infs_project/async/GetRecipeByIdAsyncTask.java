package com.example.infs_project.async;


import android.os.AsyncTask;

import com.example.infs_project.RecipesDatabase;
import com.example.infs_project.model.Recipe;

import java.util.ArrayList;
import java.util.Arrays;

public class GetRecipeByIdAsyncTask extends AsyncTask<Long, Integer, Recipe> {
    // This is just a scaffold example for a task that would handle inserting books into the database.
    // You need to complete the doInBackground and onPostExecute methods.
    // Then you will need to make your own class for a task that handles retrieving Recipes from the
    // database.
    // Refer to the tutorial slide for more information.

    // We store a variable for an object that implements our interface, so we know that whatever
    // is in here, knows how to handle the result of our task.
    private GetRecipeByIdAsyncDelegate delegate;

    // This asynctask will also need to be given a database instance, so it can perform database
    // queries. If your Room database class is named something else, change this.
    private RecipesDatabase database;

    public void setDelegate(GetRecipeByIdAsyncDelegate delegate) {
        this.delegate = delegate;
    }

    public void setDatabase(RecipesDatabase database) {
        this.database = database;
    }

    @Override
    protected Recipe doInBackground(Long... longs) {
        long id = longs[0];

        // When the task is finished, it will return.
        // You would normally want to return the result of your task.
        // For example, if my task was to get books from DB, I would make this method return the list
        // of books. The return value goes straight to onPostExecute.
        return database.recipeDao().findRecipeById(id);
    }

    @Override
    protected void onPostExecute(Recipe result) {
        // Once doInBackground is completed, this method will run.
        // The "result" comes from doInBackground.

        // This is where we give our result to the delegate and let them handle it.
        // Our delegate should be the original Fragment/Activity, so then it can use the result to
        // update the UI, for example.

        // TODO: Call the delegate's method with the results
        delegate.processFinish(result);
    }

}

