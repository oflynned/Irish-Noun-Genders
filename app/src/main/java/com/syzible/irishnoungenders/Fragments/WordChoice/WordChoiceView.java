package com.syzible.irishnoungenders.fragments;

import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.syzible.irishnoungenders.objects.Noun;

public interface WordChoiceView extends MvpView {
    void showNewGuess(Noun noun);

    void onHighScore();

    void onCorrectAnswer();

    void onWrongAnswer();
}
