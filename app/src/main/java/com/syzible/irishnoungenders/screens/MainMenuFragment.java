package com.syzible.irishnoungenders.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hannesdorfmann.mosby3.mvp.MvpFragment;
import com.syzible.irishnoungenders.MainActivity;
import com.syzible.irishnoungenders.R;
import com.syzible.irishnoungenders.screens.modes.gender.GenderFragment;
import com.syzible.irishnoungenders.screens.options.settings.SettingsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainMenuFragment extends MvpFragment<MainMenuView, MainMenuPresenter> implements MainMenuView {

    private Unbinder unbinder;

    @BindView(R.id.main_menu_gender_mode)
    TextView genderMode;

    @BindView(R.id.main_menu_how_to_play)
    TextView howToPlay;

    @BindView(R.id.main_menu_settings)
    TextView settings;

    public MainMenuFragment() {
    }

    @NonNull
    @Override
    public MainMenuPresenter createPresenter() {
        return new MainMenuPresenter();
    }

    public static MainMenuFragment getInstance() {
        return new MainMenuFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        genderMode.setOnClickListener(v -> MainActivity.setFragment(getFragmentManager(), GenderFragment.getInstance()));

        howToPlay.setVisibility(View.GONE);
        settings.setOnClickListener(v -> startActivity(new Intent(getActivity(), SettingsActivity.class)));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void notifyModeUnimplemented() {
        Toast.makeText(getContext(), R.string.mode_coming_soon, Toast.LENGTH_LONG).show();
    }
}
