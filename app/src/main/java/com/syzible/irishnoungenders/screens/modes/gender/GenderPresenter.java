package com.syzible.irishnoungenders.screens.modes.gender;

import android.content.Context;
import androidx.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.syzible.irishnoungenders.common.models.Noun;
import com.syzible.irishnoungenders.common.persistence.Cache;
import com.syzible.irishnoungenders.common.persistence.DomainNotFoundException;
import com.syzible.irishnoungenders.common.persistence.MalformedFileException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class GenderPresenter extends MvpBasePresenter<GenderView> {
    private GenderInteractor interactor;

    private Noun currentNoun;
    private List<Noun> shownNouns;
    private List<Noun> remainingNouns;
    private List<String> masculineHints, feminineHints;

    private String currentDomain = "accounting";
    private int currentScore = 0;

    @Override
    public void attachView(@NonNull GenderView view) {
        super.attachView(view);
        interactor = new GenderInteractor();
        fetchHints();
    }

    private void fetchHints() {
        try {
            masculineHints = interactor.fetchHints(Noun.Gender.MASCULINE);
            feminineHints = interactor.fetchHints(Noun.Gender.FEMININE);
        } catch (MalformedFileException e) {
            e.printStackTrace();
            masculineHints = feminineHints = new ArrayList<>();
        }
    }

    private void incrementScore() {
        currentScore += 1;
    }

    private void resetScore() {
        currentScore = 0;
    }

    void checkNewHighScore(Context context) {
        int currentHighScore = Cache.getHighScore(context);
        if (currentScore > currentHighScore) {
            Cache.setNewHighScore(context, currentScore);
            ifViewAttached(v -> v.showHighScore(String.valueOf(currentScore)));
            return;
        }

        ifViewAttached(v -> v.showHighScore(String.valueOf(currentHighScore)));
    }

    void fetchNouns(Context context) {
        shownNouns = new ArrayList<>();

        try {
            currentDomain = Cache.getLastChosenCategory(context);
        } catch (DomainNotFoundException e) {
            e.printStackTrace();
            Cache.setLastChosenCategoryFileName(context, "accounting");
            currentDomain = "accounting";
        }

        try {
            remainingNouns = interactor.fetchNouns(currentDomain);
        } catch (DomainNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedFileException e) {
            e.printStackTrace();
        }

        ifViewAttached(v -> v.setChosenCategory(currentDomain));
    }

    void pickNoun() {
        if (remainingNouns.size() == 0) {
            ifViewAttached(v -> v.notifyEndOfDeck(currentDomain, shownNouns.size()));
            return;
        }

        Collections.shuffle(remainingNouns);
        currentNoun = remainingNouns.get(0);
        checkHintIsAvailable(currentNoun);
        ifViewAttached(v -> {
            v.showChoiceButtons();
            v.showTranslation(currentNoun.getTranslations());
        });
    }

    void makeGuess(Context context, Noun.Gender gender) {
        if (isGuessCorrect(gender)) {
            shownNouns.add(currentNoun);
            remainingNouns.remove(currentNoun);
            incrementScore();
            checkNewHighScore(context);
            ifViewAttached(v -> v.notifyCorrectGuess(currentNoun));
        } else {
            resetScore();
            ifViewAttached(v -> v.notifyWrongGuess(currentNoun));
        }

        ifViewAttached(v -> v.setScore(String.valueOf(currentScore)));
    }

    void resetCurrentDeck() {
        remainingNouns = shownNouns;
        shownNouns = new ArrayList<>();
        resetScore();
        ifViewAttached(v -> v.setScore("0"));
    }

    private boolean isGuessCorrect(Noun.Gender gender) {
        return currentNoun.getGender() == gender;
    }

    private String trimHintFromTitle(Noun noun, String hint) {
        return noun.getTitle().substring(0, noun.getTitle().length() - hint.length());
    }

    private void checkHintIsAvailable(Noun noun) {
        List<String> hints = noun.getGender() == Noun.Gender.MASCULINE ? masculineHints : feminineHints;
        for (String hint : hints) {
            if (noun.getTitle().endsWith(hint)) {
                String trimmedTitle = trimHintFromTitle(noun, hint);
                ifViewAttached(v -> {
                    v.showTitle(trimmedTitle);
                    v.showHint(hint);
                });
                return;
            }
        }

        ifViewAttached(v -> {
            v.showTitle(noun.getTitle());
            v.showHint("");
        });
    }

    void changeCategory(Context context) {
        String currentCategory;
        try {
            currentCategory = Cache.getLastChosenCategory(context);
        } catch (DomainNotFoundException e) {
            e.printStackTrace();
            return;
        }

        resetScore();
        ifViewAttached(v -> v.showCategoryScreen(currentCategory));
    }

    void returnToMainMenu() {
        if (shownNouns.size() > 0) {
            ifViewAttached(GenderView::notifyLeavingGame);
            return;
        }

        ifViewAttached(GenderView::returnToMainMenu);
    }

    void showCategoryScreen(Context context) {
        if (shownNouns.size() > 0) {
            ifViewAttached(GenderView::notifyProgressLoss);
            return;
        }

        changeCategory(context);
    }
}
