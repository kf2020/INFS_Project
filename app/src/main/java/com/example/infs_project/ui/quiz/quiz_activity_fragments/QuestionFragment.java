package com.example.infs_project.ui.quiz.quiz_activity_fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.infs_project.MainActivity;
import com.example.infs_project.R;
import com.example.infs_project.RecipesDatabase;
import com.example.infs_project.async.InsertRecipesAsyncDelegate;
import com.example.infs_project.async.InsertRecipesAsyncTask;
import com.example.infs_project.model.NutritionWidgetResponse;
import com.example.infs_project.model.RandomResponse;
import com.example.infs_project.model.Recipe;
import com.example.infs_project.ui.quiz.QuizFragment;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuestionFragment extends Fragment implements InsertRecipesAsyncDelegate {
    private LinearLayout loadingLayout;

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

    String quizData[][] = new String[5][5];
            // {"Question", "Right Answer", "Choice1", "Choice2", "Choice3"}


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

    @Override
    public void processFinish(ArrayList<Recipe> output) {
        // Nothing
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

        loadingLayout = view.findViewById(R.id.loading_layout);

        //Start volley and gson
        final RequestQueue requestQueue =  Volley.newRequestQueue(getActivity());

        //Need correct api url with key to get 5 random recipes
        String url = "https://api.spoonacular.com/recipes/random?number=5&apiKey=86828503a4f24dc5acab1e6988ce07e4";

        final InsertRecipesAsyncDelegate insertRecipesAsyncDelegate = this;
        final Recipe recipesArr[] = new Recipe[5];

        // create response listener
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                RandomResponse randomResponse = gson.fromJson(response, RandomResponse.class);
                List<Recipe> recipes = Arrays.asList(randomResponse.getRecipes());

                Recipe[] localArr = recipes.toArray(new Recipe[recipes.size()]);
                for (int i = 0; i <5; i++) {
                    recipesArr[i] = localArr[i];
                }
                RecipesDatabase db = RecipesDatabase.getInstance(getContext());


                // Async Task to insert recipes into DB
                InsertRecipesAsyncTask insertRecipesAsyncTask = new InsertRecipesAsyncTask();
                insertRecipesAsyncTask.setDatabase(db);
                insertRecipesAsyncTask.setDelegate(insertRecipesAsyncDelegate);
                insertRecipesAsyncTask.execute(recipesArr);

                // For each of the recipes, get their calories
                for(int i = 0; i < 5 ; i++) {

                    //Need correct api url with key to get 5 random recipes
                    String url2 = "https://api.spoonacular.com/recipes/"+recipesArr[i].getId()+"/nutritionWidget.json?apiKey=86828503a4f24dc5acab1e6988ce07e4";


                    // create response listener
                    final int finalI = i;
                    Response.Listener<String> responseListener2 = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Gson gson = new Gson();
                            NutritionWidgetResponse nutritionWidgetResponse = gson.fromJson(response, NutritionWidgetResponse.class);
                            recipesArr[finalI].setCalories(nutritionWidgetResponse.getCalories());

                            if (recipesArr[0].getCalories() != 0 && recipesArr[1].getCalories() != 0 && recipesArr[2].getCalories() != 0 && recipesArr[3].getCalories() != 0 && recipesArr[4].getCalories() != 0) {
                                setUpQuizQuestions(recipesArr);
                                requestQueue.stop();
                            }
                        }
                    };

                    Response.ErrorListener errorListener2 = new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getContext(),"The request failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            requestQueue.stop();
                            Intent i = new Intent(getActivity(), MainActivity.class);
                            startActivity(i);
                        }
                    };

                    StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url2, responseListener2,
                            errorListener2);

                    requestQueue.add(stringRequest2);

                }

            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"The request failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                requestQueue.stop();
                Intent i = new Intent(getActivity(), MainActivity.class);
                startActivity(i);
            }
        };

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, responseListener,
                errorListener);

        requestQueue.add(stringRequest);


        //End volley and gson


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

        return view;
    }

    private void setUpQuizQuestions(Recipe[] recipesArr) {
        // {"Question", "Right Answer", "Choice1", "Choice2", "Choice3"}
        for (int i = 0; i < 5; i++) {
            quizData[i][0] = "How many calories do you think this recipe has: "+recipesArr[i].getTitle();
            int calories = recipesArr[i].getCalories();
            quizData[i][1] = Integer.toString(calories);

            // calculate random numbers for other answers
            Random random = new Random();
            int low = calories > 150 ? calories - 100 : 25;
            int high = calories + 350;

            quizData[i][2] = Integer.toString(random.nextInt(high-low)+low);
            quizData[i][3] = Integer.toString(random.nextInt(high-low)+low);
            quizData[i][4] = Integer.toString(random.nextInt(high-low)+low);
        }

        loadingLayout.setVisibility(View.GONE);

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


}
