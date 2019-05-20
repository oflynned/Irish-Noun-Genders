package com.syzible.irishnouns.games.domainchoice;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby3.mvp.MvpFragment;
import com.syzible.irishnouns.R;
import com.syzible.irishnouns.common.models.Category;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DomainChoiceFragment extends MvpFragment<DomainChoiceView, DomainChoicePresenter>
        implements DomainChoiceView {

    private DomainAdaptor adaptor;
    private Unbinder unbinder;

    @BindView(R.id.category_list_recycler_view)
    RecyclerView recyclerView;

    public DomainChoiceFragment() {
    }

    @NonNull
    @Override
    public DomainChoicePresenter createPresenter() {
        return new DomainChoicePresenter();
    }

    public static DomainChoiceFragment getInstance() {
        return new DomainChoiceFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        presenter.fetchCategories(getContext());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void showCategoryList(List<Category> categoryList) {
        if (adaptor == null) {
            adaptor = new DomainAdaptor(getFragmentManager());

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adaptor);
        }

        adaptor.setCategoryList(categoryList);
        adaptor.notifyDataSetChanged();
    }
}
