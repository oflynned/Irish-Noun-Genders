package com.syzible.irishnoungenders.screens.modes.gender;

import android.content.Context;

import androidx.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.syzible.irishnoungenders.common.common.FeatureFlag;
import com.syzible.irishnoungenders.common.firebase.Achievements;
import com.syzible.irishnoungenders.common.firebase.Event;
import com.syzible.irishnoungenders.common.firebase.FirebaseLogger;
import com.syzible.irishnoungenders.common.models.Domain;
import com.syzible.irishnoungenders.common.models.Noun;
import com.syzible.irishnoungenders.common.persistence.API;
import com.syzible.irishnoungenders.common.persistence.Cache;
import com.syzible.irishnoungenders.common.persistence.DomainNotFoundException;
import com.syzible.irishnoungenders.common.persistence.GameRules;
import com.syzible.irishnoungenders.common.persistence.LocalStorage;
import com.syzible.irishnoungenders.common.persistence.MalformedFileException;
import com.syzible.irishnoungenders.screens.modes.common.domainchoice.DomainChoiceInteractor;
import com.syzible.irishnoungenders.screens.modes.common.interactors.ExperimentInteractor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class GenderPresenter extends MvpBasePresenter<GenderView> implements ExperimentInteractor.ExperimentCallback {
    private GenderInteractor genderInteractor;
    private DomainChoiceInteractor domainInteractor;
    private ExperimentInteractor experimentInteractor;

    private Noun currentNoun;
    private List<Noun> shownNouns;
    private List<Noun> remainingNouns;
    private List<String> masculineHints, feminineHints;

    private Domain currentDomain;
    private int currentScore = 0;

    @Override
    public void attachView(@NonNull GenderView view) {
        super.attachView(view);
        genderInteractor = new GenderInteractor();
        domainInteractor = new DomainChoiceInteractor();
        experimentInteractor = new ExperimentInteractor();
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
            ifViewAttached(v -> v.showHighScore(String.valueOf(currentScore)));
            return;
        }

        ifViewAttached(v -> v.showHighScore(String.valueOf(currentHighScore)));
    }

    void fetchNouns(Context context) throws IOException, JSONException {
        shownNouns = new ArrayList<>();

        // TODO refactor this heavily
        String displayLocale = LocalStorage.getStringPref(context, LocalStorage.Pref.DISPLAY_LANGUAGE);
        domainInteractor.fetchDomains(displayLocale, context, new DomainChoiceInteractor.DomainCallback() {
            @Override
            public void onSuccess(Context context, List<Domain> domainList) {
                String lastChosenCategoryFilename = Cache.getLastChosenCategory(context);
                for (Domain domain : domainList) {
                    if (domain.getFileName().equals(lastChosenCategoryFilename)) {
                        currentDomain = domain;
                        break;
                    }
                }

                try {
                    remainingNouns = genderInteractor.fetchNouns(currentDomain.getFileName());
                } catch (DomainNotFoundException | MalformedFileException e) {
                    e.printStackTrace();
                }

                ifViewAttached(v -> v.setChosenCategory(currentDomain.getLocalisedName().toLowerCase()));
                pickNoun(context);
            }

            @Override
            public void onFailure(String message) {

            }
        });


    }

    void pickNoun(Context context) {
        if (remainingNouns.size() == 0) {
            FirebaseLogger.logEvent(context, Event.DECK_FINISHED);
            ifViewAttached(v -> v.notifyEndOfDeck(currentDomain.getLocalisedName(), shownNouns.size()));
            return;
        }

        FirebaseLogger.logEvent(context, Event.SHOW_NEW_WORD);
        Collections.shuffle(remainingNouns);
        currentNoun = remainingNouns.get(0);
        checkHintIsAvailable(context, currentNoun);
        ifViewAttached(v -> {
            v.showChoiceButtons();
            v.showTranslation(currentNoun.getTranslations());
        });
    }

    void makeGuess(Context context, Noun.Gender gender) {
        if (isGuessCorrect(gender)) {
            FirebaseLogger.logEvent(context, Event.MAKE_GUESS, "guess_correct", true);
            shownNouns.add(currentNoun);
            remainingNouns.remove(currentNoun);
            incrementScore();
            checkNewHighScore(context);
            ifViewAttached(v -> v.notifyCorrectGuess(currentNoun));
        } else {
            FirebaseLogger.logEvent(context, Event.MAKE_GUESS, "guess_correct", false);
            resetScore();
            ifViewAttached(v -> v.notifyWrongGuess(currentNoun));
        }

        // TODO enable general access
        if (FeatureFlag.EARN_ACHIEVEMENTS.isEnabled()) {
            Achievements.incrementGuessCount(context);
            if (Achievements.hasPassedFirstSteps(context)) {
                ifViewAttached(v -> v.getAchievementListener().onAchievementUnlocked(Achievements.Achievement.FIRST_STEPS));
            }
        }

        // log experiment to remote db, should migrate this to firebase for ease of viewing events
        logExperiment(context, gender);
        ifViewAttached(v -> v.setScore(String.valueOf(currentScore)));
    }

    private void logExperiment(Context context, Noun.Gender attempt) {
        JSONObject payload = experimentInteractor.buildGenderExperimentPayload(currentDomain.getEn(), currentNoun, attempt);
        experimentInteractor.requestExperiment(context, payload, this);
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

    private void checkHintIsAvailable(Context context, Noun noun) {
        if (GameRules.wordHintsEnabled(context)) {
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
        }

        ifViewAttached(v -> {
            v.showTitle(noun.getTitle());
            v.showHint("");
        });
    }

    void changeCategory(Context context) {
        resetScore();
        String currentCategory = Cache.getLastChosenCategory(context);
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

    @Override
    public void onSuccess() {

    }

    @Override
    public void onFailure(int status, JSONObject response) {

    }

    public int getCurrentScore() {
        return currentScore;
    }
}
