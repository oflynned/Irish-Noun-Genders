package com.syzible.irishnouns.games.gender;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.syzible.irishnouns.common.models.Noun;
import com.syzible.irishnouns.common.persistence.DomainNotFoundException;
import com.syzible.irishnouns.common.persistence.MalformedFileException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class GenderPresenter extends MvpBasePresenter<GenderView> {
    private GenderInteractor interactor;

    private List<Noun> shownNouns;
    private List<Noun> remainingNouns;
    private Noun currentNoun;

    private String currentDomain = "accounting";
    private int currentScore = 0;

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
            ifViewAttached(v -> v.notifyEndOfDeck(currentDomain, shownNouns.size()));
            return;
        }

        Collections.shuffle(remainingNouns);
        currentNoun = remainingNouns.get(0);
        ifViewAttached(v -> {
            v.showTitle(currentNoun.getTitle());
            v.showTranslation(currentNoun.getTranslations());
        });
    }

    public void makeGuess(Noun.Gender gender) {
        if (isGuessCorrect(gender)) {
            shownNouns.add(currentNoun);
            remainingNouns.remove(currentNoun);
            currentScore += 1;
            ifViewAttached(GenderView::notifyCorrectGuess);
        } else {
            currentScore = 0;
            ifViewAttached(GenderView::notifyWrongGuess);
        }

        ifViewAttached(v -> v.setScore(String.valueOf(currentScore)));
        pickNoun();
    }

    public void resetCurrentDeck() {
        remainingNouns = shownNouns;
        shownNouns = new ArrayList<>();
        ifViewAttached(v -> v.setScore("0"));
    }

    private boolean isGuessCorrect(Noun.Gender gender) {
        return currentNoun.getGender() == gender;
    }

    private boolean shouldShowHint(Noun noun) {
        return false;
    }
}
