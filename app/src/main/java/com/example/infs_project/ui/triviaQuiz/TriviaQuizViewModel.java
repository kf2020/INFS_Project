package com.example.infs_project.ui.triviaQuiz;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TriviaQuizViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TriviaQuizViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}