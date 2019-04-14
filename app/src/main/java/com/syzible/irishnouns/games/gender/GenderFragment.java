package com.syzible.irishnouns.games.gender;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby3.mvp.MvpFragment;
import com.syzible.irishnouns.R;

public class GenderFragment extends MvpFragment<GenderView, GenderPresenter> implements GenderView {

    public GenderFragment() {
    }

    public static GenderFragment getInstance() {
        return new GenderFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_gender_main, container, false);
    }

    @NonNull
    @Override
    public GenderPresenter createPresenter() {
        return new GenderPresenter();
    }

    @Override
    public void showTitle(String noun) {

    }

    @Override
    public void showTranslation(String translation) {

    }

    @Override
    public void showHint() {

    }
}
