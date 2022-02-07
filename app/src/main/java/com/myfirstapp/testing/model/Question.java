package com.myfirstapp.testing.model;

import android.support.annotation.NonNull;


import java.util.List;

public class Question {
    @NonNull
    private final String mQuestion;
    @NonNull
    private final List<String> mChoiceList;
    private final int mAnswerIndex;

    public Question(@NonNull String question, @NonNull List<String> choiceList, int answerIndex) {
        mQuestion = question;
        mChoiceList = choiceList;
        mAnswerIndex = answerIndex;
    }

    @NonNull
    public String getQuestion() {
        return mQuestion;
    }

    @NonNull
    public List<String> getChoiceList() {
        return mChoiceList;
    }

    public int getAnswerIndex() {
        return mAnswerIndex;
    }
}