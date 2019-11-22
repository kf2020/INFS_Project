package com.example.infs_project.ui.triviaQuiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.infs_project.R;
import com.example.infs_project.ui.quiz.QuizActivity;

public class TriviaQuizFragment extends Fragment {

    private TriviaQuizViewModel triviaQuizViewModel;
    private Button triviaQuizButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        triviaQuizViewModel =
                ViewModelProviders.of(this).get(TriviaQuizViewModel.class);
        View root = inflater.inflate(R.layout.fragment_trivia, container, false);

        triviaQuizButton = root.findViewById(R.id.trivia_quiz_button);
        triviaQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TriviaQuizActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }
}