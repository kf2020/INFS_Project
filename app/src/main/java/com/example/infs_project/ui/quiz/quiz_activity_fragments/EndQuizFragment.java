package com.example.infs_project.ui.quiz.quiz_activity_fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.infs_project.MainActivity;
import com.example.infs_project.R;
import com.example.infs_project.ui.quiz.QuizActivity;


public class EndQuizFragment extends Fragment {

    private TextView resultLabel;
    private TextView totalScoreLabel;
    private Button returnButton;

    private int score;


    public EndQuizFragment() {
        // Required empty public constructor
    }

     @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_end_quiz, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            score = bundle.getInt("RIGHT_ANSWER_COUNT", 0);
        }

        resultLabel = (TextView) view.findViewById(R.id.result_label);
        totalScoreLabel = (TextView) view.findViewById(R.id.total_score);
        returnButton = (Button) view.findViewById(R.id.return_btn);

        returnButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent i = new Intent(getActivity(), MainActivity.class);
                startActivity(i);
            }
        });

        SharedPreferences settings = getActivity().getSharedPreferences("quizApp", Context.MODE_PRIVATE);
        int totalScore = settings.getInt("totalScore", 0);
        totalScore += score;

        resultLabel.setText(score+" / 5");
        totalScoreLabel.setText("Total Score : " + totalScore);

        // Update total score
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("totalScore", totalScore);
        editor.commit();

        return view;
    }


}
