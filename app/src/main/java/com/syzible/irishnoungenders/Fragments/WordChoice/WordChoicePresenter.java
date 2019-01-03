package com.syzible.irishnoungenders.Fragments.WordChoice;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.syzible.irishnoungenders.Common.Pojo.Noun;

public class WordChoicePresenter extends MvpBasePresenter<WordChoiceView> implements WordChoiceInteractor.Callback {

    private WordChoiceInteractor interactor;

    @Override
    public void attachView(@NonNull WordChoiceView view) {
        super.attachView(view);
        interactor = new WordChoiceInteractor();
    }

    public Noun fetchNewNoun() {
        return null;
    }

    public String fetchNewDomain() {
        return null;
    }

    @Override
    public void onSuccess(Context context) {

    }

    @Override
    public void onFailure() {

    }
}
