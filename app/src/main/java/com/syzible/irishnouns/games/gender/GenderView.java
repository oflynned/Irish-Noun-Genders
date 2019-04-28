package com.syzible.irishnouns.games.gender;

import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface GenderView extends MvpView {
    void showTitle(String noun);

    void showTranslation(String translation);

    void showHint();

    void notifyCorrectGuess();

    void notifyWrongGuess();

    void notifyEndOfDeck(String currentDomain, int deckSize);
}
