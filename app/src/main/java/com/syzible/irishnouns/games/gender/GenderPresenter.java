package com.syzible.irishnouns.games.gender;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.syzible.irishnouns.common.models.Noun;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class GenderPresenter extends MvpBasePresenter<GenderView> {
    private GenderInteractor interactor;

    private String currentDomain = "accounting";
    private List<Noun> shownNouns;
    private List<Noun> remainingNouns;
    private Noun currentNoun;

    @Override
    public void attachView(@NonNull GenderView view) {
        super.attachView(view);
        interactor = new GenderInteractor();
    }

    public void fetchNouns() {
        try {
            shownNouns = new ArrayList<>();
            remainingNouns = interactor.fetchNouns(currentDomain);
        } catch (DomainNotFoundException | MalformedFileException e) {
            e.printStackTrace();
        }
    }

    public void pickNoun() {
        if (remainingNouns.size() == 0) {
            getView().notifyEndOfDeck(currentDomain, shownNouns.size());
            return;
        }

        Collections.shuffle(remainingNouns);
        currentNoun = remainingNouns.get(0);
        getView().showTitle(currentNoun.getTitle());
        getView().showTranslation(currentNoun.getTranslations());
    }

    public void makeGuess(Noun.Gender gender) {
        if (isGuessCorrect(gender)) {
            shownNouns.add(currentNoun);
            remainingNouns.remove(currentNoun);
            getView().notifyCorrectGuess();
        } else {
            getView().notifyWrongGuess();
        }

        pickNoun();
    }

    private boolean isGuessCorrect(Noun.Gender gender) {
        return currentNoun.getGender() == gender;
    }

    private boolean shouldShowHint(Noun noun) {
        return false;
    }
}
