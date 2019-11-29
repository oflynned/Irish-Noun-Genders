package com.syzible.irishnoungenders.screens.options.tutorial;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.syzible.irishnoungenders.MainActivity;
import com.syzible.irishnoungenders.screens.modes.gender.GenderFragment;

public class TutorialFragment extends GenderFragment<TutorialView, TutorialPresenter>
        implements TutorialView {

    private TutorialFragment() {
    }

    @NonNull
    @Override
    public TutorialPresenter createPresenter() {
        return new TutorialPresenter();
    }

    public static TutorialFragment getInstance() {
        return new TutorialFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showInitialMessage();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void showInitialMessage() {
        ((MainActivity) getActivity()).showDialogMessage(
                "Welcome!",
                "In this tutorial, you'll learn how to play the game and get used of any controls.",
                null);
    }
}
