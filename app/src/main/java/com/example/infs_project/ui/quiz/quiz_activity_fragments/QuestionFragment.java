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

    private OnFragmentInteractionListener mListener;
    private Activity currActivity;

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
            {"Question Fake Fake Fake", "I'm right!", "Choose me", "Choose me 2", "Choose me 3"}
    };

    public QuestionFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currActivity = getActivity();

        countLabel = (TextView) currActivity.findViewById(R.id.count_label);
        questionLabel = (TextView) currActivity.findViewById(R.id.question_label);
        answerBtn1 = (Button) currActivity.findViewById(R.id.answerBtn1);
        answerBtn2 = (Button) currActivity.findViewById(R.id.answerBtn2);
        answerBtn3 = (Button) currActivity.findViewById(R.id.answerBtn3);
        answerBtn4 = (Button) currActivity.findViewById(R.id.answerBtn4);

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

    public void checkAnswer(View view) {

        // Get pushed button
        Button answerBtn = (Button) currActivity.findViewById(view.getId());
        String btnText = answerBtn.getText().toString();

        String alertTitle;

        if(btnText.equals(rightAnswer)) {
            //Correct!
            alertTitle = "Correct!";
            rightAnswerCount++;
        } else {
            // Wrong...
            alertTitle = "Wrong...";
        }

        // Create Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(currActivity);
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

                } else {
                    quizCount++;
                    showNextQuiz();
                }
            }
        });

        builder.setCancelable(false);
        builder.show();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_question, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
