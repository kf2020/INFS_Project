package com.example.infs_project;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.infs_project.model.Recipe;
import com.example.infs_project.model.RecipeDao;

@Database(entities = {Recipe.class}, version = 1, exportSchema = false)  // Replace "Book.class" with whatever your Book entity class is.
public abstract class RecipesDatabase extends RoomDatabase {
    public abstract RecipeDao recipeDao();          // Replace BookDao with whatever you name your DAO

    private static RecipesDatabase instance;
    public static RecipesDatabase getInstance(Context context) {

        if(instance == null) {
            instance = Room.databaseBuilder(context, RecipesDatabase.class, "recipeDb")
                    //.allowMainThreadQueries()    <== IMPORTANT TO NOTE:
                    //     This is NOT correct to do in a completed app.
                    //     Next week we will fix it, but for now this
                    //     line is necessary for the app to work.
                    //     This line will basically allow the database
                    //     queries to freeze the app.
                    .build();
        }
        return instance;
    }
}
