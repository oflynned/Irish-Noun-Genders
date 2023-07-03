package com.syzible.irishnoungenders.screens.modes.gender;

import android.content.Context;

import com.syzible.irishnoungenders.common.models.Noun;
import com.syzible.irishnoungenders.common.persistence.Cache;
import com.syzible.irishnoungenders.common.persistence.DomainNotFoundException;
import com.syzible.irishnoungenders.common.persistence.GameRules;
import com.syzible.irishnoungenders.common.persistence.MalformedFileException;
import com.syzible.irishnoungenders.screens.modes.common.interactors.ExperimentInteractor;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class GenderPresenter implements ExperimentInteractor.ExperimentCallback {
    private final GenderInteractor genderInteractor = new GenderInteractor();
    private final ExperimentInteractor experimentInteractor = new ExperimentInteractor();

    private Noun currentNoun;
    private List<Noun> shownNouns;
    private List<Noun> remainingNouns;
    private List<String> masculineHints, feminineHints;

    private String currentDomain = "accounting";
    private int currentScore = 0;

    private final GenderView screen;

    GenderPresenter(GenderView screen) {
        this.screen = screen;
        fetchHints();
    }

    private void fetchHints() {
        try {
            masculineHints = genderInteractor.fetchHints(Noun.Gender.MASCULINE);
            feminineHints = genderInteractor.fetchHints(Noun.Gender.FEMININE);
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
            screen.showHighScore(String.valueOf(currentScore));
            return;
        }

        screen.showHighScore(String.valueOf(currentHighScore));
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
            remainingNouns = genderInteractor.fetchNouns(currentDomain);
        } catch (DomainNotFoundException | MalformedFileException e) {
            e.printStackTrace();
        }

        screen.setChosenCategory(currentDomain);
    }

    void pickNoun(Context context) {
        if (remainingNouns.size() == 0) {
            screen.notifyEndOfDeck(currentDomain, shownNouns.size());
            return;
        }

        Collections.shuffle(remainingNouns);
        currentNoun = remainingNouns.get(0);

        checkHintIsAvailable(context, currentNoun);
        screen.showChoiceButtons();
        screen.showTranslation(currentNoun.getTranslations());
    }

    void makeGuess(Context context, Noun.Gender gender) {
        if (isGuessCorrect(gender)) {
            shownNouns.add(currentNoun);
            remainingNouns.remove(currentNoun);
            incrementScore();
            checkNewHighScore(context);
            screen.notifyCorrectGuess(currentNoun);
        } else {
            resetScore();
            screen.notifyWrongGuess(currentNoun);
        }

        // log experiment to remote db, should migrate this to firebase for ease of viewing events
        logExperiment(context, gender);
        screen.setScore(String.valueOf(currentScore));
    }

    private void logExperiment(Context context, Noun.Gender attempt) {
        JSONObject payload = experimentInteractor.buildGenderExperimentPayload(currentDomain, currentNoun, attempt);
        experimentInteractor.requestExperiment(context, payload, this);
    }

    void resetCurrentDeck() {
        remainingNouns = shownNouns;
        shownNouns = new ArrayList<>();
        resetScore();
        screen.setScore("0");
    }

    private boolean isGuessCorrect(Noun.Gender gender) {
        return currentNoun.getGender() == gender;
    }

    private String trimHintFromTitle(Noun noun, String hint) {
        return noun.getTitle().substring(0, noun.getTitle().length() - hint.length());
    }

    private void checkHintIsAvailable(Context context, Noun noun) {
        if (GameRules.isGenderHintEnabled(context)) {
            List<String> hints = noun.getGender() == Noun.Gender.MASCULINE ? masculineHints : feminineHints;
            for (String hint : hints) {
                if (noun.getTitle().endsWith(hint)) {
                    String trimmedTitle = trimHintFromTitle(noun, hint);
                    screen.showTitle(trimmedTitle);
                    screen.showHint(hint);

                    return;
                }
            }
        }

        screen.showTitle(noun.getTitle());
        screen.showHint("");
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
        screen.showCategoryScreen(currentCategory);
    }

    void returnToMainMenu() {
        if (shownNouns.size() > 0) {
            screen.notifyLeavingGame();
            return;
        }

        screen.returnToMainMenu();
    }

    void showCategoryScreen(Context context) {
        if (shownNouns.size() > 0) {
            screen.notifyProgressLoss();
            return;
        }

        changeCategory(context);
    }

    @Override
    public void onSuccess() {
        System.out.println("Success!");
    }

    @Override
    public void onFailure(int status, JSONObject response) {
        System.out.println("Failed!");
    }

    public int getCurrentScore() {
        return currentScore;
    }
}
