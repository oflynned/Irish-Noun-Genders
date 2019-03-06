package com.syzible.irishnoungenders.Fragments.WordChoice;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.syzible.irishnoungenders.Common.Pojo.Noun;

import java.util.List;

public class WordChoicePresenter extends MvpBasePresenter<WordChoiceView>
        implements WordChoiceInteractor.NounCallback {

    private WordChoiceInteractor interactor;

    private int iteration;
    private String domain;
    private List<Noun> nouns;
    private List<String> masculineEndingHints;
    private List<String> feminineEndingHints;

    @Override
    public void attachView(@NonNull WordChoiceView view) {
        super.attachView(view);
        interactor = new WordChoiceInteractor();
    }

    private boolean shouldShowNounHint(Noun noun, List<String> hints) {
        return false;
    }

    private void fetchHints(Context context, Noun.Gender gender) {
        interactor.fetchHints(context, gender, this);
    }

    private void fetchNewNounSet(Context context, String domain) {
        interactor.fetchNounsFromDomain(context, domain, this);
    }

    public void fetchNewNoun(Context context, String domain) {
        if (domain.equals("*") && iteration % 5 == 0) {
            fetchNewNounSet(context, domain);
        }

        iteration++;

//        boolean shouldShowHints = Utils.shouldShowNounHint(currentNoun, hints);
//
//        if (!shouldShowHints) {
//            getView().showNewGuess(currentNoun);
//        } else {
//            String ending = Utils.getHint(currentNoun, hints);
//            String firstWord = currentNoun.getIrishWord().split(" ")[0];
//            String strippedNoun = firstWord.substring(0, firstWord.length() - ending.length());
//
//            // now we have <stripped><ending>
//            // we need <stripped><ending> <word> <word> ... if more words exist after the initial one
//
//            String otherWords = currentNoun.getIrishWord()
//                    .substring(firstWord.length(), currentNoun.getIrishWord().length());
//
//            gaNounTV.setText(strippedNoun);
//            gaNounHintTV.setText(ending);
//            gaNounOtherTV.setText(otherWords);
//        }
//
//        enTranslationTV.setText(currentNoun.getEnglishTranslation());
//
//        if (category.equals("*") && !newDomain.equals("")) {
//            categoryTV.setVisibility(View.VISIBLE);
//            categoryTV.setText("(" + newDomain.toLowerCase() + ")");
//            categoryTV.setTypeface(null, Typeface.ITALIC);
//        } else {
//            categoryTV.setVisibility(View.INVISIBLE);
//        }
    }

    @Override
    public void onHintsLoaded(List<String> masculineHints, List<String> feminineHints) {
        this.masculineEndingHints = masculineHints;
        this.feminineEndingHints = feminineHints;
    }

    @Override
    public void onDomainLoaded(List<Noun> nouns, String domain) {
        this.nouns = nouns;
        this.domain = domain;
    }

    @Override
    public void onNounLoaded(Noun noun, String domain) {
        getView().showNewGuess(noun, domain);
    }

    @Override
    public void onFailure() {
        getView().displayMessage("Something went wrong");
    }
}
