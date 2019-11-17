package com.example.infs_project.ui.quiz.quiz_activity_fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.infs_project.R;


public class EndQuizFragment extends Fragment {
    Activity currActivity;

    private TextView resultLabel;
    private TextView totalScoreLabel;

    private int score;


    public EndQuizFragment() {
        // Required empty public constructor
    }

     @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         Bundle bundle = this.getArguments();
         if (bundle != null) {
             score = bundle.getInt("RIGHT_ANSWER_COUNT", 0);
         }

        resultLabel = (TextView) currActivity.findViewById(R.id.result_label);
        totalScoreLabel = (TextView) currActivity.findViewById(R.id.total_score);

        SharedPreferences settings = currActivity.getSharedPreferences("quizApp", Context.MODE_PRIVATE);
        int totalScore = settings.getInt("totalScore", 0);
        totalScore += score;

        resultLabel.setText(score+" / 5");
        totalScoreLabel.setText("Total Score : " + totalScore);

        // Update total score
         SharedPreferences.Editor editor = settings.edit();
         editor.putInt("totalScore", totalScore);
         editor.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_end_quiz, container, false);
    }


}
