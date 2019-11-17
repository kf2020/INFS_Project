package com.example.infs_project.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RecipeDao {
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insertRecipes(List<Recipe> recipes);

        @Query("SELECT * FROM recipe ORDER BY title")
        List<Recipe> getAllRecipesSorted();

        @Query("SELECT * FROM recipe WHERE id = :id")
        Recipe findRecipeById(long id);

}
