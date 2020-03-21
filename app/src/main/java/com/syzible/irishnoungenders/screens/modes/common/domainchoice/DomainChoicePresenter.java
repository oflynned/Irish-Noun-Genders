package com.syzible.irishnoungenders.screens.modes.common.domainchoice;

import android.content.Context;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.syzible.irishnoungenders.common.models.Category;
import com.syzible.irishnoungenders.common.models.Domain;
import com.syzible.irishnoungenders.common.persistence.LocalStorage;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class DomainChoicePresenter extends MvpBasePresenter<DomainChoiceView>
        implements DomainChoiceInteractor.DomainCallback {

    private DomainChoiceInteractor interactor;

    DomainChoicePresenter() {
        interactor = new DomainChoiceInteractor();
    }

    void fetchCategories(Context context) {
        try {
            interactor.fetchDomains(LocalStorage.getStringPref(context, LocalStorage.Pref.DISPLAY_LANGUAGE), context, this);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(Context context, List<Domain> domainList) {
        int selectedIndex = 0;
        List<Category> categoryList = new ArrayList<>();
        String currentCategory = LocalStorage.getStringPref(context, LocalStorage.Pref.CURRENT_CATEGORY);

        for (Domain domain : domainList) {
            Category category = new Category(currentCategory, domain);
            if (category.isChosen()) {
                selectedIndex = domainList.indexOf(domain);
            }
            categoryList.add(category);
        }

        int finalSelectedIndex = selectedIndex;
        ifViewAttached(v -> v.showCategoryList(categoryList, finalSelectedIndex));
    }

    @Override
    public void onFailure(String message) {

    }
}
