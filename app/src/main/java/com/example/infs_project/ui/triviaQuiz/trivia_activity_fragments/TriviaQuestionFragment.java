package com.example.infs_project.ui.triviaQuiz.trivia_activity_fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
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
import com.example.infs_project.model.TriviaResponse;
import com.example.infs_project.ui.quiz.quiz_activity_fragments.EndQuizFragment;
import com.example.infs_project.ui.quiz.quiz_activity_fragments.QuestionFragment;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TriviaQuestionFragment extends Fragment {
    private LinearLayout triviaLoadingLayout;

    private TextView countLabel;
    private TextView questionLabel;
    private Button triviaAnswerBtn1;
    private Button triviaAnswerBtn2;

    private String triviaRightAnswer;
    private int rightAnswerCount = 0;
    private int triviaCount = 1;
    static final private int TRIVIA_COUNT = 5;

    ArrayList<ArrayList<String>> triviaArray = new ArrayList<>();

    String triviaData[][] = new String[5][2];
    // {"Right Answer", "Choice1"}

    public TriviaQuestionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void showNextQuiz() {

        // Update quizCountLabel
        countLabel.setText("Trivia Question "+triviaCount);

        // Generate random num btwn 0 and quizArray size - 1
        Random random = new Random();
        int randomNum = random.nextInt(triviaArray.size());

        // Pick one quiz set
        ArrayList<String> trivia = triviaArray.get(randomNum);

        // Set question and right answer
        // Array Format: {"Right Answer", "Choice1"}
        triviaRightAnswer = trivia.get(0);


        // Remove "Question" from quiz and shuffle choices
        Collections.shuffle(trivia);

        // Set choices
        triviaAnswerBtn1.setText(trivia.get(0));
        triviaAnswerBtn2.setText(trivia.get(1));

        triviaArray.remove(randomNum);
    }

    public class checkAnswer implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            // Get pushed button
            Button answerBtn = (Button) getActivity().findViewById(v.getId());
            String btnText = answerBtn.getText().toString();

            String alertTitle;

            if (btnText.equals(triviaRightAnswer)) {
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
            builder.setMessage("Answer: " + triviaRightAnswer);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (triviaCount == TRIVIA_COUNT) {
                        // Show Result
                        Fragment endTriviaFrag = new TriviaEndQuizFragment();
                        Bundle bundle = new Bundle();
                        bundle.putInt("RIGHT_ANSWER_COUNT", rightAnswerCount);
                        endTriviaFrag.setArguments(bundle);
                        FragmentTransaction fr = getFragmentManager().beginTransaction();

                        fr.replace(R.id.trivia_fragment_container, endTriviaFrag);
                        fr.commit();

                    } else {
                        triviaCount++;
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
        View view =  inflater.inflate(R.layout.fragment_trivia_question, container, false);

        triviaLoadingLayout = view.findViewById(R.id.trivia_loading_layout);

        //Start volley and gson
        final RequestQueue requestQueue =  Volley.newRequestQueue(getActivity());

        final ArrayList<String> facts = new ArrayList<>();

        //Api url for random trivia fact
        String url = "https://api.spoonacular.com/food/trivia/random?apiKey=86828503a4f24dc5acab1e6988ce07e4";

        for (int i = 0; i < 5; i++) {
        // create response listener
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                TriviaResponse triviaResponse = gson.fromJson(response, TriviaResponse.class);
                String trivia = triviaResponse.getText();

                facts.add(trivia);

                if (facts.size() == 5) {
                    setUpTrivia(facts);
                    requestQueue.stop();
                }

            }

        };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), "The request failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    requestQueue.stop();
                    Intent i = new Intent(getActivity(), MainActivity.class);
                    startActivity(i);
                }
            };

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, responseListener,
                    errorListener);

            requestQueue.add(stringRequest);
        }

        //End volley and gson

        countLabel = (TextView) view.findViewById(R.id.trivia_count_label);
        triviaAnswerBtn1 = (Button) view.findViewById(R.id.trivia_answerBtn1);
        triviaAnswerBtn2 = (Button) view.findViewById(R.id.trivia_answerBtn2);

        triviaAnswerBtn1.setOnClickListener(new TriviaQuestionFragment.checkAnswer());
        triviaAnswerBtn2.setOnClickListener(new TriviaQuestionFragment.checkAnswer());

        return view;

    }


    private void setUpTrivia(ArrayList<String> facts) {

        String[] falseFactsArr = {"An average ear of corn has odd number of rows.",
        "Carrots only exist in an orange colour.",
                "Raw eels can be consumed directly by humans, as they are not poisonous.",
                "The classic fortune cookie first originated in Hunan, China.",
                "Cucumbers were first grown in Western Australia.",
                "Watermelons are a fruit that contain less water content than radishes.",
                "Apples will not float when placed in water.",
                "Walnuts are botanically called legumes because they grow underground.",
                "Radish and cabbage don't belong to the same plant family.",
                "Twinkies were first released in 1937 in the U.S.",
                "Eating celery burns more calories than you take in.",
                "Raw carrots are more nutritious than cooked carrots.",
                "Using margarine instead of butter will save calories.",
                "Adding salt to water changes the boiling point, therefore cooking food faster.",
                "Low fat foods are always better for you to eat.",
                "Sugar was once widely used as a form of currency.",
                "Basil is the most popular spice in the world that is used in cooking.",
                "Hummus is a dip that is made from beans.",
                "There are 40 flavours of jelly beans that exist in the world.",
                "China is the biggest exporter of food in the world.",
                "McDonalds was founded in 1950.",
                "The blue M&M was introduced in 1990.",
                "Seaweed is a multicellular algae that is not high in fibre.",
                "Apples originate from India.",
                "Polyglycerol is another word that can be used for sugar.",
                "Goulash is the national dish of Austria.",
                "German chocolate cake is a dessert that originates from Germany.",
                "There are 250 seeds on average in a strawberry.",
                "Almonds grow underground, rather than on trees.",
                "Yams and sweet potatoes are the same food.",
                "Searing a steak is a cooking technique that seals in its juices.",
                "Decaf coffee will have no caffeine in it.",
                "All of the alcohol in wine, beer or a liquor burns off when you cook or bake something with it.",
                "Most of the nutrients in potatoes reside within the skin.",
                "Pineapples grow out of trees, rather than directly from the ground."};
        ArrayList<String> falseFacts =  new ArrayList<>();
        falseFacts.addAll(Arrays.asList(falseFactsArr));


        // Generate random num btwn 0 and quizArray size - 1
        Random random = new Random();

        // {"Right Answer", "Choice1"}
        for (int i = 0; i < 5; i++) {
            int randomNum = random.nextInt(facts.size());

            triviaData[i][0] = facts.get(i);
            triviaData[i][1] = falseFacts.get(randomNum);

            falseFacts.remove(randomNum);
        }

        triviaLoadingLayout.setVisibility(View.GONE);

        // Create triviaArray from triviaData
        for (int i = 0; i < triviaData.length; i++) {
            // Prepare array
            ArrayList<String> tmpArray = new ArrayList<>();
            tmpArray.add(triviaData[i][0]); // Right Answer
            tmpArray.add(triviaData[i][1]); // Choice 1

            // Add tmpArray to quizArray
            triviaArray.add(tmpArray);
        }
        showNextQuiz();
    }




}
