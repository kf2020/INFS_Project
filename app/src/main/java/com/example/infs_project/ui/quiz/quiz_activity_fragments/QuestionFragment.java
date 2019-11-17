package com.example.infs_project.ui.quiz.quiz_activity_fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.infs_project.R;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class QuestionFragment extends Fragment {

    private TextView countLabel;
    private TextView questionLabel;
    private Button answerBtn1;
    private Button answerBtn2;
    private Button answerBtn3;
    private Button answerBtn4;

    private String rightAnswer;
    private int rightAnswerCount = 0;
    private int quizCount = 1;
    static final private int QUIZ_COUNT = 5;

    ArrayList<ArrayList<String>> quizArray = new ArrayList<>();

    String quizData [][] = {
            // {"Question", "Right Answer", "Choice1", "Choice2", "Choice3"}
            {"Question Fake Fake 1", "I'm right!", "Choose me", "Choose me 2", "Choose me 3"},
            {"Question Fake Fake 2", "I'm right!", "Choose me", "Choose me 2", "Choose me 3"},
            {"Question Fake Fake 3", "I'm right!", "Choose me", "Choose me 2", "Choose me 3"},
            {"Question Fake Fake 4", "I'm right!", "Choose me", "Choose me 2", "Choose me 3"},
            {"Question Fake Fake 5", "I'm right!", "Choose me", "Choose me 2", "Choose me 3"},
            {"Question Fake Fake 6", "I'm right!", "Choose me", "Choose me 2", "Choose me 3"},
            {"Question Fake Fake 7", "I'm right!", "Choose me", "Choose me 2", "Choose me 3"},
            {"Question Fake Fake 8", "I'm right!", "Choose me", "Choose me 2", "Choose me 3"}
    };

    public QuestionFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public void showNextQuiz() {

        // Update quizCountLabel
        countLabel.setText("Question "+quizCount);

        // Generate random num btwn 0 and quizArray size - 1
        Random random = new Random();
        int randomNum = random.nextInt(quizArray.size());

        // Pick one quiz set
        ArrayList<String> quiz = quizArray.get(randomNum);

        // Set question and right answer
        // Array Format: {"Question", "Right Answer", "Choice1", "Choice2", "Choice3"}
        questionLabel.setText(quiz.get(0));
        rightAnswer = quiz.get(1);

        // Remove "Question" from quiz and shuffle choices
        quiz.remove(0);
        Collections.shuffle(quiz);

        // Set choices
        answerBtn1.setText(quiz.get(0));
        answerBtn2.setText(quiz.get(1));
        answerBtn3.setText(quiz.get(2));
        answerBtn4.setText(quiz.get(3));

        // Remove this quiz from quizArray
        quizArray.remove(randomNum);

    }

    public class checkAnswer implements OnClickListener {

        @Override
        public void onClick(View v) {

            // Get pushed button
            Button answerBtn = (Button) getActivity().findViewById(v.getId());
            String btnText = answerBtn.getText().toString();

            String alertTitle;

            if (btnText.equals(rightAnswer)) {
                //Correct!
                alertTitle = "Correct!";
                rightAnswerCount++;
            } else {
                // Wrong...
                alertTitle = "Wrong...";
            }

            // Create Dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(alertTitle);
            builder.setMessage("Answer: " + rightAnswer);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (quizCount == QUIZ_COUNT) {
                        // Show Result
                        Fragment endQuizFrag = new EndQuizFragment();
                        Bundle bundle = new Bundle();
                        bundle.putInt("RIGHT_ANSWER_COUNT", rightAnswerCount);
                        endQuizFrag.setArguments(bundle);
                        FragmentTransaction fr = getFragmentManager().beginTransaction();

                        fr.replace(R.id.fragment_container, endQuizFrag);
                        fr.commit();

                    } else {
                        quizCount++;
                        showNextQuiz();
                    }
                }
            });

            builder.setCancelable(false);
            builder.show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_question, container, false);


        countLabel = (TextView) view.findViewById(R.id.count_label);
        questionLabel = (TextView) view.findViewById(R.id.question_label);
        answerBtn1 = (Button) view.findViewById(R.id.answerBtn1);
        answerBtn2 = (Button) view.findViewById(R.id.answerBtn2);
        answerBtn3 = (Button) view.findViewById(R.id.answerBtn3);
        answerBtn4 = (Button) view.findViewById(R.id.answerBtn4);

        answerBtn1.setOnClickListener(new checkAnswer());
        answerBtn2.setOnClickListener(new checkAnswer());
        answerBtn3.setOnClickListener(new checkAnswer());
        answerBtn4.setOnClickListener(new checkAnswer());

        // Create quizArray from quizData
        for (int i = 0; i < quizData.length; i++) {
            // Prepare array
            ArrayList<String> tmpArray = new ArrayList<>();
            tmpArray.add(quizData[i][0]); // Question
            tmpArray.add(quizData[i][1]); // Right Answer
            tmpArray.add(quizData[i][2]); // Choice1
            tmpArray.add(quizData[i][3]); // Choice2
            tmpArray.add(quizData[i][4]); // Choice3


            // Add tmpArray to quizArray
            quizArray.add(tmpArray);
        }

        showNextQuiz();

        return view;
    }


}
