package com.syzible.irishnoungenders.screens.modes.common.domainchoice;

import com.syzible.irishnoungenders.common.models.Category;

import java.util.List;

interface DomainChoiceView {
    void showCategoryList(List<Category> categoryList, int selectedIndex);
}
