package com.syzible.irishnouns.games.gender;

import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface GenderView extends MvpView {
    void showTitle(String noun);

    void showTranslation(String translation);

    void showHint();

    void notifyCorrectGuess();

    void notifyWrongGuess();

    void showHighScore(String highScore);

    void notifyEndOfDeck(String currentDomain, int deckSize);

    void notifyProgressLoss();

    void setScore(String score);

    void setChosenCategory(String category);

    void showCategoryScreen(String currentCategory);
}
