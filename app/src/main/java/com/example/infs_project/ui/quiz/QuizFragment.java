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
import com.example.infs_project.model.RandomResponse;
import com.example.infs_project.model.Recipe;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuizFragment extends Fragment {

    private QuizViewModel quizViewModel;
    private Button quizButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        quizViewModel =
                ViewModelProviders.of(this).get(QuizViewModel.class);
        View root = inflater.inflate(R.layout.fragment_quiz, container, false);

        quizButton = root.findViewById(R.id.quiz_button);
        quizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), QuizActivity.class);
                startActivity(intent);
            }
        });



        return root;
    }

}