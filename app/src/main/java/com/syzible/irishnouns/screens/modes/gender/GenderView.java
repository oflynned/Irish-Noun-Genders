package com.syzible.irishnouns.screens.modes.gender;

import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface GenderView extends MvpView {
    void showTitle(String noun);

    void showHint(String hint);

    void showTranslation(String translation);

    void notifyCorrectGuess();

    void notifyWrongGuess();

    void showHighScore(String highScore);

    void notifyEndOfDeck(String currentDomain, int deckSize);

    void notifyLeavingGame();

    void notifyProgressLoss();

    void setScore(String score);

    void setChosenCategory(String category);

    void showCategoryScreen(String currentCategory);

    void returnToMainMenu();
}
