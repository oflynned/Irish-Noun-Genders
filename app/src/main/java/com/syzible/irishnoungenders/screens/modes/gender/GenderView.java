package com.syzible.irishnoungenders.screens.modes.gender;

import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.syzible.irishnoungenders.common.models.Noun;

public interface GenderView extends MvpView {
    void showTitle(String noun);

    void showHint(String hint);

    void showTranslation(String translation);

    void showChoiceButtons();

    void notifyCorrectGuess(Noun noun);

    void notifyWrongGuess(Noun noun);

    void showHighScore(String highScore);

    void notifyEndOfDeck(String currentDomain, int deckSize);

    void notifyLeavingGame();

    void notifyProgressLoss();

    void setScore(String score);

    void setChosenCategory(String category);

    void showCategoryScreen(String currentCategory);

    void returnToMainMenu();
}
