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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
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
    private ImageView recipeImage;
    private TextView recipeImageLoadingLabel;
    private Button answerBtn1;
    private Button answerBtn2;
    private Button answerBtn3;
    private Button answerBtn4;

    private String rightAnswer;
    private int rightAnswerCount = 0;
    private int quizCount = 1;
    static final private int QUIZ_COUNT = 5;

    ArrayList<ArrayList<String>> quizArray = new ArrayList<>();

    String quizData[][] = new String[5][6];
            // {"Question", "Right Answer", "Choice1", "Choice2", "Choice3", "imageURL"}


    public QuestionFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void showNextQuiz() {

        recipeImageLoadingLabel.setVisibility(View.VISIBLE);

        // Update quizCountLabel
        countLabel.setText("Question "+quizCount);

        // Generate random num btwn 0 and quizArray size - 1
        Random random = new Random();
        int randomNum = random.nextInt(quizArray.size());

        // Pick one quiz set
        ArrayList<String> quiz = quizArray.get(randomNum);

        System.out.println(quiz);
        // Set question and right answer
        // Array Format: {"Question", "Right Answer", "Choice1", "Choice2", "Choice3"}
        questionLabel.setText(quiz.get(0));
        rightAnswer = quiz.get(1);

        if (quiz.size() > 5 && quiz.get(5) != null && quiz.get(5) != null) {
            recipeImageLoadingLabel.setVisibility(View.GONE);
            Glide.with(getContext()).load(quiz.get(5)).into(recipeImage);
            quiz.remove(5);
        }

        // Remove "Question" from quiz and shuffle choices

        quiz.remove(0);
        Collections.shuffle(quiz);
        // Set choices
        System.out.println(quiz);
        answerBtn1.setText(quiz.get(0));
        answerBtn2.setText(quiz.get(1));
        answerBtn3.setText(quiz.get(2));
        answerBtn4.setText(quiz.get(3));

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
        String url = "https://api.spoonacular.com/recipes/random?number=10&apiKey=e66d050f20064f7f8531c769b5286774";

        final InsertRecipesAsyncDelegate insertRecipesAsyncDelegate = this;
        final Recipe recipesArr[] = new Recipe[10];

        // create response listener
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                RandomResponse randomResponse = gson.fromJson(response, RandomResponse.class);
                List<Recipe> recipes = Arrays.asList(randomResponse.getRecipes());

                Recipe[] localArr = recipes.toArray(new Recipe[recipes.size()]);
                for (int i = 0; i < 10; i++) {
                    recipesArr[i] = localArr[i];
                }
                RecipesDatabase db = RecipesDatabase.getInstance(getContext());


                // Async Task to insert recipes into DB
                InsertRecipesAsyncTask insertRecipesAsyncTask = new InsertRecipesAsyncTask();
                insertRecipesAsyncTask.setDatabase(db);
                insertRecipesAsyncTask.setDelegate(insertRecipesAsyncDelegate);
                insertRecipesAsyncTask.execute(recipesArr);

                final ArrayList<Integer> numResponses = new ArrayList<>();
                numResponses.add(0);

                // For each of the recipes, get their calories
                for(int i = 0; i < 10 ; i++) {

                    //Need correct api url with key to get 5 random recipes
                    String url2 = "https://api.spoonacular.com/recipes/"+recipesArr[i].getId()+"/nutritionWidget.json?apiKey=e66d050f20064f7f8531c769b5286774";


                    // create response listener
                    final int finalI = i;
                    Response.Listener<String> responseListener2 = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Gson gson = new Gson();
                            NutritionWidgetResponse nutritionWidgetResponse = gson.fromJson(response, NutritionWidgetResponse.class);
                            recipesArr[finalI].setCalories(nutritionWidgetResponse.getCalories());
                            numResponses.set(0, numResponses.get(0)+1);

                            if (numResponses.get(0) == 10) {
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

//                    stringRequest2.setRetryPolicy(new RetryPolicy() {
//                        @Override
//                        public int getCurrentTimeout() {
//                            return 50000;
//                        }
//
//                        @Override
//                        public int getCurrentRetryCount() {
//                            return 50000;
//                        }
//
//                        @Override
//                        public void retry(VolleyError error) throws VolleyError {
//
//                        }
//                    });

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
        recipeImage = (ImageView) view.findViewById(R.id.recipe_image);
        recipeImageLoadingLabel = (TextView) view.findViewById(R.id.loading_image_label);
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
        // {"Question", "Right Answer", "Choice1", "Choice2", "Choice3", "imageURL"}
        int i = 0, iter = 0;
        while (quizData[4][0] == null && iter < 10) {
            if (recipesArr[iter].getCalories() > 0) {
                quizData[i][0] = "How many calories do you think this recipe has: " + recipesArr[i].getTitle();
                int calories = recipesArr[i].getCalories();
                quizData[i][1] = Integer.toString(calories);
                quizData[i][5] = recipesArr[i].getImage();

                // calculate random numbers for other answers
                Random random = new Random();
                int low = calories > 150 ? calories - 100 : 25;
                int high = calories + 350;

                int wrong1 = 0;
                int wrong2 = 0;
                int wrong3 = 0;
                for (int j = 0; j < 3; j++) {
                    int next = random.nextInt(high - low) + low;

                    switch (j) {
                        case 0:
                            while (Math.abs(next - calories) < 100) {
                                next = random.nextInt(high - low) + low;
                            }
                            wrong1 = next;
                            quizData[i][2] = Integer.toString(wrong1);
                            break;
                        case 1:
                            while (Math.abs(next - calories) < 100 || Math.abs(next - wrong1) < 100) {
                                next = random.nextInt(high - low) + low;
                            }
                            wrong2 = next;
                            quizData[i][3] = Integer.toString(wrong2);
                            break;
                        case 2:
                            while (Math.abs(next - calories) < 100 || Math.abs(next - wrong1) < 100 || Math.abs(next - wrong2) < 100) {
                                next = random.nextInt(high - low) + low;
                            }
                            wrong3 = next;
                            quizData[i][4] = Integer.toString(wrong3);
                            break;
                    }

                }
                i++;
            }

            iter++;
        }
        //System.out.println("name:"+quizData[i][0] +"right: "+quizData[i][1] +"   wrong1: "+quizData[i][2]+"   wrong2: "+quizData[i][3]+"   wrong3: "+quizData[i][4]+"   imageURL: "+quizData[i][5]);


        loadingLayout.setVisibility(View.GONE);

        // Create quizArray from quizData
        for (int j = 0; j < quizData.length; j++) {
            // Prepare array
            ArrayList<String> tmpArray = new ArrayList<>();
            tmpArray.add(quizData[j][0]); // Question
            tmpArray.add(quizData[j][1]); // Right Answer
            tmpArray.add(quizData[j][2]); // Choice1
            tmpArray.add(quizData[j][3]); // Choice2
            tmpArray.add(quizData[j][4]); // Choice3
            tmpArray.add(quizData[j][5]); // ImageURL


            // Add tmpArray to quizArray
            quizArray.add(tmpArray);
        }

        showNextQuiz();
    }


}
