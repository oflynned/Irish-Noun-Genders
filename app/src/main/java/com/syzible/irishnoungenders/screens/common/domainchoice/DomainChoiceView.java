package com.syzible.irishnoungenders.screens.common.domainchoice;

import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.syzible.irishnoungenders.common.models.Category;

import java.util.List;

interface DomainChoiceView extends MvpView {
    void showCategoryList(List<Category> categoryList, int selectedIndex);
}
