package com.syzible.irishnoungenders.screens.options.rulequickstart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.hannesdorfmann.mosby3.mvp.MvpFragment;
import com.syzible.irishnoungenders.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.Unbinder;

public class RuleQuickstartFragment extends MvpFragment<RuleQuickstartView, RuleQuickstartPresenter>
        implements RuleQuickstartView {

    private Unbinder unbinder;

    @BindView(R.id.rule_recycler_view)
    RecyclerView ruleRecyclerView;

    private RuleQuickstartFragment() {
    }

    @NotNull
    @Override
    public RuleQuickstartPresenter createPresenter() {
        return new RuleQuickstartPresenter();
    }

    public static RuleQuickstartFragment getInstance() {
        return new RuleQuickstartFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void showMasculineHints(List<Explanation> explanations) {

    }

    @Override
    public void showFeminineHints(List<Explanation> explanations) {

    }
}
