package com.syzible.irishnoungenders.screens.common.domainchoice;

import android.content.Context;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.syzible.irishnoungenders.common.models.Category;
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
            interactor.fetchDomains(context, this);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
        ifViewAttached(v -> v.showCategoryList(categoryList, finalSelectedIndex));
    }

    @Override
    public void onFailure(String message) {

    }
}
