package com.example.infs_project.ui.recipes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.infs_project.R;
import com.example.infs_project.RecipesDatabase;
import com.example.infs_project.async.GetRecipeByIdAsyncDelegate;
import com.example.infs_project.async.GetRecipeByIdAsyncTask;
import com.example.infs_project.async.InsertRecipesAsyncDelegate;
import com.example.infs_project.async.InsertRecipesAsyncTask;
import com.example.infs_project.model.Recipe;
import com.example.infs_project.model.SummaryResponse;
import com.google.gson.Gson;

import java.util.ArrayList;

public class RecipeDetailActivity extends AppCompatActivity implements GetRecipeByIdAsyncDelegate {
    long recipeId;
    TextView title;
    TextView summary;
    ImageView recipeImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        title = findViewById(R.id.recipe_title);
        summary = findViewById(R.id.summary);
        //recipeImg = findViewById(R.id.recipe_img);

        recipeId = getIntent().getLongExtra("id", 0);
        title.setText(getIntent().getStringExtra("title"));


        final GetRecipeByIdAsyncDelegate getRecipeByIdAsyncDelegate = this;

        RecipesDatabase db = RecipesDatabase.getInstance(this);

        // Async Task to insert recipes into DB
        GetRecipeByIdAsyncTask getRecipeByIdAsyncTask = new GetRecipeByIdAsyncTask();
        getRecipeByIdAsyncTask.setDatabase(db);
        getRecipeByIdAsyncTask.setDelegate(getRecipeByIdAsyncDelegate);
        getRecipeByIdAsyncTask.execute(recipeId);


    }

    @Override
    public void processFinish(Recipe output) {
        final ArrayList<Recipe> recipes = new ArrayList<>();

        if (output == null) {
            // Get Recipe Description
            final RequestQueue requestQueue =  Volley.newRequestQueue(this);

            String url = "https://api.spoonacular.com/recipes/"+recipeId+"/information?includeNutrition=false";

            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Gson gson = new Gson();
                    Recipe recipe = gson.fromJson(response, Recipe.class);
                    recipes.add(recipe);

                    title.setText(recipe.getTitle());
                    getSummary(recipe);

                    requestQueue.stop();
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(getApplicationContext(),"The request failed: " //+ error.getMessage()
                            , Toast.LENGTH_SHORT).show();
                    requestQueue.stop();
                }
            };

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, responseListener,
                    errorListener);

            requestQueue.add(stringRequest);
        } else {
            recipes.add(output);
            getSummary(output);
        }


    }

    private void getSummary(Recipe recipe) {
        Recipe finalRecipe = recipe;
        // Load image if possible
//        if (finalRecipe.getImage() != null) {
//            Glide.with(this).load(finalRecipe.getImage()).into(recipeImg);
//        }

        // Get Recipe Description
        final RequestQueue requestQueue =  Volley.newRequestQueue(this);

        String url = "https://api.spoonacular.com/recipes/"+finalRecipe.getId()+"/summary?apikey=e66d050f20064f7f8531c769b5286774";

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Gson gson = new Gson();
                SummaryResponse summaryResponse = gson.fromJson(response, SummaryResponse.class);

                summary.setText(summaryResponse.getSummary());

                requestQueue.stop();
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),"The request failed: " //+ error.getMessage()
                        , Toast.LENGTH_SHORT).show();
                requestQueue.stop();
            }
        };

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, responseListener,
                errorListener);

        requestQueue.add(stringRequest);
    }
}
