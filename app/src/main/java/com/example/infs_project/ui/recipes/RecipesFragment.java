package com.example.infs_project.ui.recipes;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.infs_project.R;
import com.example.infs_project.RecipeAdapter;
import com.example.infs_project.RecipesDatabase;
import com.example.infs_project.async.GetAllRecipesAsyncDelegate;
import com.example.infs_project.async.GetAllRecipesAsyncTask;
import com.example.infs_project.async.InsertRecipesAsyncDelegate;
import com.example.infs_project.model.RandomResponse;
import com.example.infs_project.model.Recipe;
import com.example.infs_project.model.SearchResponse;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecipesFragment extends Fragment implements GetAllRecipesAsyncDelegate {
    private RecyclerView recyclerView;
    private EditText searchInput;
    private ImageButton searchButton;
    private RecipesDatabase db;
    private GetAllRecipesAsyncDelegate getAllRecipesAsyncDelegate;
    private RecipeAdapter recipeAdapter;

    public static RecipesFragment newInstance() {
        return new RecipesFragment();
    }

    public RecipesFragment() {
        //empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recipes, container, false);
        recyclerView = view.findViewById(R.id.rv_main);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        searchButton = view.findViewById(R.id.search_button);
        searchInput = view.findViewById(R.id.search_input);

        db = RecipesDatabase.getInstance(this.getContext());
        getAllRecipesAsyncDelegate = (GetAllRecipesAsyncDelegate) this;

        recipeAdapter = new RecipeAdapter();
        // I save my results to the database so I can retrieve it later in my other activities.
        GetAllRecipesAsyncTask getAllRecipesAsyncTask = new GetAllRecipesAsyncTask();
        getAllRecipesAsyncTask.setDatabase(db);
        getAllRecipesAsyncTask.setDelegate(getAllRecipesAsyncDelegate);
        getAllRecipesAsyncTask.execute(0);


        setOnClick(searchButton, recipeAdapter);

        return view;
    }

    private void setOnClick(final ImageButton btn, final RecipeAdapter recipeAdapter) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

                String url = "https://api.spoonacular.com/recipes/search?query="+searchInput.getText()+"&number=30&apiKey=e66d050f20064f7f8531c769b5286774";

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Gson gson = new Gson();
                        SearchResponse filteredRecipesResponse = gson.fromJson(response, SearchResponse.class);

                        List<Recipe> recipesFiltered = Arrays.asList(filteredRecipesResponse.getResults());

                        recipeAdapter.setData(recipesFiltered);
                        recyclerView.setAdapter(recipeAdapter);

                        requestQueue.stop();
                    }
                };

                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "The request failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        requestQueue.stop();
                    }
                };

                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, responseListener,
                        errorListener);

                requestQueue.add(stringRequest);
            }
        });
    }

    @Override
    public void processFinish(ArrayList<Recipe> output) {
        final ArrayList<Recipe> recipes = output;
        if (recipes.size() < 1) {
            final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

            String url = "https://api.spoonacular.com/recipes/random?number=40&apiKey=e66d050f20064f7f8531c769b5286774";

            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Gson gson = new Gson();
                    RandomResponse randomResponse = gson.fromJson(response, RandomResponse.class);
                    Recipe[] allRecipesResponse = randomResponse.getRecipes();

                    List<Recipe> recipesResponse = Arrays.asList(allRecipesResponse);

                    System.out.println(recipesResponse);

                    //db.recipeDao().insertRecipes(recipesResponse);

                    recipeAdapter.setData(recipesResponse);
                    recyclerView.setAdapter(recipeAdapter);

                    requestQueue.stop();
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    recipeAdapter.setData(recipes);
                    recyclerView.setAdapter(recipeAdapter);

                    Toast.makeText(getContext(), "The request failed: " //+ error.getMessage()
                            , Toast.LENGTH_SHORT).show();
                    requestQueue.stop();
                }
            };

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, responseListener,
                    errorListener);

            requestQueue.add(stringRequest);

        } else {
            recipeAdapter.setData(recipes);
            recyclerView.setAdapter(recipeAdapter);
        }
    }
}
