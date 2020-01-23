package com.syzible.irishnoungenders.screens.options.rulequickstart;

import android.content.Context;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.syzible.irishnoungenders.common.models.Noun;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

class RuleQuickstartPresenter extends MvpBasePresenter<RuleQuickstartView>
        implements Repository.RuleAPI{

    private Repository repository;

    RuleQuickstartPresenter() {
    }

    void fetchGenderExamples(Context context, Noun.Gender gender) {
        try {
            repository.fetchRules(context, gender, this);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResolve(List<Explanation> explanations, Noun.Gender gender) {

    }
}
