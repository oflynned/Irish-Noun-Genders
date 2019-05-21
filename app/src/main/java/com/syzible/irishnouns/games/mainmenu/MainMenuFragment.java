package com.syzible.irishnouns.mainmenu;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.syzible.irishnouns.MainActivity;
import com.syzible.irishnouns.R;
import com.syzible.irishnouns.games.gender.GenderFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainMenuFragment extends Fragment {

    private Unbinder unbinder;

    @BindView(R.id.main_menu_gender_mode)
    TextView genderMode;

    @BindView(R.id.main_menu_settings)
    TextView settings;

    public MainMenuFragment() {
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
        settings.setOnClickListener(v -> MainActivity.setFragmentBackstack(getFragmentManager(), SettingsFragment.getInstance()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
