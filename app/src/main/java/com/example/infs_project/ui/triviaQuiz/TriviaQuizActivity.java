package com.example.infs_project.ui.triviaQuiz;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.infs_project.R;
import com.example.infs_project.ui.quiz.quiz_activity_fragments.QuestionFragment;
import com.example.infs_project.ui.triviaQuiz.trivia_activity_fragments.TriviaQuestionFragment;

public class TriviaQuizActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.trivia_fragment_container, new TriviaQuestionFragment());
        fragmentTransaction.commit();
    }
}
