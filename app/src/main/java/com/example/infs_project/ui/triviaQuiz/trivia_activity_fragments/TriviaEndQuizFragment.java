package com.example.infs_project.ui.triviaQuiz.trivia_activity_fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.infs_project.MainActivity;
import com.example.infs_project.R;

public class TriviaEndQuizFragment extends Fragment {

    private TextView triviaResultLabel;
    private TextView triviaTotalScoreLabel;
    private Button triviaReturnButton;

    private int triviaScore;

    public TriviaEndQuizFragment() {
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
        View view = inflater.inflate(R.layout.fragment_end_trivia, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            triviaScore = bundle.getInt("RIGHT_ANSWER_COUNT", 0);
        }

        triviaResultLabel = (TextView) view.findViewById(R.id.trivia_result_label);
        triviaTotalScoreLabel = (TextView) view.findViewById(R.id.trivia_total_score);
        triviaReturnButton = (Button) view.findViewById(R.id.trivia_return_btn);

        triviaReturnButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent i = new Intent(getActivity(), MainActivity.class);
                startActivity(i);
            }
        });

        //Confused about this
        SharedPreferences settings = getActivity().getSharedPreferences("quizApp", Context.MODE_PRIVATE);
        int triviaTotalScore = settings.getInt("triviaTotalScore", 0);
        triviaTotalScore += triviaScore;

        triviaResultLabel.setText(triviaScore+" / 5");
        triviaTotalScoreLabel.setText("Total Score : " + triviaTotalScore);

        // Update total score
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("triviaTotalScore", triviaTotalScore);
        editor.commit();

        return view;
    }
}
