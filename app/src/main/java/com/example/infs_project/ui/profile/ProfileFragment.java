package com.example.infs_project.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.infs_project.R;

public class ProfileFragment extends Fragment {
    private TextView totalPoints;
    private TextView caloriePoints;
    private TextView triviaPoints;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        totalPoints = root.findViewById(R.id.total_points);
        caloriePoints = root.findViewById(R.id.calorie_points);
        triviaPoints = root.findViewById(R.id.trivia_points);

        SharedPreferences settings = getActivity().getSharedPreferences("quizApp", Context.MODE_PRIVATE);
        int calorieScore = settings.getInt("totalScore", 0);
        int triviaTotalScore = settings.getInt("triviaTotalScore", 0);
        int overallScore = calorieScore+triviaTotalScore;

        totalPoints.setText(Integer.toString(overallScore));
        caloriePoints.setText(Integer.toString(calorieScore));
        triviaPoints.setText(Integer.toString(triviaTotalScore));

        return root;
    }
}