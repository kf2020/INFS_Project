package com.example.infs_project.ui.quiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Toast;

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
import com.example.infs_project.model.RandomResponse;
import com.example.infs_project.model.Recipe;
import com.example.infs_project.ui.quiz.quiz_activity_fragments.QuestionFragment;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, new QuestionFragment());
        fragmentTransaction.commit();
    }
}
