package com.example.infs_project.ui.quiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.infs_project.R;
import com.example.infs_project.ui.quiz.quiz_activity_fragments.QuestionFragment;

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
