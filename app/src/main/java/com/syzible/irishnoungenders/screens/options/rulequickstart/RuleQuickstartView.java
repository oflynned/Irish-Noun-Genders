package com.syzible.irishnoungenders.screens.options.rulequickstart;

import com.hannesdorfmann.mosby3.mvp.MvpView;

import java.util.List;

interface RuleQuickstartView extends MvpView {
    void showMasculineHints(List<Explanation> explanations);

    void showFeminineHints(List<Explanation> explanations);
}
