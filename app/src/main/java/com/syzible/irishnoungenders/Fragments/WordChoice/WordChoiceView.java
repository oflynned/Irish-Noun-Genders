package com.syzible.irishnoungenders.Fragments.WordChoice;

import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.syzible.irishnoungenders.Common.Pojo.Noun;

public interface WordChoiceView extends MvpView {
    void showNewGuess(Noun noun);

    void resetScore();

    void onHighScore();

    void onCorrectAnswer();

    void onWrongAnswer();
}
