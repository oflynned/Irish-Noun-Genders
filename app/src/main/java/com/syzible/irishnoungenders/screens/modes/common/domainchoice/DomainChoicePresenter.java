package com.syzible.irishnoungenders.screens.modes.common.domainchoice;

import android.content.Context;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.syzible.irishnoungenders.common.models.Category;
import com.syzible.irishnoungenders.common.persistence.LocalStorage;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class DomainChoicePresenter implements DomainChoiceInteractor.DomainCallback {

    private final DomainChoiceInteractor interactor = new DomainChoiceInteractor();
    private DomainChoiceView screen;

    DomainChoicePresenter(DomainChoiceView screen) {
        this.screen = screen;
    }

    void fetchCategories(Context context) {
        try {
            interactor.fetchDomains(context, this);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(Context context, List<String> domainList) {
        int selectedIndex = 0;
        List<Category> categoryList = new ArrayList<>();
        String currentCategory = LocalStorage.getStringPref(context, LocalStorage.Pref.CURRENT_CATEGORY);
        for (String domain : domainList) {
            Category category = new Category(domain, currentCategory);
            if (category.isChosen()) {
                selectedIndex = domainList.indexOf(domain);
            }
            categoryList.add(category);
        }

        int finalSelectedIndex = selectedIndex;
        screen.showCategoryList(categoryList, finalSelectedIndex);
    }

    @Override
    public void onFailure(String message) {

    }
}
