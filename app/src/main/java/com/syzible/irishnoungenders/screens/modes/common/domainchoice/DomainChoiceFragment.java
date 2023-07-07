package com.syzible.irishnoungenders.screens.modes.common.domainchoice;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.syzible.irishnoungenders.R;
import com.syzible.irishnoungenders.common.models.Category;
import com.syzible.irishnoungenders.databinding.FragmentCategoryListBinding;

import java.util.List;

public class DomainChoiceFragment extends Fragment implements DomainChoiceView {
    private DomainAdapter adaptor;
    private FragmentCategoryListBinding binding;
    private DomainChoicePresenter presenter;

    public DomainChoiceFragment() {
        super(R.layout.fragment_category_list);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCategoryListBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new DomainChoicePresenter(this);

        presenter.fetchCategories(getContext());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void showCategoryList(List<Category> categoryList, int selectedIndex) {
        if (adaptor == null) {
            adaptor = new DomainAdapter(getFragmentManager());

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            binding.categoryListRecyclerView.setNestedScrollingEnabled(false);
            binding.categoryListRecyclerView.setLayoutManager(layoutManager);
            binding.categoryListRecyclerView.setItemAnimator(new DefaultItemAnimator());
            binding.categoryListRecyclerView.setAdapter(adaptor);
        }

        adaptor.setCategoryList(categoryList);
        adaptor.notifyDataSetChanged();
        binding.categoryListRecyclerView.scrollToPosition(selectedIndex);
    }
}
