package com.syzible.irishnouns.games.gender;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.syzible.irishnouns.common.models.Noun;

import java.util.List;

class GenderPresenter extends MvpBasePresenter<GenderView> {
    private GenderInteractor interactor;

    @Override
    public void attachView(@NonNull GenderView view) {
        super.attachView(view);
        interactor = new GenderInteractor();
    }

    public void fetchNouns() {
        List<Noun> nouns;
        try {
            nouns = interactor.fetchNouns("accounting");
            for (Noun noun : nouns)
                System.out.println(noun);
        } catch (DomainNotFoundException | MalformedFileException e) {
            e.printStackTrace();
        }
    }
}
