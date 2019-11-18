package com.example.infs_project.ui.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.infs_project.R;
import com.example.infs_project.RecipesDatabase;
import com.example.infs_project.async.InsertRecipesAsyncDelegate;
import com.example.infs_project.async.InsertRecipesAsyncTask;
import com.example.infs_project.model.Recipe;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

public class QuizFragment extends Fragment {

    private QuizViewModel quizViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        quizViewModel =
                ViewModelProviders.of(this).get(QuizViewModel.class);
        View root = inflater.inflate(R.layout.fragment_quiz, container, false);

        Button quizButton = root.findViewById(R.id.quiz_button);
        quizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), QuizActivity.class);
                startActivity(intent);
            }
        });


        //Star volley and gson
        final RequestQueue requestQueue =  Volley.newRequestQueue(getActivity());

        //Need correct api url with key
        String url = "https://api.spoonacular.com/recipes/informationBulk";

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Recipe[] objectsArray = gson.fromJson(response, Recipe[].class);
                List<Recipe> objectsList = Arrays.asList(objectsArray);
                //Testing to see array contents
                //for (Recipe r : objectsList) { System.out.println(r); }

                RecipesDatabase db = RecipesDatabase.getInstance(getContext());

                InsertRecipesAsyncTask insertRecipesAsyncTask = new InsertRecipesAsyncTask();
                insertRecipesAsyncTask.setDatabase(db);

                insertRecipesAsyncTask.setDelegate((InsertRecipesAsyncDelegate) QuizFragment.this);

                Recipe[] recipes = objectsList.toArray(new Recipe[objectsList.size()]);
                //Testing to see array contents
                //for (Recipe r : recipes) { System.out.println(r); }

                insertRecipesAsyncTask.execute(recipes);

                requestQueue.stop();
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"The request failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                requestQueue.stop();
            }
        };

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, responseListener,
                errorListener);

        requestQueue.add(stringRequest);
        //End volley and gson


        return root;
    }
}